package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest extends BaseTest {

    @SuppressWarnings("unchecked")
    public static class TestClass {
        private Map<String, Object> _map;

        public Map<String, Object> getMap() { return _map; }
    }

    @org.junit.Test
    public void testDefaultConstructor() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null, null);
        assertNull(property._property);
        assertNull(property._setter);
        assertNull(property._type);
        assertNull(property._valueDeserializer);
        assertNull(property._valueTypeDeserializer);
        assertNull(property._keyDeserializer);
        assertFalse(property._setterIsField);
    }

    @org.junit.Test
    public void testAnnotationConstructor() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertEquals(property, settableAnyProperty._property);
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty._type);
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertTrue(settableAnyProperty._setterIsField);
    }

    @org.junit.Test
    public void testWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty withValueDeserializer = settableAnyProperty.withValueDeserializer(mock(JsonDeserializer.class));
        assertNotSame(valueDeserializer, withValueDeserializer._valueDeserializer);
        assertEquals(property, withValueDeserializer._property);
        assertEquals(setter, withValueDeserializer._setter);
        assertEquals(type, withValueDeserializer._type);
        assertEquals(keyDeserializer, withValueDeserializer._keyDeserializer);
        assertEquals(valueDeserializer, withValueDeserializer._valueDeserializer);
        assertEquals(valueTypeDeserializer, withValueDeserializer._valueTypeDeserializer);
        assertTrue(withValueDeserializer._setterIsField);
    }

    @org.junit.Test
    public void testFixAccess() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty.fixAccess(createContext());
        verify(setter).fixAccess(true);
    }

    @org.junit.Test
    public void testReadResolve() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        assertEquals(settableAnyProperty, settableAnyProperty.readResolve());
    }

    @org.junit.Test
    public void testPropertyGetter() {
        BeanProperty property = mock(BeanProperty.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, null, null, null, null, null);
        assertEquals(property, settableAnyProperty.getProperty());
    }

    @org.junit.Test
    public void testHasValueDeserializer() {
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        assertFalse(settableAnyProperty.hasValueDeserializer());

        SettableAnyProperty settableAnyPropertyWithDeserializer = new SettableAnyProperty(null, null, null, null, mock(JsonDeserializer.class), null);
        assertTrue(settableAnyPropertyWithDeserializer.hasValueDeserializer());
    }

    @org.junit.Test
    public void testTypeGetter() {
        JavaType javaType = mock(JavaType.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, javaType, null, null, null);
        assertEquals(javaType, settableAnyProperty.getType());
    }

    @org.junit.Test
    public void testDeserializeAndSetNullValue() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.deserializeAndSet(createParser(), createContext(), instance, propName);
        assertNull(instance.getMap());
    }

    @org.junit.Test
    public void testDeserializeAndSetValueWithTypeDeserializer() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        JavaType javaType = createType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, javaType, null, null, mock(TypeDeserializer.class));
        JsonParser parser = createParser();
        parser.setValue(new ObjectMapper().valueToTree("testValue"));
        settableAnyProperty.deserializeAndSet(parser, createContext(), instance, propName);
        Map<String, Object> map = instance.getMap();
        assertEquals("testValue", map.get(propName));
    }

    @org.junit.Test
    public void testDeserializeAndSetValue() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        JsonParser parser = createParser();
        parser.setValue("testValue");
        settableAnyProperty.deserializeAndSet(parser, createContext(), instance, propName);
        Map<String, Object> map = instance.getMap();
        assertEquals("testValue", map.get(propName));
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetValueWithInvalidType() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, createType(int.class), null, null, null);
        JsonParser parser = createParser();
        parser.setValue("testValue");
        settableAnyProperty.deserializeAndSet(parser, createContext(), instance, propName);
        fail();
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetValueWithUnresolvedForwardReference() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        JsonParser parser = createParser();
        parser.setValue("$ref:testRef");
        settableAnyProperty.deserializeAndSet(parser, createContext(), instance, propName);
        fail();
    }

    @org.junit.Test
    public void testSet() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        Object value = "testValue";
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);
        Map<String, Object> map = instance.getMap();
        assertEquals(value, map.get(propName));
    }

    @org.junit.Test(expected = JsonProcessingException.class)
    public void testSetWithException() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        Object value = new RuntimeException("Test Exception");
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);
        fail();
    }

    @org.junit.Test
    public void testThrowAsIOE() throws IOException {
        Object instance = new TestClass();
        String propName = "testProperty";
        Object value = new RuntimeException("Test Exception");
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        try {
            settableAnyProperty.set(instance, propName, value);
            fail();
        } catch (JsonMappingException e) {
            String expectedMessage = "Problem deserializing \"any\" property 'testProperty' of class [any property on class com.example.TestClass] "
                    + "(expected type: java.util.Map<java.lang.String,java.lang.Object>; actual type: java.lang.RuntimeException)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private DeserializationContext createContext() {
        return new TestContext();
    }

    private JsonParser createParser() throws IOException {
        return new TestParser();
    }

    private ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    private JavaType createType(Class<?> clazz) {
        return createObjectMapper().getTypeFactory().constructType(clazz);
    }
}