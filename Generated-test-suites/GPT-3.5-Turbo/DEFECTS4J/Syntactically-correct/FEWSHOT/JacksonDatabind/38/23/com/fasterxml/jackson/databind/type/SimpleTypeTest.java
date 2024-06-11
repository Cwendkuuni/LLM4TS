package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
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
        SimpleType.construct(Array.class);
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, type.narrow(Object.class));
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertNull(type.withContentType(String.class));
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        try {
            type.withContentTypeHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertEquals(type, type.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        try {
            type.withContentValueHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertNull(type.refine(Object.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        StringBuilder builder = new StringBuilder();
        assertEquals(builder, type.getErasedSignature(builder));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        StringBuilder builder = new StringBuilder();
        assertEquals(builder.append("L").append(Object.class.getName()).append(";"), type.getGenericSignature(builder));
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertEquals(type1, type2);
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class " + String.class.getName() + "]", type.toString());
    }
}