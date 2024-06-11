package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructGeneric() {
        SimpleType simpleType = SimpleType.constructGeneric(int.class);
        assertEquals(int.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings().getBoundType(0));
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings().getBoundType(0));
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType construct = SimpleType.construct(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.lang.Object)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertSame(simpleType, simpleType.narrow(String.class));
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructGeneric(int.class);
        try {
            simpleType.withContentType(SimpleType.constructGeneric(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructGeneric(int.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructGeneric(int.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertEquals(int.class, withStaticTyping.getRawClass());
        assertNull(withStaticTyping.getBindings().getBoundType(0));
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getSuperInterfaces());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructGeneric(int.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType3 = SimpleType.constructGeneric(int.class);
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(""));
    }
}