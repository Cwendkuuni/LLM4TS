package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import junit.framework.TestCase;

public class SimpleTypeTest extends TestCase {

    // Test for default constructor
    public void testDefaultConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = simpleType;
        assertEquals(cls.getName(), javaType.getRawClass().getName());
    }

    // Test for constructor with Class, TypeBindings, JavaType superClass, and JavaType[] superInts
    public void testConstructorWithArguments() {
        Class<?> cls = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = String.class;
        JavaType[] superInts = new JavaType[0];
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts);
        JavaType javaType = simpleType;
        assertEquals(cls.getName(), javaType.getRawClass().getName());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
    }

    // Test for copy constructor
    public void testCopyConstructor() {
        Class<?> cls = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = String.class;
        JavaType[] superInts = new JavaType[0];
        SimpleType simpleType1 = new SimpleType(cls, bindings, superClass, superInts);
        SimpleType simpleType2 = new SimpleType(simpleType1);
        JavaType javaType1 = simpleType1;
        JavaType javaType2 = simpleType2;
        assertEquals(cls.getName(), javaType1.getRawClass().getName());
        assertEquals(cls.getName(), javaType2.getRawClass().getName());
        assertNotSame(javaType1, javaType2);
    }

    // Test for invalid constructor calls
    public void testInvalidConstructors() {
        Class<?> cls = String.class;
        try {
            new SimpleType(cls, null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(cls, null, null, null, null, null, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(cls, null, null, null, null, null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(cls, null, null, null, null, null, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimpleType(cls, null, null, null, null, null, true, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for constructUnsafe method
    public void testConstructUnsafeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        JavaType javaType = simpleType;
        assertEquals(cls.getName(), javaType.getRawClass().getName());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new JavaType[0], javaType.getSuperTypes());
    }

    // Test for deprecated construct method
    public void testDeprecatedConstructMethod() {
        try {
            SimpleType simpleType = SimpleType.construct(Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for _narrow method
    public void testNarrowMethod() {
        Class<?> cls = Integer.class;
        Class<?> subclass = Byte.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType narrowedType = simpleType._narrow(subclass);
        assertNotNull(narrowedType);
        assertEquals(subclass.getName(), narrowedType.getRawClass().getName());
    }

    // Test for equals method
    public void testEqualsMethod() {
        Class<?> cls = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = String.class;
        JavaType[] superInts = new JavaType[0];
        SimpleType simpleType1 = new SimpleType(cls, bindings, superClass, superInts);
        SimpleType simpleType2 = new SimpleType(cls, bindings, superClass, superInts);
        SimpleType simpleType3 = new SimpleType(Integer[].class, bindings, superClass, superInts);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }
}