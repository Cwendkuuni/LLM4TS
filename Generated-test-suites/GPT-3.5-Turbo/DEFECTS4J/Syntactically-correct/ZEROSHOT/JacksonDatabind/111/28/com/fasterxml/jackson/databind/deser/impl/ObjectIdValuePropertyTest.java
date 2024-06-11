package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty src = createSourceProperty();
        JsonDeserializer<?> deser = createDeserializer();
        NullValueProvider nva = createNullValueProvider();

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser, nva);

        assertEquals(src, property.getSource());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullProvider());
    }

    @Test
    public void testCopyConstructorWithNewName() {
        ObjectIdValueProperty src = createSourceProperty();
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty src = createSourceProperty();
        PropertyName newName = new PropertyName("newName");

        ObjectIdValueProperty property = src.withName(newName);

        assertNotNull(property);
        assertEquals(newName, property.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = createSourceProperty();
        JsonDeserializer<?> deser = createDeserializer();

        ObjectIdValueProperty result = property.withValueDeserializer(deser);

        assertNotNull(result);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty property = createSourceProperty();
        NullValueProvider nva = createNullValueProvider();

        ObjectIdValueProperty result = property.withNullProvider(nva);

        assertNotNull(result);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = createSourceProperty();

        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = createSourceProperty();

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdValueProperty property = createSourceProperty();
        JsonParser parser = createJsonParser();
        DeserializationContext context = createDeserializationContext();
        Object instance = new Object();

        try {
            property.deserializeAndSet(parser, context, instance);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdValueProperty property = createSourceProperty();
        JsonParser parser = createJsonParser();
        DeserializationContext context = createDeserializationContext();
        Object instance = new Object();

        try {
            Object result = property.deserializeSetAndReturn(parser, context, instance);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testSet() {
        ObjectIdValueProperty property = createSourceProperty();
        Object instance = new Object();
        Object value = new Object();

        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdValueProperty property = createSourceProperty();
        Object instance = new Object();
        Object value = new Object();

        try {
            Object result = property.setAndReturn(instance, value);
            assertNotNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    private ObjectIdValueProperty createSourceProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        return new ObjectIdValueProperty(objectIdReader, metadata);
    }

    private JsonDeserializer<?> createDeserializer() {
        return new JsonDeserializer<>();
    }

    private NullValueProvider createNullValueProvider() {
        return new NullValueProvider();
    }

    private JsonParser createJsonParser() {
        return new JsonParser();
    }

    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext();
    }
}