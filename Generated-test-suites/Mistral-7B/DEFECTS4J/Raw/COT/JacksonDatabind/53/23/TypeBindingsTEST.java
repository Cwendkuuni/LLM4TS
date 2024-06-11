package com.fasterxml.jackson.databind.type; // Make sure to import necessary classes

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeVariableStash;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateEmpty() {
        Class<?> erasedType = List.class;
        TypeBindings bindings = TypeBindings.create(erasedType, Collections.emptyList());
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<?> erasedType = ArrayList.class;
        JavaType typeArg = JavaType.constructParametricType(ArrayList.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertSame(TypeVariable.valueOf("T"), bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg2 = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertSame(TypeVariable.valueOf("K"), bindings.getBoundType(0));
        assertSame(TypeVariable.valueOf("V"), bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeededWithOneTypeArg() {
        Class<?> erasedType = ArrayList.class;
        JavaType typeArg = JavaType.constructParametricType(ArrayList.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertSame(TypeVariable.valueOf("T"), bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArgs() {
        Class<?> erasedType = Map.class;
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(Map.class, String.class, Integer.class),
                JavaType.constructParametricType(Map.class, Integer.class, String.class) };
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertSame(TypeVariable.valueOf("K"), bindings.getBoundType(0));
        assertSame(TypeVariable.valueOf("V"), bindings.getBoundType(1));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg2 = JavaType.constructParametricType(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        assertEquals(3, bindingsWithUnboundVariable.size());
        assertTrue(bindingsWithUnboundVariable.hasUnbound("X"));
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg2 = JavaType.constructParametricType(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertSame(String.class, bindings.findBoundType("K"));
        assertSame(Integer.class, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg2 = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        String expected = "<K: java.lang.String, V: java.lang.Integer>";
        String actual = bindings.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCodeAndEquals() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg2 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg3 = JavaType.constructParametricType(Map.class, Integer.class, String.class);

        TypeBindings bindings1 = TypeBindings.create(erasedType, typeArg1, typeArg2);
        TypeBindings bindings2 = TypeBindings.create(erasedType, typeArg1, typeArg2);
        TypeBindings bindings3 = TypeBindings.create(erasedType, typeArg1, typeArg3);

        assertSame(bindings1, bindings1);
        assertSame(bindings1, bindings2);
        assertFalse(bindings1.equals(null));
        assertFalse(bindings1.equals(""));
        assertFalse(bindings1.equals(new Object()));
        assertFalse(bindings1.equals(bindings3));

        JavaType[] types = new JavaType[] { typeArg1, typeArg2, typeArg3 };
        TypeBindings bindings4 = TypeBindings.createIfNeeded(erasedType, types);
        assertSame(bindings1, bindings4);
    }

    @Test
    public void testTypeParameterArray() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType typeArg2 = JavaType.constructParametricType(Map.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType[] types = bindings.typeParameterArray();
        assertArrayEquals(new JavaType[] { typeArg1, typeArg2 }, types);
    }

    
}