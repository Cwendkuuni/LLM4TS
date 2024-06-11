package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);

        assertNotNull(property);
        assertEquals(objectIdReader, property._objectIdReader);
    }

    @Test
    public void testDeserializationAndSet() {
        // implement test here
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // implement test here
    }

    @Test
    public void testSetAndReturn() {
        // implement test here
    }

}