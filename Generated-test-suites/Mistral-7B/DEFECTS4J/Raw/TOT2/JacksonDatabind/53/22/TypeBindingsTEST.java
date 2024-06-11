package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testOneTypeParameter() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        TypeBindings bindings = TypeBindings.create(listIntType, JavaType.constructParametricType(List.class, Integer.class));
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(JavaType.constructParametricType(List.class, Integer.class), bindings.getBoundType(0));
        assertNull(bindings.findBoundType("java.lang.String"));
    }

    @Test
    public void testTwoTypeParameters() {
        Class<Map<Integer, String>> mapIntStringType = Map.of(Integer.class, String.class).getType();
        TypeBindings bindings = TypeBindings.create(mapIntStringType,
                JavaType.constructParametricType(Map.class, Integer.class, String.class));
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(JavaType.constructParametricType(Map.class, Integer.class, String.class), bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(JavaType.constructParametricType(Map.class, Integer.class, String.class), bindings.getBoundType(1));
        assertNull(bindings.findBoundType("java.util.List"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeParameters() {
        Class<Map<Integer, String>> mapIntStringType = Map.of(Integer.class, String.class).getType();
        TypeBindings bindings = TypeBindings.create(mapIntStringType,
                JavaType.constructParametricType(Map.class, Integer.class, String.class),
                JavaType.constructParametricType(Map.class, Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypesForNumberOfTypeParameters() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        TypeBindings bindings = TypeBindings.create(listIntType, new JavaType[] {String.class, Integer.class});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypesForNumberOfTypeParameters2() {
        Class<Map<Integer, String>> mapIntStringType = Map.of(Integer.class, String.class).getType();
        TypeBindings bindings = TypeBindings.create(mapIntStringType, JavaType.constructParametricType(List.class, Integer.class));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<List<Integer>, List<String>>> mapListListStringType = Map.of(List.class, List.class, String.class).getType();
        TypeBindings bindings = TypeBindings.create(mapListListStringType,
                JavaType.constructParametricType(Map.class, List.class, List.class, String.class));
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(JavaType.constructParametricType(Map.class, List.class, List.class, String.class), bindings.getBoundType(0));
        assertEquals(JavaType.constructParametricType(Map.class, List.class, List.class, String.class), bindings.getBoundType(1));
        assertEquals(JavaType.constructParametricType(List.class, Integer.class), bindings.findBoundType("java.util.List"));
        assertNull(bindings.findBoundType("java.lang.String"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        assertTrue(bindings1.isEmpty());

        TypeBindings bindings2 = TypeBindings.create(String[].class, String[].class);
        assertFalse(bindings2.isEmpty());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, JavaType.constructParametricType(List.class, String.class));
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("T");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("T"));
    }
}