package com.fasterxml.jackson.databind.type; // make sure the test file is in the same package as the tested class

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;

public class SimpleTypeTest {

    

    // Test method for constructor with Class arg
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperclass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Test method for constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInterfaces
    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(Object.class, simpleType.getSuperclass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    // Test method for copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType, copiedSimpleType);
    }

    // Test method for static constructor
    @Test
    public void testSimpleTypeStaticConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperclass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Test method for constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, int extraHash, Object valueHandler, Object typeHandler, and boolean asStatic
    @Test
    public void testSimpleTypeConstructorWithAllArgs() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces, 0, valueHandler, typeHandler, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(Object.class, simpleType.getSuperclass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    // Test method for constructor with Class, TypeBindings, JavaType superClass, JavaType[] superInterfaces, int extraHash, Object valueHandler, Object typeHandler, and boolean asStatic (passed as SimpleType)
    @Test
    public void testSimpleTypeConstructorWithAllArgsPassedAsSimpleType() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType simpleType = new SimpleType(originalSimpleType, 0, null, null, null, null, null, false);
        assertEquals(originalSimpleType, simpleType);
    }

    // Test method for unsafe constructor
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeUnsafeConstructorWithArrayClass() {
        Class<int[]> intArrayClass = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(intArrayClass);
    }

    // Test method for equals
    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        assertTrue(simpleType1.equals(simpleType2));
    }

    // Test method for equals with different classes
    @Test
    public void testSimpleTypeNotEqualsDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType integerSimpleType = new SimpleType(integerClass);
        assertFalse(stringSimpleType.equals(integerSimpleType));
    }

    // Test method for equals with null
    @Test
    public void testSimpleTypeNotEqualsNull() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertFalse(stringSimpleType.equals(null));
    }

    // Test method for equals with different bindings
    @Test
    public void testSimpleTypeNotEqualsDifferentBindings() {
        Class<String> stringClass = String.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = new TypeBindings(bindings1, new JavaTypeRef(String.class));
        SimpleType simpleType1 = new SimpleType(stringClass, bindings1);
        SimpleType simpleType2 = new SimpleType(stringClass, bindings2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    // Test method for equals with different superclass
    @Test
    public void testSimpleTypeNotEqualsDifferentSuperclass() {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType simpleType2 = new SimpleType(objectClass, TypeBindings.emptyBindings());
        assertFalse(simpleType1.equals(simpleType2));
    }

    // Test method for equals with different superinterfaces
    @Test
    public void testSimpleTypeNotEqualsDifferentSuperinterfaces() {
        Class<String> stringClass = String.class;
        JavaType[] superInterfaces1 = new JavaType[0];
        JavaType[] superInterfaces2 = new JavaType[] { TypeFactory.defaultInstance().constructType(Object.class) };
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, superInterfaces1);
        SimpleType simpleType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, superInterfaces2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    
}