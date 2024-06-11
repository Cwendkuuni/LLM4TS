package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_whenGivenCollectionClass() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_whenGivenMapClass() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_whenGivenArrayClass() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> baseClass = String.class;
        Class<?> subClass = Integer.class;
        SimpleType simpleType = new SimpleType(baseClass, null, null, null, null, null, false);
        SimpleType narrowedType = simpleType.narrow(subClass);
        assertEquals(subClass, narrowedType.getRawClass());
        assertNotNull(narrowedType.getSuperClass());
        assertArrayEquals(new JavaType[0], narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow_whenGivenNonSubclass() {
        Class<?> baseClass = String.class;
        Class<?> subClass = Object.class;
        SimpleType simpleType = new SimpleType(baseClass, null, null, null, null, null, false);
        simpleType.narrow(subClass);
    }

    @Test
    public void testWithContentType() {
        IllegalArgumentException expectedException = new IllegalArgumentException("Simple types have no content types; can not call withContentType()");
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        try {
            simpleType.withContentType(IntegerType.getInstance());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        Object handler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType, null, null, null, null, null, false);
        assertNull(simpleType.refine(rawType, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> containerClass = ArrayList.class;
        Class<?> elementClass = Integer.class;
        SimpleType simpleType = new SimpleType(containerClass, null, null, null, null, null, false);
        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "java.util.ArrayList<java.lang.Integer>";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> containerClass = ArrayList.class;
        Class<?> elementClass = Integer.class;
        SimpleType simpleType = new SimpleType(containerClass, null, null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("java.util.List<java.lang.Integer>");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> containerClass = ArrayList.class;
        Class<?> elementClass = Integer.class;
        SimpleType simpleType = new SimpleType(containerClass, null, null, null, null, null, false);
        StringBuilder expectedSignature
                = new StringBuilder().append("java.util.List<").append(Integer.class.getName()).append('>');
        StringBuilder actualSignature = new StringBuilder();
        simpleType.getGenericSignature(actualSignature);
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }
}