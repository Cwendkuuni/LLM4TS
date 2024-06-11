package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("UnknownType"));
    }

    @Test
    public void testCreateBindingsForSingleTypeParameter() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        TypeVariable<?>[] typeParams = listIntType.getTypeParameters();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(listIntType, javaType);

        assertEquals(1, bindings.size());
        assertEquals("Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("UnknownType"));
    }

    @Test
    public void testCreateBindingsForMultipleTypeParameters() {
        Class<Map<String, List<Integer>>> mapListIntType = new HashMap<String, List<Integer>>().getClass();
        TypeVariable<?>[] typeParams = mapListIntType.getTypeParameters();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(mapListIntType, keyType, valueType);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("UnknownType"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithWrongNumberOfTypeArguments() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        List<JavaType> typeArguments = new ArrayList<>();
        typeArguments.add(javaType);
        TypeBindings bindings = TypeBindings.create(listIntType, typeArguments.toArray(new JavaType[0]));
        fail("Should have thrown IllegalArgumentException with message: Can not create TypeBindings for class "
                + listIntType.getName()
                + " with 2 type parameters: class expects 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithWrongTypeArguments() {
        Class<Map<String, Integer>> mapIntType = new HashMap<String, Integer>().getClass();
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(mapIntType, keyType, listIntType, valueType);
        fail("Should have thrown IllegalArgumentException with message: Can not create TypeBindings for class "
                + mapIntType.getName()
                + " with 3 type parameters: class expects 2");
    }

    @Test
    public void testCreateIfNeededWithSingleTypeParameter() {
        Class<List<Integer>> listIntType = new ArrayList<Integer>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntType, javaType);

        assertEquals(1, bindings.size());
        assertEquals("Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("UnknownType"));
    }

    @Test
    public void testCreateIfNeededWithMultipleTypeParameters() {
        Class<Map<String, List<Integer>>> mapListIntType = new HashMap<String, List<Integer>>().getClass();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(mapListIntType, keyType, listIntType);

        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("UnknownType"));

        TypeBindings bindings2 = TypeBindings.createIfNeeded(mapListIntType, new JavaType[0]);
        assertEquals(0, bindings2.size());
    }

    @Test
    public void testCreateIfNeededWithEmptyTypeArguments() {
        Class<Map<String, List<Integer>>> mapListIntType = new HashMap<String, List<Integer>>().getClass();
        TypeBindings bindings = TypeBindings.createIfNeeded(mapListIntType, new JavaType[0]);

        assertEquals(0, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Integer>>> mapListIntType = new HashMap<String, List<Integer>>().getClass();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(mapListIntType, keyType, listIntType);

        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyType, bindings.findBoundType("java.lang.String"));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(valueType, bindings.findBoundType("java.util.List"));
        assertNull(bindings.findBoundType("UnknownType"));
    }

    @Test
    public void testHasUnbound() {
        Class<Map<String, List<Integer>>> mapListIntType = new HashMap<String, List<Integer>>().getClass();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listIntType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(mapListIntType, keyType, listIntType);

        assertFalse(bindings.hasUnbound("java.lang.String"));
        assertFalse(bindings.hasUnbound("java.util.List"));
        assertFalse(bindings.hasUnbound("Integer"));
        assertTrue(bindings.hasUnbound("UnknownType"));

        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("UnknownType");
        assertTrue(bindingsWithUnbound.hasUnbound("UnknownType"));
    }
}