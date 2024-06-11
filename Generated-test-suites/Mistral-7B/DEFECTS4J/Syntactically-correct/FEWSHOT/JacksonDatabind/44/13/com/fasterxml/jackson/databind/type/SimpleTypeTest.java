package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType simpleTypeWithBindings = new SimpleType(cls, TypeBindings.emptyBindings());
        SimpleType simpleTypeWithSuperClass = new SimpleType(cls, TypeBindings.emptyBindings(), Arrays.asList(Object.class).toJavaType(TypeFactory.defaultInstance()));
        SimpleType copyConstructor = new SimpleType(simpleType);

        assertEquals(cls, simpleType.getRawClass());
        assertEquals(simpleType.getClass(), simpleType.getClass());
        assertEquals(cls, simpleTypeWithBindings.getRawClass());
        assertEquals(simpleType.getClass(), simpleTypeWithBindings.getClass());
        assertEquals(cls, simpleTypeWithSuperClass.getRawClass());
        assertEquals(Object.class, simpleTypeWithSuperClass.getSuperClass());
        assertEquals(simpleType, copyConstructor);
    }

    @Test
    public void testConstructUnsafe() {
        try {
            Class<?> cls = Integer[].class;
            SimpleType simpleType = SimpleType.constructUnsafe(cls);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        SimpleType simpleTypeWithBindings = new SimpleType(cls, TypeBindings.emptyBindings());

        assertEquals(cls, simpleType.getRawClass());
        assertEquals(simpleType.getClass(), simpleTypeWithBindings.getClass());
        assertNotNull(simpleType.getSuperClass());
        assertEquals(null, simpleTypeWithBindings.getSuperClass());
    }

    @Test
    public void testNarrow() {
        Class<?> stringClass = String.class;
        Class<?> stringSubClass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowType = simpleType.narrow(stringSubClass);

        assertEquals(stringSubClass, narrowType.getRawClass());
        assertNotSame(simpleType, narrowType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());

        SimpleType arrayType = new SimpleType(int[].class);
        assertTrue(arrayType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());

        SimpleType listType = new SimpleType(ArrayList.class);
        assertEquals("java.util.ArrayList<java.lang.Object>", listType.buildCanonicalName());

        SimpleType mapType = new SimpleType(HashMap.class);
        assertEquals("java.util.HashMap<java.lang.Object,java.lang.Object>", mapType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());

        SimpleType listType = new SimpleType(ArrayList.class);
        assertEquals("[simple type, class java.util.ArrayList]", listType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}