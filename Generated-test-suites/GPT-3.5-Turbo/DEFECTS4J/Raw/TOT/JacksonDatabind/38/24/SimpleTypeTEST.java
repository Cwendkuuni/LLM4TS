package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType simpleType = SimpleType.construct(List.class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.List)", e.getMessage());
        }

        try {
            SimpleType simpleType = SimpleType.construct(int[].class);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertSame(simpleType, simpleType.narrow(Object.class));
        SimpleType narrowedType = (SimpleType) simpleType.narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
        assertFalse(narrowedType.isContainerType());
        assertEquals("[simple type, class java.lang.String]", narrowedType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(String.class);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType withValueHandler = simpleType.withValueHandler(new Object());
        assertNotNull(withValueHandler);
        assertEquals(Object.class, withValueHandler.getRawClass());
        assertFalse(withValueHandler.isContainerType());
        assertEquals("[simple type, class java.lang.Object]", withValueHandler.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(Object.class, withStaticTyping.getRawClass());
        assertFalse(withStaticTyping.isContainerType());
        assertEquals("[simple type, class java.lang.Object]", withStaticTyping.toString());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(int.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder signature = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(signature);
        assertNotNull(erasedSignature);
        assertEquals("Ljava/lang/Object;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder signature = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(signature);
        assertNotNull(genericSignature);
        assertEquals("Ljava/lang/Object;", genericSignature.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType3));

        assertTrue(simpleType1.equals(simpleType1));
    }

}