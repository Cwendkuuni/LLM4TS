package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Double.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNull(simpleType.refine(Double.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Float.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Long.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals(sb.toString(), "J");
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Float.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
    }

}