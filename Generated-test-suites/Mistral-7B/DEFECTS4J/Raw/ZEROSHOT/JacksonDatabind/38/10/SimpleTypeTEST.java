package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType javaType = TypeFactory.defaultInstance().constructType(cls);
        assertEquals(javaType, simpleType.getType());
    }

    @Test
    public void testUnsafeSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        JavaType javaType = TypeFactory.defaultInstance().constructType(cls);
        assertEquals(javaType, simpleType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithArray() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> cls = String.class;
        Class<?> subclass = Object.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowType = simpleType._narrow(subclass);
        assertFalse(simpleType.equals(narrowType));
        assertNull(narrowType.getType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        TypeBindings bindings = new TypeBindings(Arrays.asList(new TypeBinding("T", Integer.TYPE)));
        SimpleType simpleTypeWithBindings = new SimpleType(cls, bindings, null, null);
        StringBuilder expectedWithBindings = new StringBuilder("java.lang.String<T>");
        StringBuilder actualWithBindings = simpleTypeWithBindings.getGenericSignature(new StringBuilder());
        assertEquals(expectedWithBindings.toString(), actualWithBindings.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        String expected = "[simple type, class java.lang.String]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType3));
    }
}