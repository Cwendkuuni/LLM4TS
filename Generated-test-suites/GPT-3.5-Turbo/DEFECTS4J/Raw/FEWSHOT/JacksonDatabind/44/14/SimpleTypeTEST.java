package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(String[].class);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
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
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(simpleType, simpleType.narrow(String.class));
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithGenericTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withTypeHandler(null);
        assertNull(simpleType.getTypeHandler());
    }

    @Test
    public void testWithFakeValueTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isStatic());
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(simpleType, staticType);
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("java.util.Map", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.getGenericSignature(new StringBuilder());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.util.Map]", simpleType.toString());
    }
}