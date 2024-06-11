package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;

public class SettableAnyPropertyTest {

    @Test
    public void testNewInstance() {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructUninitializedType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty propertyUnderTest = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertEquals(property, propertyUnderTest.getProperty());
        assertEquals(setter, propertyUnderTest._setter);
        assertEquals(type, propertyUnderTest._type);
        assertEquals(keyDeserializer, propertyUnderTest._keyDeserializer);
        assertEquals(valueDeserializer, propertyUnderTest._valueDeserializer);
        assertEquals(valueTypeDeserializer, propertyUnderTest._valueTypeDeserializer);
        assertFalse(propertyUnderTest._setterIsField);
    }

    @Test
    public void testNewInstanceWithSetterIsField() {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedField setter = mock(AnnotatedField.class);
        JavaType type = mapper.getTypeFactory().constructUninitializedType(Map.class);

        SettableAnyProperty propertyUnderTest = new SettableAnyProperty(property, setter, type, null, null, null);
        assertEquals(property, propertyUnderTest.getProperty());
        assertEquals(setter, propertyUnderTest._setter);
        assertEquals(type, propertyUnderTest._type);
        assertNull(propertyUnderTest._keyDeserializer);
        assertNull(propertyUnderTest._valueDeserializer);
        assertNull(propertyUnderTest._valueTypeDeserializer);
        assertTrue(propertyUnderTest._setterIsField);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructUninitializedType(String.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        Object instance = new Object();
        String propName = "propName";
        Object deserializedValue = "value";

        when(setter.callOnWith(instance, eq(propName), eq(deserializedValue)))
                .thenReturn(instance);

        SettableAnyProperty propertyUnderTest = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        propertyUnderTest.deserializeAndSet(parser, ctxt, instance, propName);

        verify(setter).callOnWith(instance, eq(propName), eq(deserializedValue));
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty propertyUnderTest = new SettableAnyProperty(null, null, null, null, valueDeserializer, valueTypeDeserializer);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        String jsonString = "{\"foo\": \"bar\"}";
        when(parser.getText()).thenReturn(jsonString);

        Object deserializedValue = propertyUnderTest.deserialize(parser, ctxt);

        assertEquals("bar", deserializedValue);

        verify(valueDeserializer, times(1)).deserialize(parser, ctxt);
    }
}