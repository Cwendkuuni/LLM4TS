package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettableAnyPropertyTest {

    private Object testInstance;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private KeyDeserializer keyDeserializer;

    @Before
    public void setup() throws Exception {
        testInstance = new Object() {{}};

        valueDeserializer = new FromStringDeserializer();
        valueDeserializer.setContentAsString(true);
        valueDeserializer = new ToStringSerializer(String.class).withValueDeserializer(valueDeserializer);

        valueTypeDeserializer = mock(TypeDeserializer.class);

        keyDeserializer = mock(KeyDeserializer.class);
    }

    @Test
    public void testSettableAnyPropertyConstruction() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty._type);
        assertNotNull(settableAnyProperty._valueDeserializer);
        assertNotNull(settableAnyProperty._valueTypeDeserializer);
        assertNotNull(settableAnyProperty._keyDeserializer);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(mock(JsonDeserializer.class));

        assertEquals(property, newSettableAnyProperty.getProperty());
        assertEquals(setter, newSettableAnyProperty._setter);
        assertEquals(type, newSettableAnyProperty._type);
        assertNotNull(newSettableAnyProperty._valueDeserializer);
        assertSame(valueTypeDeserializer, newSettableAnyProperty._valueTypeDeserializer);
        assertNotNull(newSettableAnyProperty._keyDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        when(setter.getDeclaringClass()).thenReturn(Map.class);
        when(setter.getParameterTypes()).thenReturn(new Class[0]);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object propName = "test";
        JsonParser parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn("testValue");

        Object instance = testInstance;
        DeserializationContext ctxt = mock(DeserializationContext.class);

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);

        Map map = (Map) instance;
        assertEquals("testValue", map.get("test"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithNullValueDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        when(setter.getDeclaringClass()).thenReturn(Map.class);
        when(setter.getParameterTypes()).thenReturn(new Class[0]);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, valueTypeDeserializer);

        Object propName = "test";
        JsonParser parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn("testValue");

        Object instance = testInstance;
        DeserializationContext ctxt = mock(DeserializationContext.class);

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithInvalidTypeDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        when(setter.getDeclaringClass()).thenReturn(Map.class);
        when(setter.getParameterTypes()).thenReturn(new Class[0]);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, null);

        Object propName = "test";
        JsonParser parser = mock(JsonParser.class);
        when(parser.getText()).thenReturn("testValue");

        Object instance = testInstance;
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getTypeDeserializer()).thenReturn(mock(TypeDeserializer.class));

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getValueAsString()).thenReturn("testValue");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object value = settableAnyProperty.deserialize(parser, mock(DeserializationContext.class));

        assertEquals("testValue", value);
    }

    @Test
    public void testSetterIsField() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = testInstance;
        Object propName = "test";
        Object value = "testValue";

        settableAnyProperty.set(instance, propName, value);

        Map map = (Map) instance;
        assertEquals("testValue", map.get("test"));
    }

    @Test(expected = IOException.class)
    public void testSetterIsNotField() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMethod.class);
        JavaType type = mock(JavaType.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = testInstance;
        Object propName = "test";
        Object value = "testValue";

        settableAnyProperty.set(instance, propName, value);
    }
}