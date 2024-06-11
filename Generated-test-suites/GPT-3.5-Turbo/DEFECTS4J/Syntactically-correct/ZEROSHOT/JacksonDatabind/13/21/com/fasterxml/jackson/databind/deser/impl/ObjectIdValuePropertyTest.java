package com.fasterxml.jackson.databind.deser.impl.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor1() {
        // Setup
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();

        // Test
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        // Verify
        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testConstructor2() {
        // Setup
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        // Test
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deser);

        // Verify
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(deser, property._valueDeserializer);
    }

    @Test
    public void testConstructor3() {
        // Setup
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName("newName");

        // Test
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        // Verify
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property._propertyName);
    }

    @Test
    public void testConstructor4() {
        // Setup
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        String newName = "newName";

        // Test
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);

        // Verify
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property._propertyName.getSimpleName());
    }

    @Test
    public void testWithName() {
        // Setup
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        PropertyName newName = new PropertyName("newName");

        // Test
        ObjectIdValueProperty property = src.withName(newName);

        // Verify
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(newName, property._propertyName);
    }

    @Test
    public void testWithValueDeserializer() {
        // Setup
        ObjectIdValueProperty src = new ObjectIdValueProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();

        // Test
        ObjectIdValueProperty property = src.withValueDeserializer(deser);

        // Verify
        assertNotNull(property);
        assertEquals(src._objectIdReader, property._objectIdReader);
        assertEquals(deser, property._valueDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        // Setup
        ObjectIdValueProperty property = new ObjectIdValueProperty();

        // Test
        Annotation annotation = property.getAnnotation(Annotation.class);

        // Verify
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        // Setup
        ObjectIdValueProperty property = new ObjectIdValueProperty();

        // Test
        AnnotatedMember member = property.getMember();

        // Verify
        assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() {
        // Setup
        ObjectIdValueProperty property = new ObjectIdValueProperty();

        // Test
        // Add test here...

        // Verify
        // Add verification here...
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Setup
        ObjectIdValueProperty property = new ObjectIdValueProperty();

        // Test
        // Add test here...

        // Verify
        // Add verification here...
    }

    @Test
    public void testSet() {
        // Setup
        ObjectIdValueProperty property = new ObjectIdValueProperty();

        // Test
        // Add test here...

        // Verify
        // Add verification here...
    }

    @Test
    public void testSetAndReturn() {
        // Setup
        ObjectIdValueProperty property = new ObjectIdValueProperty();

        // Test
        // Add test here...

        // Verify
        // Add verification here...
    }
}