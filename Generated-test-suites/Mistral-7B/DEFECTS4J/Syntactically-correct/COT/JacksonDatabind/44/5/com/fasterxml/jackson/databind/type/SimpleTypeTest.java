package com.fasterxml.jackson.databind.type; // Import the necessary classes

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest { // Test class name

    

    // Test case for the constructor using a Class
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test case for the constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInterfaces
    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<String> cls = String.class;
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test case for the copy-constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType originalSimpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
    }

    // Test case for the constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, Object valueHandler, Object typeHandler, and boolean asStatic
    @Test
    public void testSimpleTypeConstructorWithAllArguments() {
        Class<String> cls = String.class;
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, valueHandler, typeHandler, false);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test case for the constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, int extraHash, Object valueHandler, Object typeHandler, and boolean asStatic
    @Test
    public void testSimpleTypeConstructorWithAllArgumentsPassThrough() {
        Class<String> cls = String.class;
        SimpleType base = new SimpleType(cls);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test case for the static construct method with Class
    @Test(expected = IllegalArgumentException.class)
    public void testStaticConstructMethodWithClassThrowsException() {
        Class<String> cls = String.class;
        SimpleType.construct(cls);
    }

    // Test case for the deprecated construct method with Class
    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithClassThrowsException() {
        Class<String> cls = String.class;
        SimpleType.construct(cls);
    }

    // Test case for the equals method
    @Test
    public void testEquals() {
        Class<String> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        assertTrue(simpleType1.equals(simpleType2));
    }

    // Test case for the equals method with different classes
    @Test
    public void testEqualsWithDifferentClasses() {
        Class<String> cls1 = String.class;
        Class<Integer> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    // Test case for the _narrow method with same class
    @Test
    public void testNarrowMethodWithSameClass() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType narrowedSimpleType = simpleType._narrow(cls);
        assertEquals(simpleType, narrowedSimpleType);
    }

    // Test case for the _narrow method with subclass
    @Test
    public void testNarrowMethodWithSubclass() {
        Class<String> cls = String.class;
        Class<StringBuilder> subclass = StringBuilder.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType narrowedSimpleType = simpleType._narrow(subclass);
        assertNotNull(narrowedSimpleType);
        assertTrue(subclass.isAssignableFrom(narrowedSimpleType.getRawClass()));
    }

    
}