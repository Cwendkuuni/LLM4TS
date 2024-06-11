package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(mock(JsonDeserializer.class));
        assertNotSame(valueDeserializer, settableAnyPropertyWithValueDeserializer._valueDeserializer);
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer._valueDeserializer);
    }

    @Test
    public void testFixAccess() throws JsonProcessingException {
        DeserializationConfig config = mock(DeserializationConfig.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.fixAccess(config);
        _setter.fixAccess(true);
        verify(setter).fixAccess(true);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object() {};
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "propName";
        Object value = new Object() {};
        Object key = "key";

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        Map<Object, Object> map = new HashMap<>();

        when(setter.getValue(instance)).thenReturn(map);
        when(setter.getName()).thenReturn("setterName");
        when(setter.getDeclaringClass()).thenReturn(SettableAnyProperty.class);
        when(settableAnyProperty.getSetter()).thenReturn(setter);
        when(settableAnyProperty.getType().getRawClass()).thenReturn(Object.class);
        when(settableAnyProperty._setterIsField).thenReturn(true);

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
        assertEquals(value, map.get(propName));
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        Object value = new Object() {};

        when(settableAnyProperty.deserialize(p, ctxt)).thenReturn(value);
        assertEquals(value, settableAnyProperty.deserialize(p, ctxt));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object() {};
        Object propName = "propName";
        Object value = new Object() {};

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        Map<Object, Object> map = new HashMap<>();

        when(settableAnyProperty.getType().getRawClass()).thenReturn(Map.class);
        when(setter.getValue(instance)).thenReturn(map);
        when(setter.getName()).thenReturn("setterName");
        when(settableAnyProperty.getSetter()).thenReturn(setter);
        when(settableAnyProperty._setterIsField).thenReturn(true);

        settableAnyProperty.set(instance, propName, value);
        assertEquals(value, map.get(propName));
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowAsIOE() throws IOException {
        Object instance = new Object() {};
        Object propName = "propName";
        Object value = new Object() {};
        Exception e = new IllegalArgumentException("Error message");

        SettableAnyProperty settableAnyProperty = mock(SettableAnyProperty.class);
        settableAnyProperty._throwAsIOE(e, propName, value);
    }
}