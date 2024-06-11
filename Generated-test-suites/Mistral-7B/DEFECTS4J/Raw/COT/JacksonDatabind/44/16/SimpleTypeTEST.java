package com.fasterxml.jackson.databind.type; // Assuming the package is correct

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodsWithInvalidClass() {
        new SimpleType(Map.class); // Map should not be constructible as a SimpleType
    }

    @Test
    public void testCopyConstructor() {
        SimpleType original = new SimpleType(String.class);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    @Test
    public void testConstructStaticMethod() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethod() {
        new SimpleType(String.class); // Deprecated method should not be called directly
    }

    @Test
    public void testNarrowMethodWithSameClass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowed = simpleType.narrow(String.class);
        assertEquals(simpleType, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithDifferentClass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowed = simpleType.narrow(Integer.class);
        assertNull(narrowed);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertNotNull(withHandler);
        assertEquals(simpleType._typeHandler, withHandler._typeHandler);
        assertNotSame(simpleType._typeHandler, withHandler._typeHandler);
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType._asStatic);
        assertFalse(simpleType._asStatic);
    }

    
}