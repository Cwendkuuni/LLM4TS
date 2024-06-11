package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParamStash;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType(""));
    }

    @Test
    public void testCreate() {
        Class<List<String>> clazz = List.class;
        TypeVariable<String> variable = clazz.getTypeParameters()[0];
        JavaType javaType = JavaType.constructType(String.class);

        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));

        List<JavaType> types = bindings.getTypeParameters();
        assertEquals(javaType, types.get(0));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<String>> clazz = List.class;
        JavaType javaType = JavaType.constructType(String.class);

        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, javaType);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<List<String>> clazz = List.class;
        JavaType javaType = JavaType.constructType(String.class);

        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        TypeBindings unboundBindings = bindings.withUnboundVariable("T");

        assertTrue(unboundBindings.hasUnbound("T"));
    }

    @Test
    public void testFindBoundType() {
        Class<List<String>> clazz = List.class;
        JavaType javaType = JavaType.constructType(String.class);

        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertEquals(javaType, bindings.findBoundType("java.lang.String"));
        assertNull(bindings.findBoundType("Object"));
    }

    @Test
    public void testCreateWithTypeParameters() {
        Class<Map<String, List<Integer>>> clazz = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(List.class, IntType.INT_CLASS);

        TypeBindings bindings = TypeBindings.create(clazz, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List<java.lang.Integer>", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testCreateWithTypeParametersArray() {
        Class<Map<String, List<Integer>>> clazz = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(List.class, IntType.INT_CLASS);

        TypeBindings bindings = TypeBindings.create(clazz, keyType, valueType);
        TypeVariable<?>[] typeParameters = clazz.getTypeParameters();

        List<JavaType> types = bindings.getTypeParameters();
        assertEquals(2, types.size());
        assertEquals(keyType, types.get(0));
        assertEquals(valueType, types.get(1));

        String[] names = bindings.getNames();
        assertEquals(2, names.length);
        assertEquals("java.lang.String", names[0]);
        assertEquals("java.util.List<java.lang.Integer>", names[1]);

        assertEquals(keyType, bindings.findBoundType("java.lang.String"));
        assertEquals(valueType, bindings.findBoundType("java.util.List<java.lang.Integer>"));
    }

    @Test
    public void testCreateWithTypeVariable() {
        Class<List<String>> clazz = List.class;
        TypeVariable<String> variable = clazz.getTypeParameters()[0];

        TypeBindings bindings = TypeBindings.create(clazz, variable);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(variable, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTypeVariableArray() {
        Class<Map<String, List<Integer>>> clazz = Map.class;
        TypeVariable<String> keyVariable = clazz.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueVariable = clazz.getTypeParameters()[1];

        TypeBindings bindings = TypeBindings.create(clazz, keyVariable, valueVariable);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyVariable, bindings.getBoundType(0));
        assertEquals("java.util.List<java.lang.Integer>", bindings.getBoundName(1));
        assertEquals(valueVariable, bindings.getBoundType(1));
    }

    @Test
    public void testTypeParameterArray() {
        Class<Map<String, List<Integer>>> clazz = Map.class;
        TypeVariable<String> keyVariable = clazz.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueVariable = clazz.getTypeParameters()[1];

        TypeBindings bindings = TypeBindings.create(clazz, keyVariable, valueVariable);
        JavaType[] types = bindings.typeParameterArray();
        assertEquals(2, types.length);
        assertEquals(keyVariable.getType(), types[0].getType());
        assertEquals(valueVariable.getType(), types[1].getType());
    }

    private static class IntType {
        public static final Class<Integer> INT_CLASS = Integer.class;
    }
}

class ResolvedTypeTest {
    @Test
    public void testGetSelfReferencedType() {
        Class<List<List<String>>> clazz = List.class;
        TypeVariable<List<String>> variable = TypeParamStash.paramsFor2(clazz)[1];
        JavaType javaType = JavaType.constructType(List.class, variable);

        ResolvedType resolvedType = new ResolvedType(clazz, javaType);
        assertEquals(clazz, resolvedType.getRawClass());
        assertEquals(variable, resolvedType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSelfReferencedTypeNull() {
        Class<List<List<String>>> clazz = List.class;
        JavaType javaType = JavaType.constructType(List.class, String.class);

        ResolvedType resolvedType = new ResolvedType(clazz, javaType);
        assertNull(resolvedType.getSelfReferencedType());
    }
}