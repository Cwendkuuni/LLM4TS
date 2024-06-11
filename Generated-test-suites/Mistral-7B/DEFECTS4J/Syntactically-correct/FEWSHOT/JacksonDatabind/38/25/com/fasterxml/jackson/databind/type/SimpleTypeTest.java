package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType base = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(base);
        assertEquals(base.getClass(), copy.getClass());
        assertEquals(base.getRawClass(), copy.getRawClass());
        assertEquals(base._bindings, copy._bindings);
        assertEquals(base._superClass, copy._superClass);
        assertEquals(base._superInterfaces, copy._superInterfaces);
        assertEquals(base._valueHandler, copy._valueHandler);
        assertEquals(base._typeHandler, copy._typeHandler);
        assertEquals(base._asStatic, copy._asStatic);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
    }

    // Add more test cases as needed for each method in the SimpleType class
}