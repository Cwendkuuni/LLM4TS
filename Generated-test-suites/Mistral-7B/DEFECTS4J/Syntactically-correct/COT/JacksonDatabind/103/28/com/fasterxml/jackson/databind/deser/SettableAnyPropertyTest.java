package com.example.settableanyproperty;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSettableAnyPropertyConstructor() {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(Object.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
        assertFalse(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);

        assertEquals(property, settableAnyPropertyWithValueDeserializer.getProperty());
        assertEquals(setter, settableAnyPropertyWithValueDeserializer._setter);
        assertEquals(type, settableAnyPropertyWithValueDeserializer.getType());
        assertNotNull(settableAnyPropertyWithValueDeserializer._valueDeserializer);
        assertNull(settableAnyPropertyWithValueDeserializer._valueTypeDeserializer);
        assertNull(settableAnyPropertyWithValueDeserializer._keyDeserializer);
        assertFalse(settableAnyPropertyWithValueDeserializer._setterIsField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettableAnyPropertyNullSetter() {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, null, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();

        JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"name\":\"John\",\"age\":30}]");
        String propName = "name";
        Object value = "Jane";

        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), pojo, propName);

        assertEquals("Jane", pojo.getClass().getDeclaredField("name").get(pojo));
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testSettableAnyPropertyUnresolvedForwardReference() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();

        JsonParser jsonParser = objectMapper.getFactory().createParser("[{\"name\":null}]");
        String propName = "name";

        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), pojo, propName);
    }

    @Test
    public void testSettableAnyPropertyDeserialize() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"hello\"]");

        Object value = settableAnyProperty.deserialize(jsonParser, objectMapper.getDeserializationContext());

        assertEquals("hello", value);
    }

    @Test
    public void testSettableAnyPropertySet() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();
        Object propName = "name";
        Object value = "Jane";

        settableAnyProperty.set(pojo, propName, value);

        assertEquals("Jane", pojo.getClass().getDeclaredField("name").get(pojo));
    }

    @Test(expected = IOException.class)
    public void testSettableAnyPropertyThrowAsIOE() throws JsonProcessingException {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedField(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JavaType type = new TypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<Object>(String.class);
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();
        Object propName = "name";
        Object value = new Object();

        settableAnyProperty._throwAsIOE(new IllegalArgumentException("Test error message"), propName, value);
    }
}