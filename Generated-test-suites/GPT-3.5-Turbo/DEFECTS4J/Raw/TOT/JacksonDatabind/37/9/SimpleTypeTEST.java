package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);

        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Number.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(Integer.class);

        assertEquals(Integer.class, narrowedType.getRawClass());
        assertFalse(narrowedType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentType(new SimpleType(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object typeHandler = new MyTypeHandler();
        SimpleType modifiedType = simpleType.withTypeHandler(typeHandler);

        assertEquals(Integer.class, modifiedType.getRawClass());
        assertFalse(modifiedType.isContainerType());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentTypeHandler(new MyTypeHandler());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object valueHandler = new MyValueHandler();
        SimpleType modifiedType = simpleType.withValueHandler(valueHandler);

        assertEquals(Integer.class, modifiedType.getRawClass());
        assertFalse(modifiedType.isContainerType());
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        try {
            simpleType.withContentValueHandler(new MyValueHandler());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType modifiedType = simpleType.withStaticTyping();

        assertEquals(String.class, modifiedType.getRawClass());
        assertFalse(modifiedType.isContainerType());
        assertTrue(modifiedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Number.class);
        assertNull(simpleType.refine(Float.class, null, null, null));
    }

    private static class MyTypeHandler {}

    private static class MyValueHandler {}
}