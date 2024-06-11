package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class SettableAnyPropertyTest extends TestBase {

    @Test
    public void testConstructor() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(keyDeserializer, settableAnyProperty.getKeyDeserializer());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());
    }

    @Test
    public void testWithValueDeserializer() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newValueDeserializer);

        assertSame(property, newSettableAnyProperty.getProperty());
        assertSame(setter, newSettableAnyProperty.getSetter());
        assertSame(type, newSettableAnyProperty.getType());
        assertSame(keyDeserializer, newSettableAnyProperty.getKeyDeserializer());
        assertSame(newValueDeserializer, newSettableAnyProperty.getValueDeserializer());
        assertSame(valueTypeDeserializer, newSettableAnyProperty.getValueTypeDeserializer());
        assertFalse(newSettableAnyProperty.isSetterIsField());
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        settableAnyProperty.fixAccess(mock(DeserializationConfig.class));

        verify(setter).fixAccess(true);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String propName = "propName";
        Object instance = new Object();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapType(String.class, Object.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        when(keyDeserializer.deserializeKey(eq(propName), any(DeserializationContext.class)))
                .thenReturn("key");
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        Object key = "key";
        settableAnyProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) setter).getValue(instance);
        assertEquals("value", map.get(key));
    }

    @Test
    public void testDeserialize() throws IOException {
        String propName = "propName";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapType(String.class, Object.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        Object value = "value";
        when(valueDeserializer.deserialize(eq(jsonParser), eq(deserializationContext)))
                .thenReturn(value);

        Object deserializedValue = settableAnyProperty.deserialize(jsonParser, deserializationContext);
        assertEquals(value, deserializedValue);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object propName = "propName";
        Object value = "value";
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);

        when(((AnnotatedField) setter).getValue(instance)).thenReturn(new HashMap<>());

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);
        settableAnyProperty.set(instance, propName, value);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) setter).getValue(instance);
        assertEquals(value, map.get(propName));
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, null, null, null, null);

        IllegalArgumentException e = new IllegalArgumentException("Test error message");
        settableAnyProperty._throwAsIOE(e, "propName", "value");

        String expectedMessage = "Problem deserializing \"any\" property 'propName' of class [any property on class "
                + getClass().getName() + "] (expected type: null; actual type: java.lang.IllegalArgumentException)";
        assertEquals(expectedMessage, e.getMessage());
    }
}