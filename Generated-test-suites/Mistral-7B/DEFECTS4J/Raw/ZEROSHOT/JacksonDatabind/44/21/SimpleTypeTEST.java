package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class<?>[]{}, simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertArrayEquals(new Class<?>[0], simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Class<?> subClass = Integer.class;
        SimpleType narrowType = simpleType.narrow(subClass);
        assertEquals(subClass, narrowType.getRawClass());
        assertSame(simpleType, narrowType.getSuperClass());
        assertArrayEquals(new Class<?>[0], narrowType.getSuperInterfaces());
        assertNull(narrowType.getValueHandler());
        assertNull(narrowType.getTypeHandler());
        assertFalse(narrowType.isStatic());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.putBinding(0, new JavaType(Integer.class));
        SimpleType simpleType = new SimpleType(cls, bindings, null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.Object;<0:Ljava.lang.Integer;>", sb.toString());
    }

    @Test
    public void testRefine() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.putBinding(0, new JavaType(Integer.class));
        JavaType refinedType = simpleType.refine(rawType, bindings, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("[Ljava.lang.Object;", simpleType.buildCanonicalName());

        TypeBindings bindings = TypeBindings.emptyBindings();
        bindings.putBinding(0, new JavaType(Integer.class));
        simpleType = new SimpleType(cls, bindings, null, null);
        assertEquals("[Ljava.lang.Object;<0:Ljava.lang.Integer;>", simpleType.buildCanonicalName());
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType contentType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        try {
            simpleType.withContentType(contentType);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(cls, withTypeHandler.getRawClass());
        assertSame(simpleType.getSuperClass(), withTypeHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withTypeHandler.getSuperInterfaces());
        assertEquals(handler, withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        try {
            simpleType.withContentTypeHandler(handler);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(cls, withValueHandler.getRawClass());
        assertSame(simpleType.getSuperClass(), withValueHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withValueHandler.getSuperInterfaces());
        assertEquals(null, withValueHandler.getValueHandler());
        assertEquals(handler, withValueHandler.getTypeHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        try {
            simpleType.withContentValueHandler(handler);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertEquals(cls, withStaticTyping.getRawClass());
        assertSame(simpleType.getSuperClass(), withStaticTyping.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), withStaticTyping.getSuperInterfaces());
        assertEquals(null, withStaticTyping.getValueHandler());
        assertEquals(null, withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testEquals() {
        Class<?> cls = List.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        simpleType4.setValueHandler(new Object());
        assertFalse(simpleType1.equals(simpleType4));
    }

    private Class<?>[] assertArrayEquals(Class<?>[] expected, Class<?>[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}