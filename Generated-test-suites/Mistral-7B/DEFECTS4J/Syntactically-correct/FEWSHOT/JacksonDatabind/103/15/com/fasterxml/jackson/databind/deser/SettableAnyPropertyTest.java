package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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

/**
 * Test cases for {@link SettableAnyProperty}.
 */
public class SettableAnyPropertyTest extends TestBase {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty
                .withValueDeserializer(valueDeserializer);
        assertEquals(property, settableAnyPropertyWithValueDeserializer.getProperty());
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer.getValueDeserializer());
        assertEquals(type, settableAnyPropertyWithValueDeserializer.getType());

        SettableAnyProperty settableAnyPropertyWithTypeDeserializer = settableAnyProperty
                .withValueTypeDeserializer(valueTypeDeserializer);
        assertEquals(property, settableAnyPropertyWithTypeDeserializer.getProperty());
        assertEquals(valueDeserializer, settableAnyPropertyWithTypeDeserializer.getValueDeserializer());
        assertEquals(type, settableAnyPropertyWithTypeDeserializer.getType());
        assertEquals(valueTypeDeserializer, settableAnyPropertyWithTypeDeserializer.getValueTypeDeserializer());

        SettableAnyProperty settableAnyPropertyWithKeyDeserializer = settableAnyProperty
                .withKeyDeserializer(keyDeserializer);
        assertEquals(property, settableAnyPropertyWithKeyDeserializer.getProperty());
        assertEquals(valueDeserializer, settableAnyPropertyWithKeyDeserializer.getValueDeserializer());
        assertEquals(type, settableAnyPropertyWithKeyDeserializer.getType());
        assertEquals(keyDeserializer, settableAnyPropertyWithKeyDeserializer.getKeyDeserializer());

        SettableAnyProperty settableAnyPropertyWithSetterIsField = new SettableAnyProperty(property, setter, type,
                keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyPropertyWithSetterIsField._setterIsField = true;
        assertEquals(property, settableAnyPropertyWithSetterIsField.getProperty());
        assertTrue(settableAnyPropertyWithSetterIsField._setterIsField);
        assertFalse(settableAnyProperty._setterIsField);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type,
                keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propName";
        Object value = new Object();

        JsonParser p = createParser("{\"propName\":{\"value\":\"test\"}}");
        DeserializationContext ctxt = setupContext();

        settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);

        assertEquals(value, instance.getClass().getDeclaredField("value").get(instance));
    }

    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type,
                keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object value = new Object();
        JsonParser p = createParser("{\"value\":\"test\"}");
        DeserializationContext ctxt = setupContext();

        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(value);

        assertEquals(value, settableAnyProperty.deserialize(p, ctxt));
    }

    @Test
    public void testSet() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object instance = new Object();
        String propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        when(setter.getValue(instance)).thenReturn(null);

        settableAnyProperty.set(instance, propName, value);

        assertEquals(value, instance.getClass().getDeclaredField("value").get(instance));
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowAsIOE() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object instance = new Object();
        Object propName = "propName";
        Object value = new Object();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        when(setter.getValue(instance)).thenThrow(new IllegalArgumentException());
        settableAnyProperty.set(instance, propName, value);

        fail("Should throw JsonMappingException");
    }
}