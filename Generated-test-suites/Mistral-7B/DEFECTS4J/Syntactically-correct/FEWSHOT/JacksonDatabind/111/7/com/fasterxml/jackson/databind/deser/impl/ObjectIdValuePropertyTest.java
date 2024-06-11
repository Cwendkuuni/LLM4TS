package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValuePropertyCreation() {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals(propertyName, objectIdValueProperty.getName());
        assertEquals(Object.class, objectIdValueProperty.getType());
        assertNull(objectIdValueProperty.getValueDeserializer());
        assertNull(objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializer() {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer);
        assertEquals(propertyName, objectIdValueProperty.getName());
        assertEquals(Object.class, objectIdValueProperty.getType());
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertNotNull(objectIdValueProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyWithName() {
        String propertyName = "newId";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newId"));
        assertEquals(propertyName, newObjectIdValueProperty.getName());
        assertEquals(objectIdValueProperty.getType(), newObjectIdValueProperty.getType());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newObjectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newObjectIdValueProperty._objectIdReader);
    }

    @Test
    public void testObjectIdValuePropertyWithNullProvider() {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), null);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, nullValueProvider);
        assertEquals(propertyName, newObjectIdValueProperty.getName());
        assertEquals(objectIdValueProperty.getType(), newObjectIdValueProperty.getType());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newObjectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newObjectIdValueProperty._objectIdReader);
        assertEquals(nullValueProvider, newObjectIdValueProperty._nullProvider);
    }

    @Test
    public void testObjectIdValuePropertyWithValueDeserializerAndNullProvider() {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer, nullValueProvider);
        assertEquals(propertyName, objectIdValueProperty.getName());
        assertEquals(Object.class, objectIdValueProperty.getType());
        assertEquals(valueDeserializer, objectIdValueProperty.getValueDeserializer());
        assertNotNull(objectIdValueProperty._objectIdReader);
        assertEquals(nullValueProvider, objectIdValueProperty._nullProvider);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer, null);

        ObjectNode testObject = ObjectMapper.getDefaultInstance().createObjectNode();
        testObject.put("id", 1L);

        JsonParser jsonParser = ObjectMapper.getDefaultInstance().treeAsTokens(testObject);

        Object instance = new Object();

        objectIdValueProperty.deserializeAndSet(jsonParser, new DeserializationContext(), instance);

        assertEquals(1L, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer, null);

        ObjectNode testObject = ObjectMapper.getDefaultInstance().createObjectNode();
        testObject.put("id", 1L);

        JsonParser jsonParser = ObjectMapper.getDefaultInstance().treeAsTokens(testObject);

        Object instance = new Object();

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new DeserializationContext(), instance);

        assertEquals(1L, instance.getClass().getDeclaredField("id").get(instance));
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object value = new Object();

        objectIdValueProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String propertyName = "id";
        ObjectIdReader objectIdReader = new StandardObjectIdReader();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), valueDeserializer, null);
        SettableBeanProperty idProperty = new SimpleValueProperty("id", String.class, null, null);
        ObjectIdReader idReader = new StandardObjectIdReader();
        ObjectIdValueProperty idObjectIdValueProperty = new ObjectIdValueProperty(idReader, new PropertyMetadata(), null, idProperty);

        Object instance = new Object();
        Object value = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        assertEquals(value, result);
        assertEquals(value, instance.getClass().getDeclaredField("id").get(instance));
    }
}