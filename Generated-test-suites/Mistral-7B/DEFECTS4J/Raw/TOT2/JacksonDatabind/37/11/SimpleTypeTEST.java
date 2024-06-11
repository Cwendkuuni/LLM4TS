package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.TypeFactory;
import org.junit.Test;

import java.lang.ClassCastException;
import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new Class[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithClassConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new Class[0], simpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithCustomParametersConstructor() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.bind(javaType), null, new JavaType[] { javaType });
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.bind(javaType), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new JavaType[] { javaType }, simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithNonClassArgument() {
        Object nonClassObject = new Object();
        try {
            new SimpleType(nonClassObject);
        } catch (IllegalArgumentException e) {
            assertEquals("Class " + nonClassObject.getClass().getName() + " not a valid Java class", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidSuperClassesAndSuperInterfaces() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        try {
            new SimpleType(stringClass, TypeBindings.emptyBindings(), javaType, new JavaType[] { javaType });
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test(expected = ClassCastException.class)
    public void testSimpleTypeWithClassArgumentThatCannotBeCastToClass() {
        Class<?> nonStringClass = Integer.TYPE;
        SimpleType simpleType = new SimpleType(nonStringClass);
        assertEquals(Integer.TYPE, simpleType.getRawClass());
        fail("Expected ClassCastException but got: " + simpleType);
    }
}