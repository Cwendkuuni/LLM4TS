package com.example; // Replace with your test package

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterized;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertNull(typeBindings._names);
        assertArrayEquals(new JavaType[0], typeBindings._types);
        assertNull(typeBindings._unboundVariables);
        assertEquals(0, typeBindings._hashCode);
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<List<String>> erasedType = List.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1);
        String[] names = typeBindings._names;
        JavaType[] types = typeBindings._types;
        String[] unboundVariables = typeBindings._unboundVariables;
        assertEquals(1, names.length);
        assertEquals("T", names[0]);
        assertEquals(typeArg1, types[0]);
        assertNull(unboundVariables);
        assertEquals(1, typeBindings._hashCode);
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        Class<Map<String, List<Integer>>> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        String[] names = typeBindings._names;
        JavaType[] types = typeBindings._types;
        String[] unboundVariables = typeBindings._unboundVariables;
        assertEquals(2, names.length);
        assertEquals("K", names[0]);
        assertEquals("V", names[1]);
        assertEquals(typeArg1, types[0]);
        assertEquals(typeArg2, types[1]);
        assertNull(unboundVariables);
        assertEquals(2, typeBindings._hashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArgs() {
        Class<?> erasedType = Object.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType[] types = new JavaType[] { typeArg1 };
        TypeBindings typeBindings = TypeBindings.create(erasedType, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidTypeArgs() {
        Class<Number> erasedType = Number.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType[] types = new JavaType[] { typeArg1 };
        TypeBindings typeBindings = TypeBindings.create(erasedType, types);
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Integer>>> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType boundType = typeBindings.findBoundType("K");
        assertSame(typeArg1, boundType);
        boundType = typeBindings.findBoundType("V");
        assertSame(typeArg2, boundType);
        boundType = typeBindings.findBoundType("X");
        assertNull(boundType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindBoundTypeWithUnresolvedType() {
        Class<Map<String, List<Integer>>> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class);
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType unresolvedType = new TypeParameterized(Collections.emptyMap().getClass(), "W");
        typeBindings.withUnboundVariable("W");
        JavaType boundType = typeBindings.findBoundType("W");
    }

    @Test
    public void testTypeBindingsWithUnboundVariable() {
        Class<Map<String, List<Integer>>> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        TypeBindings typeBindingsWithUnboundVariable = typeBindings.withUnboundVariable("W");
        JavaType boundType = typeBindingsWithUnboundVariable.findBoundType("W");
        assertNull(boundType);
        TypeVariable<?> unresolvedType = new TypeVariable<Object>("W", null);
        assertEquals(unresolvedType, typeBindingsWithUnboundVariable._unboundVariables[0]);
        assertEquals(typeBindingsWithUnboundVariable._hashCode, typeBindings._hashCode + typeBindingsWithUnboundVariable._unboundVariables[0].getGenericDeclaration().getTypeParameters().length);
    }

    @Test
    public void testToString() {
        Class<Map<String, List<Integer>>> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertSame("<Map<String,List<Integer>>>", typeBindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        Class<Map<String, List<Integer>>> erasedType = Map.class;
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings typeBindings1 = TypeBindings.create(erasedType, typeArg1, typeArg2);
        JavaType typeArg1_1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2_1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings typeBindings2 = TypeBindings.create(erasedType, typeArg1_1, typeArg2_1);
        JavaType typeArg1_2 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType typeArg2_2 = TypeFactory.defaultInstance().constructType(List.class, Integer[].class);
        TypeBindings typeBindings3 = TypeBindings.create(erasedType, typeArg1_2, typeArg2_2);
        assertEquals(typeBindings1, typeBindings1);
        assertEquals(typeBindings1, typeBindings1);
        assertNotEquals(typeBindings1, typeBindings2);
        assertNotEquals(typeBindings1, typeBindings3);
    }

    
}