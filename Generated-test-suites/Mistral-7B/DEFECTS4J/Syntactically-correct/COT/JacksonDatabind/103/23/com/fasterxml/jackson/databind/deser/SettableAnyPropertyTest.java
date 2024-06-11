package com.example.junit; // replace with your test package if needed

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private Object instance;
    private BeanProperty property;
    private AnnotatedMember setter;
    private JavaType type;
    private KeyDeserializer keyDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;

    @Before
    public void setUp() throws Exception {
        property = Mockito.mock(BeanProperty.class);
        setter = Mockito.mock(AnnotatedMember.class);
        instance = Mockito.mock(Object.class);
        type = Mockito.mock(JavaType.class);
        keyDeserializer = Mockito.mock(KeyDeserializer.class);
        valueDeserializer = Mockito.mock(JsonDeserializer.class);
        valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
    }

    @Test
    public void testSettableAnyPropertyConstructor() throws Exception {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws Exception {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertEquals(settableAnyProperty, newSettableAnyProperty);
    }

    @Test
    public void testFixAccess() throws Exception {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.fixAccess(null);
        verify(setter, times(1)).fixAccess(false);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String propName = "testProperty";
        Object value = Mockito.mock(Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value);
        when(keyDeserializer.deserializeKey(propName, context)).thenReturn(propName);

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(setter, times(1)).callOnWith(instance, propName, value);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetUnresolvedForwardReference() throws Exception {
        String propName = "testProperty";
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueDeserializer.getObjectIdReader()).thenReturn(null);
        when(reference.getRoid()).thenReturn(Mockito.mock(ObjectIdReader.class));

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(reference, times(1)).getRoid();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetInvalidType() throws Exception {
        String propName = "testProperty";
        Object value = Mockito.mock(Object.class);
        Object invalidValue = Mockito.mock(Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value);
        when(valueTypeDeserializer.deserializesFromObject(value)).thenReturn(invalidValue.getClass());

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        fail();
    }

    @Test
    public void testSet() throws Exception {
        Object key = "testKey";
        Object value = Mockito.mock(Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getValue(instance)).thenReturn(Map.of());
        settableAnyProperty.set(instance, key, value);

        verify(setter, times(1)).callOnWith(instance, key, value);
    }

    @Test(expected = IOException.class)
    public void testSetIOException() throws Exception {
        Object instance = Mockito.mock(Object.class);
        Object key = "testKey";
        Object value = Mockito.mock(Object.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        doThrow(new IOException("Test error")).when(settableAnyProperty)._throwAsIOE(any(), any(), eq(value));

        settableAnyProperty.set(instance, key, value);

        fail();
    }
}