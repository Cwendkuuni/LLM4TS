package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isArrayType());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isArrayType());
    }

    @Test
    public void testConstructor3() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isArrayType());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isArrayType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isArrayType());
    }

    @Test
    @Deprecated
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Object.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.lang.Object)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = (SimpleType) simpleType1._narrow(Object.class);
        assertSame(simpleType1, simpleType2);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = simpleType1.withTypeHandler(null);
        assertSame(simpleType1, simpleType2);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = simpleType1.withValueHandler(null);
        assertSame(simpleType1, simpleType2);
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, simpleType.getErasedSignature(sb));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, simpleType.getGenericSignature(sb));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

}