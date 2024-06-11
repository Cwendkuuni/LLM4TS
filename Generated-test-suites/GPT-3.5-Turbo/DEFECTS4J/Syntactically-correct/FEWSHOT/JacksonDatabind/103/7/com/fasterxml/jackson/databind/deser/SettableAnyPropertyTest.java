package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;
    private BeanProperty property;
    private AnnotatedMember setter;
    private JavaType type;
    private KeyDeserializer keyDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer typeDeserializer;
    private DeserializationConfig config;

    @Before
    public void setUp() {
        property = mock(BeanProperty.class);
        setter = mock(AnnotatedMember.class);
        type = mock(JavaType.class);
        keyDeserializer = mock(KeyDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        config = mock(DeserializationConfig.class);

        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, typeDeserializer);
    }

    @Test
    public void testWithNullValueDeserializer() {
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(null);

        assertNull(newSettableAnyProperty._valueDeserializer);
    }

    @Test
    public void testWithNonNullValueDeserializer() {
        JsonDeserializer<Object> newDeserializer = mock(JsonDeserializer.class);

        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newDeserializer);

        assertEquals(newDeserializer, newSettableAnyProperty._valueDeserializer);
    }

    @Test
    public void testFixAccess() {
        settableAnyProperty.fixAccess(config);

        verify(setter).fixAccess(eq(false));
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        verify(setter).callOnWith(eq(instance), propertyCaptor.capture(), eq(null));
        assertEquals(propName, propertyCaptor.getValue());
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        JsonToken t = JsonToken.VALUE_NULL;

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p).getCurrentToken();
        verify(valueDeserializer).getNullValue(ctxt);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        JsonToken t = JsonToken.VALUE_TRUE;
        Object key = "key";
        Object value = "value";

        ArgumentCaptor<JsonParser> parserCaptor = ArgumentCaptor.forClass(JsonParser.class);
        ArgumentCaptor<DeserializationContext> ctxtCaptor = ArgumentCaptor.forClass(DeserializationContext.class);
        ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<String> propNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> keyCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        verify(p).getCurrentToken();
        verify(_keyDeserializer).deserializeKey(propName, ctxt);
        verify(_valueDeserializer).deserialize(parserCaptor.capture(), ctxtCaptor.capture());
        verify(setter).callOnWith(instanceCaptor.capture(), propNameCaptor.capture(), valueCaptor.capture());

        assertEquals(p, parserCaptor.getValue());
        assertEquals(ctxt, ctxtCaptor.getValue());
        assertEquals(instance, instanceCaptor.getValue());
        assertEquals(propName, propNameCaptor.getValue());
        assertEquals(value, valueCaptor.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetWithField() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        Object key = "key";
        Object value = "value";

        ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Object> keyCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);

        settableAnyProperty.set(instance, propName, value);

        verify(setter).callOnWith(instanceCaptor.capture(), keyCaptor.capture(), valueCaptor.capture());

        assertEquals(instance, instanceCaptor.getValue());
        assertEquals(key, keyCaptor.getValue());
        assertEquals(value, valueCaptor.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetWithMethod() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "prop";
        Object key = "key";
        Object value = "value";

        ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Object> keyCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);

        settableAnyProperty.set(instance, propName, value);

        verify(setter).callOnWith(instanceCaptor.capture(), keyCaptor.capture(), valueCaptor.capture());

        assertEquals(instance, instanceCaptor.getValue());
        assertEquals(key, keyCaptor.getValue());
        assertEquals(value, valueCaptor.getValue());
    }

    @Test
    public void test_throwAsIOE_withIllegalArgumentException() throws IOException {
        String propName = "prop";
        Object value = new Object();
        IllegalArgumentException exception = new IllegalArgumentException("test");

        try {
            settableAnyProperty._throwAsIOE(exception, propName, value);
        } catch (JsonMappingException e) {
            assertEquals("Problem deserializing \"any\" property 'prop' of class null (expected type: null; actual type: java.lang.Object)", e.getMessage());
            assertEquals(exception, e.getCause());
        }
    }

    @Test
    public void test_throwAsIOE_withIOException() throws IOException {
        IOException exception = new IOException("test");

        try {
            settableAnyProperty._throwAsIOE(exception, null, null);
        } catch (IOException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void test_throwAsIOE_withRuntimeException() throws IOException {
        RuntimeException exception = new RuntimeException("test");

        try {
            settableAnyProperty._throwAsIOE(exception, null, null);
        } catch (RuntimeException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void test_throwAsIOE_withThrowable() throws IOException {
        Exception exception = new Exception("test");

        try {
            settableAnyProperty._throwAsIOE(exception, null, null);
        } catch (JsonMappingException e) {
            assertEquals(exception, e.getCause());
        }
    }
}