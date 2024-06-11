package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType original = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType copy = new SimpleType(original);
        assertEquals(original.getRawClass(), copy.getRawClass());
        assertEquals(original.getBindings(), copy.getBindings());
        assertEquals(original.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
        assertEquals(original.getValueHandler(), copy.getValueHandler());
        assertEquals(original.getTypeHandler(), copy.getTypeHandler());
        assertEquals(original.isStatic(), copy.isStatic());
        assertEquals(original.isContainerType(), copy.isContainerType());
    }

    @Test
    public void testSimpleTypeNarrow() {
        SimpleType original = new SimpleType(Integer[].class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType narrow = original.narrow(Integer.class);
        assertNotNull(narrow);
        assertEquals(Integer.class, narrow.getRawClass());
        assertEquals(original.getBindings(), narrow.getBindings());
        assertNull(narrow.getSuperClass());
        assertNull(narrow.getSuperInterfaces());
        assertFalse(narrow.isStatic());
        assertFalse(narrow.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeNarrowFail() {
        SimpleType original = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType narrow = original.narrow(Integer.class);
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals("java.lang.String[]", simpleType.getCanonicalName());
    }

    @Test
    public void testSimpleTypeIsInstance() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertTrue(simpleType.isInstance(String.class));
        assertTrue(simpleType.isInstance(String.class.getCanonicalName()));
        assertTrue(simpleType.isInstance((Type) String[].class));
        assertTrue(simpleType.isInstance((Type) String[].class.getCanonicalName()));
        assertFalse(simpleType.isInstance(Integer.class));
        assertFalse(simpleType.isInstance(Integer.class.getCanonicalName()));
    }

    @Test
    public void testSimpleTypeEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeNotEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testSimpleTypeHashCode() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
    }

    private void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (null == expected || null == actual) {
            assertEquals(expected, actual);
        } else {
            assertEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], actual[i]);
            }
        }
    }
}