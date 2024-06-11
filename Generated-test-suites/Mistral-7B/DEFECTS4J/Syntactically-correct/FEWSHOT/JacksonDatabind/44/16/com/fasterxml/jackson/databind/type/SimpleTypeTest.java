package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(ArrayList.class);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test
    public void testConstructWithSuperClass() {
        Class<?> superClass = Object.class;
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType superType = simpleType.getSuperClass();
        assertNull(superType);

        simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, null);
        assertEquals(superClass, simpleType.getSuperClass());

        simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, new JavaType[] {});
        assertEquals(superClass, simpleType.getSuperClass());
    }

    @Test
    public void testConstructWithSuperInterface() {
        Class<?> interfaceType = Comparable.class;
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, new JavaType[] {interfaceType}); {};
        JavaType[] superInterfaces = simpleType.getSuperInterfaces();
        assertNotNull(superInterfaces);
        assertEquals(1, superInterfaces.length);
        assertEquals(interfaceType, superInterfaces[0]);

        simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, new JavaType[] {interfaceType, Object.class}); {};
        JavaType[] superInterfaces2 = simpleType.getSuperInterfaces();
        assertNotNull(superInterfaces2);
        assertEquals(2, superInterfaces2.length);
        assertEquals(interfaceType, superInterfaces2[0]);
        assertEquals(Object.class, superInterfaces2[1]);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());

        simpleType = new SimpleType(int[].class, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class [I]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        simpleType1 = new SimpleType(int.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));

        simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        simpleType3._bindings = new TypeBindings() {};
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[");
        assertEquals(expected.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[");
        simpleType.getGenericSignature(expected);
        assertEquals("List<>", expected.toString());
    }
}