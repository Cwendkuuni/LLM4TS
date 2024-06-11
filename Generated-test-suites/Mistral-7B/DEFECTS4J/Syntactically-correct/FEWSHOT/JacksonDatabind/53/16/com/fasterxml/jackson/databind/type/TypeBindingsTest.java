package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateBindingsForSingleTypeParameter() {
        JavaType typeArg = new TypeReference<Integer>() {}.getType();
        TypeBindings bindings = TypeBindings.create(String[].class, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForMultipleTypeParameters() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType(),
                new TypeReference<List<String>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(List[].class, types);
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(types[0], bindings.getBoundType(0));
        assertEquals("E", bindings.getBoundName(1));
        assertEquals(types[1], bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsForCollectionTypeParameter() {
        JavaType typeArg = new TypeReference<List<String>>() {}.getType();
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForMapTypeParameter() {
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType(),
                new TypeReference<List<String>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(List[].class, types);
        JavaType intType = new TypeReference<Integer>() {}.getType();
        JavaType listType = new TypeReference<List<String>>() {}.getType();
        assertEquals(intType, bindings.findBoundType("T"));
        assertEquals(listType, bindings.findBoundType("E"));
        assertNull(bindings.findBoundType("undefined"));
    }

    @Test
    public void testHasUnbound() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType(),
                new TypeReference<List<String>>() {}.getType() };
        TypeBindings bindings = TypeBindings.create(List[].class, types);
        assertTrue(bindings.hasUnbound("T"));
        assertTrue(bindings.hasUnbound("E"));
        assertFalse(bindings.hasUnbound("Integer"));
        assertFalse(bindings.hasUnbound("List"));
    }

    @Test
    public void testCreateBindingsForRecursiveType() {
        JavaType elementType = new TypeReference<Integer>() {}.getType();
        JavaType listType = new TypeReference<List<Integer>>() {}.getType();
        JavaType recursiveListType = new TypeReference<List<List<Integer>>>() {}.getType();
        TypeBindings bindings = TypeBindings.create(List.class, elementType, recursiveListType);
        JavaType intType = new TypeReference<Integer>() {}.getType();
        JavaType listOfListIntType = bindings.findBoundType("E");
        JavaType listOfListOfIntType = bindings.findBoundType("T");
        assertEquals(intType, listOfListIntType);
        assertEquals(listOfListOfIntType, listOfListIntType.getComponentType());
        assertEquals(listOfListOfIntType, listOfListIntType.getRawType());
        assertEquals(listType, listOfListIntType.getComponentType());
        assertEquals(intType, listType.getComponentType());
    }

    @Test
    public void testCreateBindingsForArrayType() {
        TypeBindings bindings = TypeBindings.create(int[].class, new TypeReference<Integer>() {}.getType());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(new TypeReference<Integer>() {}.getType(), bindings.getBoundType(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParameters() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType() };
        TypeBindings.create(List[].class, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParametersForList() {
        JavaType[] types = new JavaType[0];
        TypeBindings.create(List[].class, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParametersForArrayList() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType(), new TypeReference<String>() {}.getType() };
        TypeBindings.create(ArrayList.class, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParametersForMap() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType() };
        TypeBindings.create(Map.class, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParametersForHashMap() {
        JavaType[] types = new JavaType[0];
        TypeBindings.create(HashMap.class, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsForWrongNumberOfTypeParametersForLinkedHashMap() {
        JavaType[] types = new JavaType[] { new TypeReference<Integer>() {}.getType(), new TypeReference<Float>() {}.getType() };
        TypeBindings.create(LinkedHashMap.class, types);
    }
}