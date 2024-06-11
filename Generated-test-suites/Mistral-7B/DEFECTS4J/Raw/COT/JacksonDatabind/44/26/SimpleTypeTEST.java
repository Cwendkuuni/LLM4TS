package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory; // for TypeFactory.unknownType()

public class SimpleTypeTest {

    

    // Test constructor with Class argument
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperclass());
        assertEquals(null, simpleType.getSuperInterfaces());
    }

    // Test copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    // Test constructUnsafe method
    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperclass());
        assertEquals(null, simpleType.getSuperInterfaces());
    }

    // Test construct method (deprecated)
    @Test
    public void testSimpleTypeConstructDeprecated() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        assertEquals(String.class, simpleType.getClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperclass());
        assertEquals(null, simpleType.getSuperInterfaces());
    }

    // Test narrow method
    @Test
    public void testSimpleTypeNarrow() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType intType = new SimpleType(intClass);

        SimpleType narrowed = stringType.narrow(intClass);
        assertNotEquals(stringType, narrowed);
        assertEquals(intClass, narrowed.getClass());
        assertEquals(TypeBindings.emptyBindings(), narrowed.getBindings());
        assertEquals(null, narrowed.getSuperclass());
        assertEquals(null, narrowed.getSuperInterfaces());
    }

    // Test withContentType method
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(JavaType.construct(List.class));
    }

    // Test withTypeHandler method
    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(handler);
        assertNotEquals(simpleType, newSimpleType);
        assertEquals(handler, newSimpleType.getTypeHandler());
    }

    // Test withStaticTyping method
    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType staticType = simpleType.withStaticTyping();
        assertNotEquals(simpleType, staticType);
        assertTrue(staticType.isStatic());
    }

    
}