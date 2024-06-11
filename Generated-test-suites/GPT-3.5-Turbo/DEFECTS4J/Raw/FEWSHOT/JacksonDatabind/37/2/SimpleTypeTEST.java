package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructWithInvalidMap() {
        try {
            SimpleType.construct(java.util.Map.class);
            fail("Should throw IllegalArgumentException for Map type");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testConstructWithInvalidCollection() {
        try {
            SimpleType.construct(java.util.Collection.class);
            fail("Should throw IllegalArgumentException for Collection type");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testConstructWithArray() {
        try {
            SimpleType.construct(String[].class);
            fail("Should throw IllegalArgumentException for array type");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Should throw IllegalArgumentException for content type");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Should throw IllegalArgumentException for content value handler");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType.toString());
    }


}