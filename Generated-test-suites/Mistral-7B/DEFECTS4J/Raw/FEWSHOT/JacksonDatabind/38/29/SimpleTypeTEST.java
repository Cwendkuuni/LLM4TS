package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> stringClass = String.class;
        Class<?> stringSubClass = StringBuffer.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        SimpleType narrowedType = simpleType.narrow(stringSubClass);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(stringSubClass, narrowedType.getRawClass());
        assertNotSame(simpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithIncompatibleClass() {
        Class<?> intClass = Integer.class;
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(intClass);
        simpleType.narrow(stringClass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> stringClass = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = SimpleType.construct(stringClass);
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> stringClass = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = SimpleType.construct(stringClass);
        try {
            simpleType.withContentTypeHandler(handler);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithValueHandler() {
        Class<?> stringClass = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = SimpleType.construct(stringClass);
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> stringClass = String.class;
        Object handler = new Object() {};
        SimpleType simpleType = SimpleType.construct(stringClass);
        try {
            simpleType.withContentValueHandler(handler);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> stringClass = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { List.class };
        SimpleType simpleType = SimpleType.construct(stringClass);
        JavaType refinedType = simpleType.refine(StringBuffer.class, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "java.lang.String";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String expectedErasedSignature = "java.lang.String";
        assertEquals(expectedErasedSignature, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String expectedGenericSignature = "java.lang.String";
        assertEquals(expectedGenericSignature, sb.toString());
    }

    @Test
    public void testEquals() {
        Class<?> stringClass1 = String.class;
        Class<?> stringClass2 = String.class;
        SimpleType simpleType1 = SimpleType.construct(stringClass1);
        SimpleType simpleType2 = SimpleType.construct(stringClass2);
        SimpleType simpleType3 = SimpleType.construct(Object.class);
        SimpleType simpleType4 = new SimpleType(String.class, new TypeBindings(), null, null, null, null, false);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(simpleType4));
    }

    @Test
    public void testToString() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        String expectedToString = "[simple type, class java.lang.String]";
        assertEquals(expectedToString, simpleType.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertFalse(simpleType.isContainerType());
    }
}