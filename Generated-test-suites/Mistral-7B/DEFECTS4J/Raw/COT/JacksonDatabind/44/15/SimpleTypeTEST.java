package com.example.junit4; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory; // for TypeFactory.unknownType()

public class SimpleTypeTest {
    

    // Test constructor with a class
    @Test
    public void testSimpleTypeConstructionWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Test constructor with class, bindings, superClass, and superInterfaces
    @Test
    public void testSimpleTypeConstructionWithClassBindingsSuperClassSuperInterfaces() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = null;
        SimpleType simpleType = new SimpleType(stringClass, bindings, superClass, superInterfaces);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
    }

    // Test copy constructor
    @Test
    public void testCopyConstructor() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = null;
        SimpleType original = new SimpleType(stringClass, bindings, superClass, superInterfaces);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Test deprecated construct method
    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
    }

    // Test narrow method with same class
    @Test
    public void testNarrowMethodWithSameClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType narrowedType = simpleType.narrow(stringClass);
        assertEquals(simpleType, narrowedType);
    }

    // Test narrow method with subclass
    @Test
    public void testNarrowMethodWithSubclass() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType narrowedType = simpleType.narrow(stringBuilderClass);
        assertNotNull(narrowedType);
        assertTrue(stringBuilderClass.isAssignableFrom(narrowedType.getRawClass()));
    }

    // Test methods that should not be called by application code
    @Test(expected = IllegalArgumentException.class)
    public void testMethodsNotToBeCalledByApplicationCode() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.withContentType(null);
    }

    
}