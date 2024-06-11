package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForCollection() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, listType);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(listType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForMap() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, mapType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(String.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(Integer.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("W"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForInvalidNumberOfTypeParameters() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class, String.class);
        TypeBindings bindings = TypeBindings.create(List.class, listType);
        // Should throw an IllegalArgumentException
    }

    @Test
    public void testCreateBindingsForLambdaType() {
        Class<?> lambdaClass = TypeBindings.class;
        JavaType lambdaType = TypeFactory.defaultInstance().constructType(lambdaClass, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(lambdaClass, lambdaType);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(String.class, bindings.getBoundType(0));
        assertEquals("U", bindings.getBoundName(1));
        assertSame(Integer.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("V"));
    }

    @Test
    public void testCreateBindingsForGenericClassWithNoTypeArguments() {
        JavaType abstractListType = TypeFactory.defaultInstance().constructType(AbstractList.class);
        TypeBindings bindings = TypeBindings.create(AbstractList.class, abstractListType);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsForGenericClassWithSingleTypeArgument() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(ArrayList.class, listType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertSame(Integer.class, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForGenericClassWithMultipleTypeArguments() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(LinkedHashMap.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(LinkedHashMap.class, mapType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(String.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(Integer.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("W"));
    }

    @Test
    public void testFindBoundTypeForUnboundVariable() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, listType);
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testFindBoundTypeForBoundVariable() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, listType);
        assertEquals(Integer.class, bindings.findBoundType("T"));
    }

    @Test
    public void testFindBoundTypeForBoundVariableWithGenericType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, List.class);
        TypeBindings bindings = TypeBindings.create(List.class, listType);
        JavaType listElementType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        assertEquals(listElementType, bindings.findBoundType("T"));
    }

    @Test
    public void testFindBoundTypeForRecursivelyBoundVariable() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class, List.class);
        TypeBindings bindings = TypeBindings.create(ArrayList.class, listType);
        TypeBindings subBindings = bindings.withUnboundVariable("T");
        JavaType listElementType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        assertEquals(listElementType, subBindings.findBoundType("T"));
    }

    @Test
    public void testCreateBindingsForNullTypeArguments() {
        TypeBindings bindings = TypeBindings.create(List.class, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsForEmptyTypeArgumentsList() {
        TypeBindings bindings = TypeBindings.create(List.class, Collections.emptyList());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsForSingleTypeArgumentArray() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { listType });
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(listType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForMultipleTypeArgumentArray() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, new JavaType[] { String.class, mapType });
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertSame(String.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(mapType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("W"));
    }

    @Test
    public void testCreateBindingsForMixedTypeArgumentTypes() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { listType, mapType });
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(Integer.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(mapType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForMixedTypeArgumentArrays() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer[].class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String[].class, Integer[].class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { listType, mapType });
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(Integer[].class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertSame(mapType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForArrayTypeArgument() {
        JavaType arrayType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { arrayType });
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(int[].class, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForGenericArrayTypeArgument() {
        JavaType arrayType = TypeFactory.defaultInstance().constructType(Integer[].class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { arrayType });
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(Integer[].class, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForMixedGenericArrayTypeArguments() {
        JavaType arrayTypeInt = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType arrayTypeString = TypeFactory.defaultInstance().constructType(String[].class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { arrayTypeInt, arrayTypeString });
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(int[].class, bindings.getBoundType(0));
        assertEquals("U", bindings.getBoundName(1));
        assertSame(String[].class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("V"));
    }

    @Test
    public void testCreateBindingsForAbstractClassTypeArgument() {
        JavaType abstractListType = TypeFactory.defaultInstance().constructType(AbstractList.class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { abstractListType });
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(AbstractList.class, bindings.getBoundType(0).getRawClass());
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testCreateBindingsForInterfaceTypeArgument() {
        JavaType iterableType = TypeFactory.defaultInstance().constructType(Iterable.class);
        TypeBindings bindings = TypeBindings.create(List.class, new JavaType[] { iterableType });
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertSame(Iterable.class, bindings.getBoundType(0).getRawClass());
        assertNull(bindings.findBoundType("U"));
    }
}