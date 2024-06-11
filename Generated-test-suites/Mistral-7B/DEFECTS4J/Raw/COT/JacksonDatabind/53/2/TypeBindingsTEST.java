package com.fasterxml.jackson.databind.type; // adjust the package name if necessary

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeVariableStash;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateEmptyType() {
        TypeBindings bindings = TypeBindings.create(String[].class, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongNumberOfTypes() {
        Class<List<Integer>> listIntClass = List.class.getGenericType();
        TypeVariable<?>[] variables = listIntClass.getTypeParameters();
        JavaType[] types = new JavaType[variables.length + 1];
        TypeBindings bindings = TypeBindings.create(listIntClass, types);
    }

    @Test
    public void testCreateWithOneType() {
        Class<List<Integer>> listIntClass = List.class.getGenericType();
        TypeVariable<?>[] variables = listIntClass.getTypeParameters();
        JavaType intType = JavaType.constructCollectionType(List.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(listIntClass, intType);
        assertEquals(1, bindings.size());
        assertEquals(Integer.TYPE.getName(), bindings.getBoundName(0));
        assertEquals(intType.getTypeId(), bindings.getBoundType(0).getTypeId());
    }

    @Test
    public void testCreateWithTwoTypes() {
        Class<Map<Integer, String>> mapClass = Map.class.getGenericType();
        TypeVariable<?>[] variables = mapClass.getTypeParameters();
        JavaType intType = JavaType.constructTypeReference(Integer.TYPE, null);
        JavaType stringType = JavaType.constructTypeReference(String.class, null);
        TypeBindings bindings = TypeBindings.create(mapClass, intType, stringType);
        assertEquals(2, bindings.size());
        assertEquals(Integer.TYPE.getName(), bindings.getBoundName(0));
        assertEquals(intType.getTypeId(), bindings.getBoundType(0).getTypeId());
        assertEquals(String.class.getName(), bindings.getBoundName(1));
        assertEquals(stringType.getTypeId(), bindings.getBoundType(1).getTypeId());
    }

    @Test
    public void testCreateIfNeededWithOneType() {
        Class<List<Integer>> listIntClass = List.class.getGenericType();
        JavaType intType = JavaType.constructCollectionType(List.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntClass, intType);
        assertEquals(1, bindings.size());
        assertEquals(Integer.TYPE.getName(), bindings.getBoundName(0));
        assertEquals(intType.getTypeId(), bindings.getBoundType(0).getTypeId());
    }

    @Test
    public void testCreateIfNeededWithTwoTypes() {
        Class<Map<Integer, String>> mapClass = Map.class.getGenericType();
        JavaType intType = JavaType.constructTypeReference(Integer.TYPE, null);
        JavaType stringType = JavaType.constructTypeReference(String.class, null);
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, intType, stringType);
        assertEquals(2, bindings.size());
        assertEquals(Integer.TYPE.getName(), bindings.getBoundName(0));
        assertEquals(intType.getTypeId(), bindings.getBoundType(0).getTypeId());
        assertEquals(String.class.getName(), bindings.getBoundName(1));
        assertEquals(stringType.getTypeId(), bindings.getBoundType(1).getTypeId());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<Integer, String>> mapClass = Map.class.getGenericType();
        JavaType intType = JavaType.constructTypeReference(Integer.TYPE, null);
        JavaType stringType = JavaType.constructTypeReference(String.class, null);
        TypeBindings bindings = TypeBindings.create(mapClass, intType, stringType);
        JavaType boundType = bindings.findBoundType("Integer");
        assertEquals(intType, boundType);
        JavaType boundType2 = bindings.findBoundType("String");
        assertEquals(stringType, boundType2);
        JavaType boundType3 = bindings.findBoundType("Object");
        assertNull(boundType3);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<Integer, List<String>>> mapClass = Map.class.getGenericType();
        JavaType intType = JavaType.constructTypeReference(Integer.TYPE, null);
        JavaType listStringType = JavaType.constructCollectionType(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(mapClass, intType, listStringType);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("List");
        JavaType listType = bindingsWithUnboundVariable.findBoundType("List");
        assertTrue(listType instanceof WildcardType);
        assertEquals(WildcardType.parse("? extends List<String>"), listType);
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.create(String[].class, null);
        assertEquals("<>", bindings.toString());

        JavaType intType = JavaType.constructCollectionType(List.class, Integer.TYPE);
        bindings = TypeBindings.create(List.class, intType);
        String expected = "<I>";
        assertEquals(expected, bindings.toString());

        Class<Map<Integer, String>> mapClass = Map.class.getGenericType();
        JavaType intTypeMap = JavaType.constructTypeReference(Map.class, Integer.TYPE, String.class);
        bindings = TypeBindings.create(mapClass, intType, String.class);
        expected = "<I=>Ljava/lang/String;";
        assertEquals(expected, bindings.toString());
    }

    @Test
    public void testHashCodeEquals() {
        JavaType intType = JavaType.constructCollectionType(List.class, Integer.TYPE);
        JavaType intType2 = JavaType.constructCollectionType(ArrayList.class, Integer.TYPE);
        JavaType stringType = JavaType.constructTypeReference(String.class, null);

        TypeBindings bindings1 = TypeBindings.create(List.class, intType);
        TypeBindings bindings2 = TypeBindings.create(List.class, intType);
        TypeBindings bindings3 = TypeBindings.create(List.class, intType2);
        TypeBindings bindings4 = TypeBindings.create(ArrayList.class, intType);
        TypeBindings bindings5 = TypeBindings.create(List.class, stringType);
        TypeBindings bindings6 = TypeBindings.create(ArrayList.class, stringType);

        assertEquals(bindings1, bindings1);
        assertEquals(bindings1.hashCode(), bindings1.hashCode());
        assertEquals(bindings1, bindings2);
        assertEquals(bindings1.hashCode(), bindings2.hashCode());
        assertNotEquals(bindings1, bindings3);
        assertNotEquals(bindings1.hashCode(), bindings3.hashCode());
        assertNotEquals(bindings1, bindings4);
        assertNotEquals(bindings1.hashCode(), bindings4.hashCode());
        assertNotEquals(bindings1, bindings5);
        assertNotEquals(bindings1.hashCode(), bindings5.hashCode());
        assertNotEquals(bindings3, bindings4);
        assertNotEquals(bindings3.hashCode(), bindings4.hashCode());
        assertNotEquals(bindings3, bindings5);
        assertNotEquals(bindings3.hashCode(), bindings5.hashCode());
        assertNotEquals(bindings4, bindings5);
        assertNotEquals(bindings4.hashCode(), bindings5.hashCode());
    }

    
}