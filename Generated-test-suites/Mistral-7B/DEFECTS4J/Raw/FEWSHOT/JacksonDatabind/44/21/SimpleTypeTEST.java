package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(original);

        assertEquals(original, copy);
        assertNotSame(original, copy);
        assertEquals(cls, copy.getRawClass());
        assertNull(copy.getSuperClass());
        assertNull(copy.getSuperInterfaces());
        assertFalse(copy.isStatic());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.bindings(Integer.TYPE), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertEquals(java.lang.Object[].class, simpleType.getSuperClass());
        assertArrayEquals(new Class[] { Integer.TYPE }, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object valueHandler = new Object();
        SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(simpleType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object typeHandler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertFalse(simpleType.isStatic());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructDeprecated() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        Class<?> cls = new Class<?>(){};
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.construct(cls);
    }
}