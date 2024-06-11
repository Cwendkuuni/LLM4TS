package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(getClass());
        assertEquals(getClass(), simpleType.getRawClass());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        SimpleType simpleType = new SimpleType(getClass(), bindings, superClass, superInts);
        assertEquals(getClass(), simpleType.getRawClass());
    }

    @Test
    public void testConstructor3() {
        TypeBase base = new TypeBase();
        SimpleType simpleType = new SimpleType(base);
        assertEquals(base, simpleType.getRawClass());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(getClass(), bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertEquals(getClass(), simpleType.getRawClass());
    }

    @Test
    public void testConstructor5() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(getClass(), bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals(getClass(), simpleType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(getClass());
        assertEquals(getClass(), simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown for construct(Map.class)");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown for construct(Collection.class)");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException to be thrown for construct(int[].class)");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }

        TypeBindings b = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(getClass(), b, SimpleType._buildSuperClass(getClass().getSuperclass(), b), null, null, null, false);
        assertEquals(getClass(), simpleType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(getClass());
        assertFalse(simpleType._narrow(getClass()) == simpleType);
        assertEquals(getClass(), simpleType._narrow(getClass()).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(getClass());
        simpleType.withContentType(JavaType);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        try {
            simpleType.withContentTypeHandler(Object);
            fail("Expected IllegalArgumentException to be thrown for withContentTypeHandler(Object)");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        SimpleType simpleType = new SimpleType(getClass());
        assertFalse(simpleType.withValueHandler(h) == simpleType);
        assertEquals(getClass(), simpleType.withValueHandler(h).getRawClass());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        try {
            simpleType.withContentValueHandler(Object);
            fail("Expected IllegalArgumentException to be thrown for withContenValueHandler(Object)");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(getClass());
        assertFalse(simpleType.withStaticTyping() == simpleType);
        assertEquals(getClass(), simpleType.withStaticTyping().getRawClass());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(getClass());
        assertNull(simpleType.refine(getClass(), TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(getClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(getClass());
        simpleType.getErasedSignature(sb);
        assertEquals("com.fasterxml.jackson.databind.type.SimpleTypeTest", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(getClass());
        simpleType.getGenericSignature(sb);
        assertEquals("com.fasterxml.jackson.databind.type.SimpleTypeTest;", sb.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(getClass());
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(new Object()));
        assertTrue(simpleType.equals(simpleType));
        assertFalse(simpleType.equals(new SimpleType(getClass())));
        assertFalse(simpleType.equals(new SimpleType(null, TypeBindings.emptyBindings(), null, null, null, null, false)));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(getClass());
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleTypeTest]", simpleType.toString());
    }
}