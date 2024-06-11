package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());

        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType constructed = SimpleType.constructUnsafe(Integer.class);

        assertEquals(Integer.class, constructed.getRawClass());
        assertNull(constructed.getBindings());
        assertNull(constructed.getSuperClass());
        assertNull(constructed.getInterfaces());

        assertNull(constructed.getValueHandler());
        assertNull(constructed.getTypeHandler());
        assertFalse(constructed.isStaticTyping());

        assertFalse(constructed.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType withStaticTyping = simpleType.withStaticTyping();

        assertEquals(String.class, withStaticTyping.getRawClass());
        assertNull(withStaticTyping.getBindings());
        assertNull(withStaticTyping.getSuperClass());
        assertNull(withStaticTyping.getInterfaces());

        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStaticTyping());

        assertFalse(withStaticTyping.isContainerType());
    }

    @Test
    public void testRefine() {
        assertNull(simpleType.refine(Integer.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(StringBuilder.class, result.getClass());
        assertTrue(result.toString().contains("[Ljava.lang.StringBuilder;"));
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(StringBuilder.class, result.getClass());
        assertTrue(result.toString().contains("[Ljava.lang.StringBuilder;"));
    }

    @Test
    public void testToString() {
        String result = simpleType.toString();
        assertNotNull(result);
        assertTrue(result.contains("simple type"));
        assertTrue(result.contains("class java.lang.String"));
    }

    @Test
    public void testEquals() {
        SimpleType sameSimpleType = new SimpleType(String.class);
        assertEquals(simpleType, simpleType);
        assertEquals(simpleType, sameSimpleType);
        assertNotEquals(simpleType, null);

        SimpleType differentSimpleType = new SimpleType(Integer.class);
        assertNotEquals(simpleType, differentSimpleType);
    }

}