package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(setterlessProperty);
    }

    @Test
    public void testWithName() {
        BeanPropertyDefinition propDef = null;
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, new PropertyName("testName"));
        assertEquals("testName", setterlessProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, new JsonDeserializer<>());
        assertNotNull(setterlessProperty._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null);
        assertNotNull(setterlessProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null);
        setterlessProperty.fixAccess(null);
        assertTrue(setterlessProperty._annotated.isAccessible());
    }

    @Test
    public void testGetAnnotation() {
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotation(Mockito.any(Annotation.class))).thenReturn(mock(Annotation.class));

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, annotatedMethod);
        assertEquals(mock(Annotation.class), setterlessProperty.getAnnotation(Mockito.any(Annotation.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethod() throws IOException {
        Object instance = new Object();
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null);
        setterlessProperty.set(instance, "testValue");
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("testValue");

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, new AnnotatedMethod());

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertTrue(instance instanceof String);
        assertEquals("testValue", instance.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetterlessPropertyDeserializeAndReturn() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null);
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethodWithNullValue() throws IOException {
        Object instance = new Object();
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null);
        setterlessProperty.set(instance, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, new AnnotatedMethod());
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithInvalidToken() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, new AnnotatedMethod());
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testDeserializeAndSetWithEmptyCollection() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getText()).thenReturn("");

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, new AnnotatedMethod());
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertTrue(instance instanceof Collection);
        assertTrue(instance.getClass().isEmpty());
    }

    @Test
    public void testDeserializeAndSetWithCollectionValue() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        JsonToken token = JsonToken.START_ARRAY;
        when(jsonParser.getCurrentToken()).thenReturn(token);

        Object value = new Object();
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("testValue1");
        when(jsonParser.nextToken()).thenReturn(token);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(token);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, new AnnotatedMethod());
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertTrue(instance instanceof List);
        assertEquals(2, ((List) instance).size());
        assertEquals("testValue1", ((List) instance).get(0));
        assertNull(((List) instance).get(1));
    }

    @Test
    public void testDeserializeAndSetWithMapValue() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        JsonToken token = JsonToken.START_OBJECT;
        when(jsonParser.getCurrentToken()).thenReturn(token);

        String key1 = "key1";
        when(jsonParser.nextFieldName()).thenReturn(key1);

        JsonToken valueToken1 = JsonToken.VALUE_STRING;
        when(jsonParser.nextToken()).thenReturn(valueToken1);
        when(jsonParser.getValueAsString()).thenReturn("value1");

        JsonToken keyToken2 = JsonToken.FIELD_NAME;
        when(jsonParser.nextToken()).thenReturn(keyToken2);
        when(jsonParser.nextFieldName()).thenReturn("key2");

        JsonToken valueToken2 = JsonToken.VALUE_NULL;
        when(jsonParser.nextToken()).thenReturn(valueToken2);

        JsonToken endObjectToken = JsonToken.END_OBJECT;
        when(jsonParser.nextToken()).thenReturn(endObjectToken);

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, new AnnotatedMethod());
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        assertTrue(instance instanceof Map);
        assertEquals(2, ((Map) instance).size());
        assertEquals("value1", ((Map) instance).get(key1));
        assertNull(((Map) instance).get("key2"));
    }
}