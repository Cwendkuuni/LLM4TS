package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructWithEmptyBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructWithSuperClass() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(Object.class, simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructWithSuperClassAndSuperInterfaces() {
        JavaType superClass = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Number.class, TypeBindings.emptyBindings()) };
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(Object.class, simpleType.getSuperClass());
        assertEquals(superInterfaces, simpleType.getSuperTypes());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructWithValueHandler() {
        Object valueHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null, null, valueHandler, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null, null, null, false, typeHandler);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(String.class, staticType.getRawClass());
        assertNull(staticType.getSuperClass());
        assertNull(staticType.getSuperTypes());
        assertTrue(staticType.isStatic());
        assertFalse(staticType.isContainerType());
    }

    @Test
    public void testWithTypeHandler() {
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(String.class, withTypeHandler.getRawClass());
        assertEquals(Object.class, withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getSuperTypes());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isContainerType());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null, null, null, false);
        SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(String.class, withValueHandler.getRawClass());
        assertEquals(Object.class, withValueHandler.getSuperClass());
        assertNull(withValueHandler.getSuperTypes());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        String toString = simpleType.toString();
        String expected = "[simple type, class " + String.class.getName() + "]";
        assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsDifferentClass() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        SimpleType simpleType2 = new SimpleType(Integer.class, TypeBindings.emptyBindings(),
                Object.class, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsNull() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                Object.class, null);
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testGetSuperClassWhenTypeHasSuperClass() {
        JavaType superClass = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                superClass, null);
        assertEquals(superClass, simpleType.getSuperClass());
    }

    @Test
    public void testGetSuperClassWhenTypeHasNoSuperClass() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, null);
        assertNull(simpleType.getSuperClass());
    }

    @Test
    public void testGetSuperTypesWhenTypeHasSuperInterfaces() {
        JavaType superInterface1 = new SimpleType(Comparable.class, TypeBindings.emptyBindings());
        JavaType superInterface2 = new SimpleType(Serializable.class, TypeBindings.emptyBindings());
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, new JavaType[] { superInterface1, superInterface2 });
        JavaType[] superTypes = simpleType.getSuperTypes();
        assertEquals(2, superTypes.length);
        assertTrue(Arrays.asList(superTypes).contains(superInterface1));
        assertTrue(Arrays.asList(superTypes).contains(superInterface2));
    }

    @Test
    public void testGetSuperTypesWhenTypeHasNoSuperInterfaces() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(),
                null, null);
        assertNull(simpleType.getSuperTypes());
    }

}