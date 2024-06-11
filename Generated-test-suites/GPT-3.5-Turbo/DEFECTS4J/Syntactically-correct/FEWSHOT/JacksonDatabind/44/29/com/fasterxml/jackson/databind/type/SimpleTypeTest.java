package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType type = new SimpleType(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType base = new SimpleType(String.class);
        SimpleType type = new SimpleType(base);
        assertEquals(base.getRawClass(), type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
        assertEquals("[simple type, class java.lang.String]", type.toString());
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
    public void testWithContentType() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentType(String.class);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentValueHandler(new Object());
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(String.class);
        assertFalse(type.isStaticTyping());
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(String.class);
        assertNull(type.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }
}