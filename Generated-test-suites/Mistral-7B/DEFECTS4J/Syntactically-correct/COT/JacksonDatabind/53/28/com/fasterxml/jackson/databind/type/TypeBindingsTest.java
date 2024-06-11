package com.fasterxml.jackson.databind.type; // Import necessary packages

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeVariableStash; // Assuming the TypeVariableStash is imported from the same package

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertNull(bindings.findBoundType("name"));
    }

    @Test
    public void testCreateWithNoTypeParameter() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = TypeBindings.create(erasedType, null);
        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"T"}, bindings._names);
        assertArrayEquals(new JavaType[] { JavaType.constructRawType(String[].class) }, bindings._types);
    }

    @Test
    public void testCreateWithOneTypeParameter() {
        Class<?> erasedType = ArrayList.class;
        JavaType typeArg1 = JavaType.constructType(int.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"E"}, bindings._names);
        assertArrayEquals(new JavaType[] { typeArg1 }, bindings._types);
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"K", "V"}, bindings._names);
        assertArrayEquals(new JavaType[] { typeArg1, typeArg2 }, bindings._types);
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameter() {
        Class<?> erasedType = Map.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameter() {
        Class<?> erasedType = ArrayList.class;
        JavaType typeArg1 = JavaType.constructType(int.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg1);
        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"E"}, bindings._names);
        assertArrayEquals(new JavaType[] { typeArg1 }, bindings._types);
    }

    @Test
    public void testCreateIfNeededWithTwoTypeParameters() {
        Class<?> erasedType = HashMap.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg1, typeArg2);
        assertFalse(bindings.isEmpty());
        assertArrayEquals(new String[] {"K", "V"}, bindings._names);
        assertArrayEquals(new JavaType[] { typeArg1, typeArg2 }, bindings._types);
    }

    @Test
    public void testCreateWithUnboundVariables() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        String name = "key";
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2).withUnboundVariable(name);
        assertTrue(bindings.hasUnbound(name));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        TypeBindings.create(erasedType, typeArg1, typeArg2, new JavaType[] { JavaType.constructType(Long.class) });
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType expectedType = JavaType.constructType(String.class);
        assertSame(expectedType, bindings.findBoundType("K"));
        expectedType = JavaType.constructType(Integer.class);
        assertSame(expectedType, bindings.findBoundType("V"));
    }

    @Test
    public void testFindBoundTypeWhenTypeParameterIsUnbound() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        String name = "key";
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2).withUnboundVariable(name);
        assertNull(bindings.findBoundType(name));
    }

    @Test
    public void testSize() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        int size = bindings.size();
        int expectedSize = 2;
        assertEquals("Unexpected size", expectedSize, size);
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructType(String.class);
        JavaType typeArg2 = JavaType.constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        String expectedString = "<K, V>";
        String actualString = bindings.toString();
        assertEquals("Unexpected toString", expectedString, actualString);
    }

    

    private void assertEquals(String message, int expected, int actual) {
        if (expected == actual) {
            return;
        }
        fail(message + " Expected: " + expected + " Actual: " + actual);
    }
}