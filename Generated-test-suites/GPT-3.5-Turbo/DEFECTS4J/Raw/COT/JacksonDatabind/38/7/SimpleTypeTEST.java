package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals("class java.lang.String", type.toString());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType type = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType type = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType type = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType newType = type.withTypeHandler(handler);
        assertSame(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isStaticTyping());
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNull(type.refine(List.class, null, null, null));
    }

}