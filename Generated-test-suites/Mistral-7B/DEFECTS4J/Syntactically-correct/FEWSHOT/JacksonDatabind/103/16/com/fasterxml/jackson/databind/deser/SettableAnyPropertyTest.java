package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyCreation() {
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
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertTrue(settableAnyProperty.isSetterField());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertTrue(settableAnyProperty.hasValueDeserializer());

        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newValueDeserializer);
        assertNotSame(valueDeserializer, newSettableAnyProperty.getValueDeserializer());
        assertEquals(newValueDeserializer, newSettableAnyProperty.getValueDeserializer());
    }

    @Test
    public void testFixAccess() throws IOException {
        AnnotatedMember setter = mock(AnnotatedMember.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);
        settableAnyProperty.fixAccess(config);
        verify(setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getDeserializationContext();

        AnnotatedMember setter = mock(AnnotatedMember.class);
        Object instance = new Object();
        String propName = "propName";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, null, null, null, null);

        JsonParser p = objectMapper.getFactory().createParser("{\"propName\": \"value\"}");

        settableAnyProperty.deserializeAndSet(p, context, instance, propName);

        verify(setter, times(1)).callOnWith(instance, propName, "value");
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getDeserializationContext();

        JsonParser p = objectMapper.getFactory().createParser("\"value\"");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        Object result = settableAnyProperty.deserialize(p, context);

        assertEquals("value", result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getDeserializationContext();
        Object instance = new Object();
        String propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        settableAnyProperty.set(instance, propName, value);

        if (instance instanceof Map) {
            Map<?,?> map = (Map<?,?>) instance;
            assertEquals(value, map.get(propName));
        } else {
            fail("Expected instance to be of type Map but was " + instance.getClass().getName());
        }
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext context = objectMapper.getDeserializationContext();
        Throwable e = new IOException("Error message");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        settableAnyProperty._throwAsIOE(e, "propName", new Object());

        String actType = ClassUtil.classNameOf(new Object());
        String expectedType = "java.lang.Object";
        assertEquals(expectedType, actType);

        String msg = settableAnyProperty.getMessage();
        String expectedMsg = "Problem deserializing \"any\" property 'propName' of class [any property on class X] (expected type: " +
                expectedType + "; actual type: " + actType + ")";
        assertEquals(expectedMsg, msg);
    }
}