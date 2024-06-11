package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
    }

    @After
    public void tearDown() {
        deserializer = null;
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = new BeanProperty();
        TypeDeserializer result = deserializer.forProperty(prop);
        assertNotEquals(deserializer, result);
    }

    @Test
    public void testGetTypeInclusion() {
        As expected = As.PROPERTY;
        As result = deserializer.getTypeInclusion();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTypedFromObject() {
        // TODO: Implement test case
    }

    @Test
    public void testDeserializeTypedForId() {
        // TODO: Implement test case
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        // TODO: Implement test case
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // TODO: Implement test case
    }
}