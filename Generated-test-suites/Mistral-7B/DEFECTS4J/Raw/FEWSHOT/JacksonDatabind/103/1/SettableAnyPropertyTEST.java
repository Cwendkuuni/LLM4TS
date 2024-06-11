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
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstruction() {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer, idGenerator);
        SettableAnyProperty settableAnyPropertyWithoutKeyDeserializer = new SettableAnyProperty(property, setter, type, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty._type);
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertEquals(false, settableAnyProperty._setterIsField);

        assertEquals(property, settableAnyPropertyWithoutKeyDeserializer.getProperty());
        assertEquals(setter, settableAnyPropertyWithoutKeyDeserializer._setter);
        assertEquals(type, settableAnyPropertyWithoutKeyDeserializer._type);
        assertNull(settableAnyPropertyWithoutKeyDeserializer._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyPropertyWithoutKeyDeserializer._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyPropertyWithoutKeyDeserializer._valueTypeDeserializer);
        assertEquals(true, settableAnyPropertyWithoutKeyDeserializer._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);

        assertEquals(property, settableAnyPropertyWithValueDeserializer.getProperty());
        assertEquals(setter, settableAnyPropertyWithValueDeserializer._setter);
        assertEquals(type, settableAnyPropertyWithValueDeserializer._type);
        assertEquals(keyDeserializer, settableAnyPropertyWithValueDeserializer._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyPropertyWithValueDeserializer._valueTypeDeserializer);
        assertEquals(false, settableAnyPropertyWithValueDeserializer._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyFixAccess() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        settableAnyProperty.fixAccess(mock(DeserializationConfig.class));

        verify(setter).fixAccess(true);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        String propName = "propName";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(parser.getCurrentName()).thenReturn(propName);
        when(parser.getValueAsString()).thenReturn("value");

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(setter).callOnWith(instance, propName, argThat(isInstanceOf(Object.class)));
    }

    @Test
    public void testSettableAnyPropertyDeserialize() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("value");

        Object result = settableAnyProperty.deserialize(parser, context);

        assertEquals("value", result);
    }

    @Test
    public void testSettableAnyPropertySet() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        Object instance = mock(Object.class);
        Object propName = "propName";
        Object value = "value";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        when(setter.getValue(instance)).thenReturn(new HashMap<>());

        settableAnyProperty.set(instance, propName, value);

        Map<Object, Object> map = (Map<Object, Object>) setter.getValue(instance);

        assertEquals(value, map.get(propName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettableAnyPropertySetWithNullInstance() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        Object propName = "propName";
        Object value = "value";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        settableAnyProperty.set(null, propName, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyThrowAsIOE() throws IOException {
        ObjectIdGenerator idGenerator = mock(ObjectIdGenerator.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mock(JavaType.class);
        Object instance = mock(Object.class);
        Object propName = "propName";
        Object value = "value";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        when(setter.getValue(instance)).thenThrow(new IllegalArgumentException());

        settableAnyProperty._throwAsIOE(new IllegalArgumentException(), propName, value);
    }
}