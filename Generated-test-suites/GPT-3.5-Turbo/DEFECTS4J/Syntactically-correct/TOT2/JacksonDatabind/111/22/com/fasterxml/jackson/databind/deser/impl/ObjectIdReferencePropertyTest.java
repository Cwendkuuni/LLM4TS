package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testConstructorWithObjectIdInfo() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("property", String.class, null);
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, objectIdInfo);
        assertNotNull(property);
        assertEquals(objectIdInfo, property.getObjectIdInfo());
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdReferenceProperty src = new ObjectIdReferenceProperty(null, null);
        NullValueProvider nva = new Nulls("null");
        JsonDeserializer<?> deser = new StringDeserializer();
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(src, deser, nva);
        assertNotNull(property);
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullProvider());
    }

    // Other test cases for each method...

}