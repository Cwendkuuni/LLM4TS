package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getContentType(), null);
        assertEquals(simpleType.isContainerType(), false);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testConstructor2() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        SimpleType simpleType = new SimpleType(Object.class, typeBindings, superClass, superInts);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getContentType(), null);
        assertEquals(simpleType.isContainerType(), false);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testConstructor3() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Object.class, typeBindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getContentType(), null);
        assertEquals(simpleType.isContainerType(), false);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testConstructor4() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(Object.class, typeBindings, superClass, superInts, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getContentType(), null);
        assertEquals(simpleType.isContainerType(), false);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getContentType(), null);
        assertEquals(simpleType.isContainerType(), false);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Can not construct SimpleType for a Map (class: java.util.Map)");
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Can not construct SimpleType for a Collection (class: java.util.Collection)");
        }

        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Can not construct SimpleType for an array (class: [Ljava.lang.String;)");
        }
        
        TypeBindings b = TypeBindings.emptyBindings();
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertEquals(simpleType.getClass(), SimpleType.class);
        assertEquals(simpleType.getContentType(), null);
        assertEquals(simpleType.isContainerType(), false);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType narrowedType = simpleType._narrow(Object.class);
        assertEquals(narrowedType, simpleType);

        Class<?> subclass = Integer.class;
        narrowType = simpleType._narrow(subclass);
        assertEquals(narrowedType.getClass(), SimpleType.class);
        assertEquals(narrowedType.getContentType(), null);
        assertEquals(narrowedType.isContainerType(), false);
        assertEquals(narrowedType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Integer;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(null);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(newSimpleType.getClass(), SimpleType.class);
        assertEquals(newSimpleType.getContentType(), null);
        assertEquals(newSimpleType.isContainerType(), false);
        assertEquals(newSimpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertEquals(newSimpleType.getClass(), SimpleType.class);
        assertEquals(newSimpleType.getContentType(), null);
        assertEquals(newSimpleType.isContainerType(), false);
        assertEquals(newSimpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertEquals(newSimpleType.getClass(), SimpleType.class);
        assertEquals(newSimpleType.getContentType(), null);
        assertEquals(newSimpleType.isContainerType(), false);
        assertEquals(newSimpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(refinedType, null);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.buildCanonicalName(), "java.lang.Object");
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.isContainerType(), false);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.getErasedSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.getGenericSignature(new StringBuilder()).toString(), "Ljava.lang.Object;");
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        SimpleType simpleType3 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType.toString(), "[simple type, class java.lang.Object]");
    }

}