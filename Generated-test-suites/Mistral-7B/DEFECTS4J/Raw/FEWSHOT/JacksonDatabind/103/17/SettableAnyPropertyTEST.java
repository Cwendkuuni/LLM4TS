package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = new BeanProperty("testProperty", String.class, null, null, null, null);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertNotNull(settableAnyProperty.getProperty());
        assertNotNull(settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertTrue(settableAnyProperty.isSetterField());

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);
        assertNotSame(settableAnyProperty, settableAnyPropertyWithValueDeserializer);
        assertEquals(settableAnyProperty.getProperty(), settableAnyPropertyWithValueDeserializer.getProperty());
        assertEquals(settableAnyProperty.getSetter(), settableAnyPropertyWithValueDeserializer.getSetter());
        assertEquals(settableAnyProperty.getType(), settableAnyPropertyWithValueDeserializer.getType());
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer.getValueDeserializer());
        assertEquals(settableAnyProperty.getValueTypeDeserializer(), settableAnyPropertyWithValueDeserializer.getValueTypeDeserializer());
        assertEquals(settableAnyProperty.getKeyDeserializer(), settableAnyPropertyWithValueDeserializer.getKeyDeserializer());
        assertFalse(settableAnyPropertyWithValueDeserializer.isSetterField());

        SettableAnyProperty settableAnyPropertyWithKeyDeserializer = new SettableAnyProperty(property, setter, type, keyDeserializer, null, null);
        assertNotSame(settableAnyProperty, settableAnyPropertyWithKeyDeserializer);
        assertEquals(settableAnyProperty.getProperty(), settableAnyPropertyWithKeyDeserializer.getProperty());
        assertEquals(settableAnyProperty.getSetter(), settableAnyPropertyWithKeyDeserializer.getSetter());
        assertEquals(type, settableAnyPropertyWithKeyDeserializer.getType());
        assertNull(settableAnyPropertyWithKeyDeserializer.getValueDeserializer());
        assertNotNull(settableAnyPropertyWithKeyDeserializer.getKeyDeserializer());
        assertTrue(settableAnyPropertyWithKeyDeserializer.isSetterField());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        settableAnyProperty.fixAccess(config);
        assertEquals(Access.PUBLIC, settableAnyProperty.getSetter().getMemberAccess());
        assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));

        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.fixAccess(config);
        assertEquals(Access.PRIVATE, settableAnyProperty.getSetter().getMemberAccess());
        assertFalse(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String propName = "testProperty";
        Object instance = Mockito.mock(Object.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(setter.getDeclaringClass()).thenReturn(Object.class);
        when(p.getText()).thenReturn("testValue");

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        verify(instance).invoke(setter, propName, "testValue");
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        String value = "testValue";
        JsonToken token = JsonToken.VALUE_STRING;

        when(p.getValueAsString()).thenReturn(value);
        when(p.getCurrentToken()).thenReturn(token);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        Object result = settableAnyProperty.deserialize(p, ctxt);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = Mockito.mock(Object.class);
        Object propName = "testProperty";
        Object value = "testValue";

        AnnotatedField field = Mockito.mock(AnnotatedField.class);
        when(field.getValue(instance)).thenReturn(null);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, field, null, null, null, null);

        settableAnyProperty.set(instance, propName, value);
        verify(instance).setValue(propName, value);

        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);
        try {
            settableAnyProperty.set(instance, null, value);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        Exception e = new IOException("Test error message");
        Object propName = "testProperty";
        Object value = "testValue";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        settableAnyProperty._throwAsIOE(e, propName, value);

        String expectedMessage = "Problem deserializing \"any\" property 'testProperty' of class [class com.fasterxml.jackson.databind.deser.SettableAnyProperty] (expected type: class java.lang.String; actual type: class java.io.IOException)";
        assertEquals(expectedMessage, e.getMessage());
    }
}