package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertEquals(0, type.getInterfaces().length);
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertEquals(0, type.getInterfaces().length);
    }

    @Test
    public void test_narrow() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowedType = type._narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
        assertSame(type.getBindings(), narrowedType.getBindings());
        assertSame(type.getSuperClass(), narrowedType.getSuperClass());
        assertArrayEquals(type.getInterfaces(), narrowedType.getInterfaces());
        assertSame(type.getValueHandler(), narrowedType.getValueHandler());
        assertSame(type.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(type.isStatic(), narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentType(type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType withTypeHandler = type.withTypeHandler(handler);
        assertNotSame(type, withTypeHandler);
        assertSame(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTyping = type.withStaticTyping();
        assertNotSame(type, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNull(type.refine(Object.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertSame(sb, result);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertSame(sb, result);
        assertEquals("Ljava/lang/String;;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        SimpleType type3 = SimpleType.constructUnsafe(Object.class);
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        assertFalse(type1.equals(type3));
        assertFalse(type3.equals(type1));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals("String"));
    }
}