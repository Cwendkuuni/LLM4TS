package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor1() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new SimpleType[0];
        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInts);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getInterfaces());
    }

    @Test
    public void testConstructor3() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInts = new SimpleType[0];
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;
        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInts, valueHandler, typeHandler, asStatic);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInts, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType._narrow(Object.class));
        assertEquals(new SimpleType(String.class), simpleType._narrow(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType._narrow(String.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentType(new SimpleType(String.class));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object typeHandler = new Object();
        SimpleType modifiedType = simpleType.withTypeHandler(typeHandler);
        assertNotNull(modifiedType);
        assertEquals(Object.class, modifiedType.getRawClass());
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        SimpleType modifiedType = simpleType.withValueHandler(valueHandler);
        assertNotNull(modifiedType);
        assertEquals(Object.class, modifiedType.getRawClass());
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertNotNull(modifiedType);
        assertEquals(Object.class, modifiedType.getRawClass());
        assertTrue(modifiedType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(String.class, null, new SimpleType(Object.class), new SimpleType[0]));
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
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, simpleType.getGenericSignature(sb));
        assertEquals("Ljava/lang/Object;<>;", sb.toString());
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
        SimpleType simpleType3 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }
}