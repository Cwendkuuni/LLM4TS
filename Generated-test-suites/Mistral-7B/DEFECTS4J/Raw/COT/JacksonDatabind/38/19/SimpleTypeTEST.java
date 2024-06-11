package com.yourpackage;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType actual = SimpleType.constructUnsafe(cls);
        SimpleType expected = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String[]");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());

        simpleType = new SimpleType(List.class, TypeBindings.bindValue("elementType", JavaType.getType(int.class)), null, null);
        expected = new StringBuilder("java.util.List<java.lang.Integer>");
        actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        simpleType1 = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType actual = simpleType.withStaticTyping();
        SimpleType expected = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, true);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNarrowWithDifferentClass() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType toNarrowTo = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        simpleType.withNarrow(toNarrowTo.getTypeClass());
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType actual = simpleType.withTypeHandler(handler);
        SimpleType expected = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, handler, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType actual = simpleType.withValueHandler(handler);
        SimpleType expected = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, handler, null, false);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerWithSimpleType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        simpleType.withContentTypeHandler(handler);
    }
}