package com.example.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertNotNull(empty);
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullErasedType() {
        List<JavaType> types = new ArrayList<>();
        TypeBindings bindings = TypeBindings.create(null, types.toArray(new JavaType[0]));
    }

    @Test
    public void testCreateWithEmptyTypeList() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = TypeBindings.create(erasedType, Collections.emptyList());
        assertNotNull(bindings);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithSingleTypeArgument() {
        Class<?> erasedType = List.class;
        JavaType typeArg = JavaType.constructParametricType(ArrayList.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg);
        assertNotNull(bindings);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String.class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertNotNull(bindings);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(typeArg2, bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeededWithEmptyTypeList() {
        Class<?> erasedType = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertNotNull(bindings);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithSingleTypeArgument() {
        Class<?> erasedType = List.class;
        JavaType typeArg = JavaType.constructParametricType(ArrayList.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg);
        assertNotNull(bindings);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String.class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg1, typeArg2);
        assertNotNull(bindings);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(typeArg2, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType[] types = new JavaType[3];
        TypeBindings bindings = TypeBindings.create(erasedType, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidTypeArgumentTypes() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String[].class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
    }

    @Test
    public void testFindBoundTypeWithBoundType() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String.class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType expected = typeArg1;
        JavaType actual = bindings.findBoundType("java.lang.Integer");
        assertEquals(expected, actual);
    }

    @Test
    public void testFindBoundTypeWithUnboundType() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String.class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType actual = bindings.findBoundType("java.lang.String");
        assertNull(actual);
    }

    @Test
    public void testFindBoundTypeWithInvalidName() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String.class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType actual = bindings.findBoundType("java.util.ArrayList");
        assertNull(actual);
    }

    @Test
    public void testWithUnboundVariable() {
        String[] unboundVariables = new String[]{"T"};
        TypeBindings bindings = new TypeBindings(new String[]{"A"}, new JavaType[0], unboundVariables);
        TypeBindings newBindings = bindings.withUnboundVariable("T");
        String[] newUnboundVariables = newBindings.getUnboundVariables();
        assertEquals(2, newUnboundVariables.length);
        assertArrayEquals(new String[]{"A", "T"}, newBindings.getNames());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(HashMap.class, Integer.TYPE, String.class);
        JavaType typeArg2 = JavaType.constructParametricType(String.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        String expected = "<Map<Integer,String>>";
        String actual = bindings.toString();
        assertEquals(expected, actual);
    }

    
}