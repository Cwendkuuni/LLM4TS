package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;

        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor3() {
        SimpleType simpleTypeBase = new SimpleType(Object.class);
        SimpleType simpleType = new SimpleType(simpleTypeBase);
        assertEquals(Object.class, simpleTypeBase.getRawClass());
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor5() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType simpleType = new SimpleType(Object.class, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(Object[].class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType._narrow(Object.class));

        SimpleType narrowType = simpleType._narrow(String.class);
        assertEquals(String.class, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(null);
    }

    @Test
    public void testWithTypeHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(valueHandler);
        assertEquals(valueHandler, newSimpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, newSimpleType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType.withStaticTyping());

        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, new TypeBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("java.lang.Object", simpleType.toCanonical());
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
        assertEquals(sb.toString(), simpleType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        assertEquals(sb.toString(), simpleType.getGenericSignature(sb).toString());
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
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType2.equals(simpleType1));
        assertFalse(simpleType1.equals(simpleType3));
    }
}