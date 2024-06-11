package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class SettableAnyPropertyTest extends TestBase {

    @Test
    public void testSettableAnyProperty() throws IOException {
        BeanProperty property = new BeanProperty("anyProperty", String.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        when(setter.getDeclaringClass()).thenReturn(String.class);
        JavaType type = mapType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        assertEquals("any property on class java.lang.String", settableAnyProperty.toString());
        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());

        // test deserializeAndSet method
        Object instance = new Object();
        String propName = "propName";
        String value = "value";
        JsonParser p = createParser(mapper(), "[\"propName\", \"value\"]");
        settableAnyProperty.deserializeAndSet(p, context(), instance, propName);
        assertEquals("value", ((Map<?,?>)instance).get(propName));

        // test deserialize method
        JsonToken token = JsonToken.VALUE_STRING;
        Object deserializedValue = settableAnyProperty.deserialize(p, context());
        assertEquals("value", deserializedValue);

        // test set method
        settableAnyProperty.set(instance, propName, value);
        assertEquals("value", ((Map<?,?>)instance).get(propName));
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
        BeanProperty property = new BeanProperty("anyProperty", String.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        when(setter.getDeclaringClass()).thenReturn(String.class);
        JavaType type = mapType(String.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, typeDeserializer);

        assertEquals("any property on class java.lang.String", settableAnyProperty.toString());
        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(typeDeserializer, settableAnyProperty.getValueTypeDeserializer());

        // test deserializeAndSet method with valueDeserializer
        Object instance = new Object();
        String propName = "propName";
        String value = "value";
        String json = "\"propName\":\"value\"";
        JsonParser p = createParser(mapper(), json);
        settableAnyProperty.deserializeAndSet(p, context(), instance, propName);
        assertEquals("value", ((Map<?,?>)instance).get(propName));

        verify(valueDeserializer).deserialize(p, context());

        // test deserialize method with valueDeserializer
        Object deserializedValue = settableAnyProperty.deserialize(p, context());
        assertEquals("value", deserializedValue);

        verify(valueDeserializer).deserialize(p, context());

        // test set method with valueDeserializer
        settableAnyProperty.set(instance, propName, value);
        assertEquals("value", ((Map<?,?>)instance).get(propName));

        verify(valueDeserializer).deserialize(p, context());
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        BeanProperty property = new BeanProperty("anyProperty", String.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        when(setter.getDeclaringClass()).thenReturn(String.class);
        JavaType type = mapType(Map.class, String.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);

        assertEquals("any property on class java.util.Map<java.lang.String, java.lang.String>", settableAnyProperty.toString());
        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(keyDeserializer, settableAnyProperty.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(typeDeserializer, settableAnyProperty.getValueTypeDeserializer());

        // test deserializeAndSet method with keyDeserializer
        Object instance = new HashMap<>();
        String propName = "propName";
        String key = "key";
        String value = "value";
        String json = "{\"key\":\"propName\", \"value\":\"value\"}";
        JsonParser p = createParser(mapper(), json);
        settableAnyProperty.deserializeAndSet(p, context(), instance, propName);
        assertEquals(key, ((Map<?,?>)instance).get(propName));
        assertEquals("value", ((Map<?,?>)instance).get(key));

        verify(keyDeserializer).deserializeKey(eq("propName"), context());
        verify(valueDeserializer).deserialize(p, context());

        // test deserialize method with keyDeserializer
        Object deserializedKey = keyDeserializer.deserializeKey("propName", context());
        Object deserializedValue = settableAnyProperty.deserialize(p, context());
        assertEquals(key, deserializedKey);
        assertEquals("value", deserializedValue);

        verify(keyDeserializer).deserializeKey(eq("propName"), context());
        verify(valueDeserializer).deserialize(p, context());

        // test set method with keyDeserializer
        settableAnyProperty.set(instance, propName, value);
        assertEquals(key, ((Map<?,?>)instance).get(propName));
        assertEquals("value", ((Map<?,?>)instance).get(key));

        verify(keyDeserializer).deserializeKey(eq("propName"), context());
        verify(valueDeserializer).deserialize(p, context());
    }
}