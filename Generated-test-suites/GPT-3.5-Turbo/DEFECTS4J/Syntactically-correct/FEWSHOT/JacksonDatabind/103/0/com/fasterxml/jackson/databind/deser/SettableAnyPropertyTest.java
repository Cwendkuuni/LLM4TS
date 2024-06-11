package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class SettableAnyPropertyTest {
    private SettableAnyProperty settableAnyProperty;
    private BeanProperty property;
    private AnnotatedMember setter;
    private JavaType type;
    private KeyDeserializer keyDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer typeDeserializer;

    @Before
    public void setup() {
        property = mock(BeanProperty.class);
        setter = mock(AnnotatedMember.class);
        type = mock(JavaType.class);
        keyDeserializer = mock(KeyDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer,
                typeDeserializer);
    }

    @Test
    public void testDeserializeAndSet_WhenKeyDeserializationFails() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "propertyName";

        when(keyDeserializer.deserializeKey(propName, context)).thenThrow(new JsonParseException(parser, "error"));

        try {
            settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
        } catch (JsonParseException e) {
            assertEquals("error", e.getMessage());
            return;
        }

        throw new AssertionError("Expected JsonParseException to be thrown");
    }

    @Test
    public void testDeserializeAndSet_WithUnresolvedForwardReference_AndNoObjectIdReader() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "propertyName";
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(valueDeserializer.getObjectIdReader()).thenReturn(null);
        when(reference.getRoid()).thenReturn(null);

        try {
            settableAnyProperty.deserializeAndSet(parser, context, instance, propName);
        } catch (JsonMappingException e) {
            assertEquals("Unresolved forward reference but no identity info.", e.getMessage());
            return;
        }

        throw new AssertionError("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testDeserializeAndSet_WithUnresolvedForwardReference_AndObjectIdReader() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "propertyName";
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        ObjectIdReader oid = mock(ObjectIdReader.class);
        when(valueDeserializer.getObjectIdReader()).thenReturn(oid);
        when(reference.getRoid()).thenReturn(oid);

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(oid, times(1)).appendReferring(any(SettableAnyPropertyTest.AnySetterReferring.class));
    }

    @Test
    public void testDeserialize_WithValueDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, context)).thenReturn("value");

        settableAnyProperty.deserializeAndSet(parser, context, new Object(), "property");

        verify(valueDeserializer, times(0)).deserializeWithType(any(), any(), any());
        verify(setter, times(1)).callOnWith(any(), any(), any());
    }

    @Test
    public void testDeserialize_WithValueTypeDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(valueDeserializer.deserializeWithType(parser, context, typeDeserializer)).thenReturn("value");

        settableAnyProperty.deserializeAndSet(parser, context, new Object(), "property");

        verify(valueDeserializer, times(1)).deserializeWithType(any(), any(), any());
        verify(setter, times(1)).callOnWith(any(), any(), any());
    }

    @Test
    public void testDeserialize_WithNullValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(valueDeserializer.getNullValue(context)).thenReturn(null);

        settableAnyProperty.deserializeAndSet(parser, context, new Object(), "property");

        verify(valueDeserializer, times(0)).deserializeWithType(any(), any(), any());
        verify(setter, times(1)).callOnWith(any(), any(), any());
    }

    @Test
    public void testSet_WhenSetterIsField_AndInstanceIsNull() throws IOException {
        Object instance = null;
        String propName = "propertyName";
        Object value = new Object();

        settableAnyProperty.set(instance, propName, value);

        verify(setter, times(0)).getValue(any());
        verify(setter, times(0)).callOnWith(any(), any(), any());
    }

    @Test
    public void testSet_WhenSetterIsField_AndInstanceIsNotNull() throws IOException {
        Object instance = new Object();
        String propName = "propertyName";
        Object value = new Object();
        AnnotatedField field = (AnnotatedField) setter;

        field.setValue(instance, null);

        settableAnyProperty.set(instance, propName, value);

        verify(setter, times(1)).getValue(any());
        verify(setter, times(1)).callOnWith(any(), any(), any());
    }

    @Test
    public void testSet_WhenSetterIsMethod() throws IOException {
        Object instance = new Object();
        String propName = "propertyName";
        Object value = new Object();

        settableAnyProperty.set(instance, propName, value);

        verify(setter, times(0)).getValue(any());
        verify(((AnnotatedMethod) setter), times(1)).callOnWith(any(), any(), any());
    }

    @Test
    public void testThrowAsIOE_WhenIAE() throws IOException {
        try {
            settableAnyProperty._throwAsIOE(new IllegalArgumentException("error"), "property", new Object());
        } catch (JsonMappingException e) {
            assertEquals("Problem deserializing \"any\" property 'property' of class null "
                    + "(expected type: null; actual type: java.lang.Object), problem: error", e.getMessage());
            return;
        }

        throw new AssertionError("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testThrowAsIOE_WhenExceptionNotIAE() throws IOException {
        try {
            settableAnyProperty._throwAsIOE(new Exception(), "property", new Object());
        } catch (JsonMappingException e) {
            assertEquals("null", e.getMessage());
            return;
        }

        throw new AssertionError("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testThrowAsIOE_WhenExceptionIsIOException() throws IOException {
        try {
            settableAnyProperty._throwAsIOE(new IOException(), "property", new Object());
        } catch (IOException e) {
            return;
        }

        throw new AssertionError("Expected IOException to be thrown");
    }

    @Test
    public void testThrowAsIOE_WhenExceptionIsRuntimeException() throws IOException {
        try {
            settableAnyProperty._throwAsIOE(new RuntimeException(), "property", new Object());
        } catch (RuntimeException e) {
            return;
        }

        throw new AssertionError("Expected RuntimeException to be thrown");
    }

    private class AnySetterReferring extends Referring {
        private final SettableAnyProperty _parent;
        private final Object _pojo;
        private final String _propName;

        public AnySetterReferring(SettableAnyProperty parent, UnresolvedForwardReference reference, Class<?> type,
                Object instance, String propName) {
            super(reference, type);
            _parent = parent;
            _pojo = instance;
            _propName = propName;
        }

        @Override
        public void handleResolvedForwardReference(Object id, Object value) throws IOException {
            if (!hasId(id)) {
                throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id.toString()
                        + "] that wasn't previously registered.");
            }
            _parent.set(_pojo, _propName, value);
        }
    }
}