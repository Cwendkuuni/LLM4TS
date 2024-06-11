package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType baseType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedType = new SimpleType(baseType);
        assertEquals(baseType.getRawClass(), copiedType.getRawClass());
        assertEquals(baseType.getBindings(), copiedType.getBindings());
        assertEquals(baseType.getSuperClass(), copiedType.getSuperClass());
        assertArrayEquals(baseType.getSuperInterfaces(), copiedType.getSuperInterfaces());
        assertEquals(baseType.getValueHandler(), copiedType.getValueHandler());
        assertEquals(baseType.getTypeHandler(), copiedType.getTypeHandler());
        assertEquals(baseType.isStatic(), copiedType.isStatic());
    }

    /**
     * Test for the constructor used by {@link ReferenceType}.
     *
     * @since 2.6
     */
    @Test
    public void testSimpleTypeReferenceConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, 0, null, false);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Map.class.getDeclaredConstructor(); // ensure Map is loaded
        Class<?> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Collection.class.getDeclaredConstructor(); // ensure Collection is loaded
        Class<?> cls = Collection.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        int[] arr = new int[10];
        Class<?> cls = arr.getClass().getComponentType();
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> baseClass = String.class;
        Class<?> subClass = Integer.class;
        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = baseType._narrow(subClass);
        assertEquals(subClass, narrowedType.getRawClass());
        assertEquals(baseType.getBindings(), narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }
}