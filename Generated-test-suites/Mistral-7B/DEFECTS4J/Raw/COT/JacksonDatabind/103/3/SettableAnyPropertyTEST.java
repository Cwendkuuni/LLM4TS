package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSettableAnyPropertyConstructor() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(keyDeserializer, settableAnyProperty.getKeyDeserializer());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());

        SettableAnyProperty settableAnyProperty2 = new SettableAnyProperty(property, setter, type, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty2);
        assertEquals(property, settableAnyProperty2.getProperty());
        assertEquals(setter, settableAnyProperty2.getSetter());
        assertEquals(type, settableAnyProperty2.getType());
        assertNull(settableAnyProperty2.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty2.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty2.getValueTypeDeserializer());
        assertTrue(settableAnyProperty2.isSetterIsField());
    }

    @org.junit.Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, valueDeserializer, valueTypeDeserializer);
        assertFalse(settableAnyProperty.hasValueDeserializer());

        SettableAnyProperty settableAnyProperty2 = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertTrue(settableAnyProperty2.hasValueDeserializer());
        assertSame(valueDeserializer, settableAnyProperty2.getValueDeserializer());
    }

    @org.junit.Test
    public void testSettableAnyPropertyFixAccess() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.fixAccess(createContext());

        verify(setter).fixAccess(true);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSettableAnyPropertyNullSetter() {
        BeanProperty property = mock(BeanProperty.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, null, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();
        String propName = "name";

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getDeclaringClass()).thenReturn(Object.class);
        when(setter.getName()).thenReturn(propName);

        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("John");

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);

        verify(instance).getClass();
        verify(setter).callOnWith(instance, propName, "John");
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSetUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": null}");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();
        String propName = "name";

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getDeclaringClass()).thenReturn(Object.class);
        when(setter.getName()).thenReturn(propName);
        when(valueDeserializer.getObjectIdReader()).thenReturn(null);

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);

        verify(parser).nextToken();
        verify(parser).nextToken();
    }

    @org.junit.Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = createContext();

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("John");

        Object result = settableAnyProperty.deserialize(parser, ctxt);

        assertEquals("John", result);
    }

    @org.junit.Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();
        String propName = "name";
        Object value = "John";

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getValue(instance)).thenReturn((Map<Object, Object>) new HashMap<>());
        when(setter.getName()).thenReturn(propName);

        settableAnyProperty.set(instance, propName, value);

        Map<Object, Object> map = (Map<Object, Object>) settableAnyProperty.getProperty().getBean();
        assertEquals(value, map.get(propName));
    }

    @org.junit.Test
    public void testSetNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();
        String propName = "name";

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getValue(instance)).thenReturn((Map<Object, Object>) new HashMap<>());
        when(setter.getName()).thenReturn(propName);

        settableAnyProperty.set(instance, propName, null);

        Map<Object, Object> map = (Map<Object, Object>) settableAnyProperty.getProperty().getBean();
        assertNull(map.get(propName));
    }

    @org.junit.Test(expected = IOException.class)
    public void testSetIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object instance = new Object();
        String propName = "name";
        Object value = "John";

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getValue(instance)).thenThrow(new IOException("Error setting value"));
        when(setter.getName()).thenReturn(propName);

        settableAnyProperty.set(instance, propName, value);
    }

    @org.junit.Test
    public void testThrowAsIOE() {
        IllegalArgumentException e = new IllegalArgumentException("Invalid value");
        Object propName = "name";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        settableAnyProperty._throwAsIOE(e, propName, value);

        String className = settableAnyProperty.getClass().getName();
        String expectedMessage = "Problem deserializing \"any\" property 'name' of class [" + className + "] (expected type: null; actual type: class java.lang.Object)";
        assertEquals(expectedMessage, e.getMessage());
    }

    @org.junit.Test
    public void testToString() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals("[any property on class com.example.junit.SettableAnyPropertyTest]", settableAnyProperty.toString());
    }
}