package com.example.junit; // replace with your testing package

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    // Test constructor with a valid class
    @Test
    public void testValidConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test constructor with an empty bindings, null superClass and null superInts
    @Test
    public void testEmptyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test constructor with copy-constructor
    @Test
    public void testCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedType = new SimpleType(simpleType);
        assertNotNull(copiedType);
        assertEquals(simpleType.getRawClass(), copiedType.getRawClass());
    }

    // Test constructor with a class, TypeBindings, superClass, superInts, valueHandler, typeHandler and asStatic
    @Test
    public void testFullConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType fullConstructedType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false, null, null, null, null, false);
        assertNotNull(fullConstructedType);
        assertEquals(simpleType.getRawClass(), fullConstructedType.getRawClass());
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        Class<?> cls = List.class; // Map, Collection or Array is not allowed
        SimpleType.construct(cls);
    }

    // Test getRawClass method
    @Test
    public void testGetRawClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test getBindings method
    @Test
    public void testGetBindings() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertSame(TypeBindings.emptyBindings(), simpleType.getBindings());
    }

    // Test getSuperClass method
    @Test
    public void testGetSuperClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.getSuperclass());
    }

    // Test getSuperInts method
    @Test
    public void testGetSuperInts() {
        Class<?> cls = List.class; // List has a generic type
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { javaType });
        JavaType[] superInts = simpleType.getSuperInterfaces();
        assertEquals(1, superIntings.length);
        assertEquals(javaType, superInts[0]);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    // Test toString method
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        String expected = "[simple type, class " + cls.getName() + "]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    // Test equals method
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));
    }

    // Test equals with null and different classes
    @Test
    public void testEqualsWithNullAndDifferentClasses() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
    }

    // Test equals with different bindings
    @Test
    public void testEqualsWithDifferentBindings() {
        Class<?> cls = String.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = new TypeBindings();
        SimpleType simpleType1 = new SimpleType(cls, bindings1, null, null);
        SimpleType simpleType2 = new SimpleType(cls, bindings2, null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    // Test equals with same class but different bindings
    @Test
    public void testEqualsWithSameClassButDifferentBindings() {
        Class<?> cls = String.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = new TypeBindings();
        SimpleType simpleType1 = new SimpleType(cls, bindings1, null, null);
        SimpleType simpleType2 = new SimpleType(cls, bindings2, null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    
}