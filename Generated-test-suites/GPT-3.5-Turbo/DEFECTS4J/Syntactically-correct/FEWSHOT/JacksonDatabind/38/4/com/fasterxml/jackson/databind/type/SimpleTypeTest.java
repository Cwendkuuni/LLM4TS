package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(String[].class);
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType narrowedType = (SimpleType) type.narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentType(Object.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentTypeHandler(Object.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType newType = type.withValueHandler(Object.class);
        assertNotNull(newType);
        assertEquals(Object.class, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withContentValueHandler(Object.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType newType = type.withStaticTyping();
        assertNotNull(newType);
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType refinedType = type.refine(String.class, null, null, null);
        assertTrue(refinedType instanceof SimpleType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(String.class);
        String stringRep = type.toString();
        assertEquals("[simple type, class java.lang.String]", stringRep);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        SimpleType type3 = SimpleType.construct(Integer.class);
        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}