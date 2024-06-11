package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterHandler;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeUtil;

/**
 * Test cases for TypeBindings class.
 */
public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertNull(empty.findBoundType(""));
    }

    @Test
    public void testCreateBindings() {
        List<JavaType> types = new ArrayList<>();
        Class<List<String>> listClass = List.class;
        Class<String> stringClass = String.class;
        types.add(JavaType.constructType(String.class));
        types.add(JavaType.constructType(String.class));
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertArrayEquals(new String[]{"T", " java.lang.String"}, bindings.getNames());
        assertArrayEquals(new JavaType[]{JavaType.constructType(String.class), JavaType.constructType(String.class)}, bindings.getTypes());
        assertNull(bindings.findBoundType(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsInvalidNumberOfTypes() {
        List<JavaType> types = new ArrayList<>();
        Class<List<String>> listClass = List.class;
        JavaType stringType = JavaType.constructType(String.class);
        types.add(stringType);
        TypeBindings.create(listClass, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsInvalidClass() {
        Class<List<String>> listClass = List.class;
        JavaType[] types = new JavaType[1];
        types[0] = JavaType.constructType(Object.class);
        TypeBindings.create(listClass, types);
    }

    @Test
    public void testCreateBindingsNullTypes() {
        List<JavaType> types = null;
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateBindingsEmptyTypes() {
        List<JavaType> types = new ArrayList<>();
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateBindingsOneType() {
        List<JavaType> types = new ArrayList<>();
        Class<List<String>> listClass = List.class;
        JavaType stringType = JavaType.constructType(String.class);
        types.add(stringType);
        TypeBindings bindings = TypeBindings.create(listClass, types);
        String[] expectedNames = {"T"};
        JavaType[] expectedTypes = {stringType};
        assertArrayEquals(expectedNames, bindings.getNames());
        assertArrayEquals(expectedTypes, bindings.getTypes());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsTwoTypes() {
        List<JavaType> types = new ArrayList<>();
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType integerType = JavaType.constructType(Integer.class);
        types.add(stringType);
        types.add(integerType);
        TypeBindings bindings = TypeBindings.create(mapClass, types);
        String[] expectedNames = {"K", "V"};
        JavaType[] expectedTypes = {stringType, integerType};
        assertArrayEquals(expectedNames, bindings.getNames());
        assertArrayEquals(expectedTypes, bindings.getTypes());
        assertTrue(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("V"));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<List<String>>> listListClass = List.class;
        JavaType listType = JavaType.constructType(List.class);
        JavaType stringType = JavaType.constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listListClass, listType);
        assertTrue(bindings.isEmpty());

        bindings = TypeBindings.createIfNeeded(listListClass, new JavaType[] { listType, stringType });
        String[] expectedNames = {"T", "java.lang.String"};
        JavaType[] expectedTypes = {listType, stringType};
        assertArrayEquals(expectedNames, bindings.getNames());
        assertArrayEquals(expectedTypes, bindings.getTypes());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateIfNeededEmptyTypes() {
        Class<List<List<String>>> listListClass = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(listListClass, new JavaType[0]);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededOneType() {
        Class<List<List<String>>> listListClass = List.class;
        JavaType listType = JavaType.constructType(List.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listListClass, listType);
        String[] expectedNames = {"T"};
        JavaType[] expectedTypes = {listType};
        assertArrayEquals(expectedNames, bindings.getNames());
        assertArrayEquals(expectedTypes, bindings.getTypes());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType listIntType = JavaType.constructType(List.class, JavaType.constructType(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, new JavaType[] { stringType, listIntType });
        JavaType keyType = bindings.findBoundType("java.lang.String");
        assertEquals(stringType, keyType);
        JavaType valueType = bindings.findBoundType("java.util.List<java.lang.Integer>");
        assertEquals(listIntType, valueType);
    }

    @Test
    public void testFindBoundTypeNullName() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType listIntType = JavaType.constructType(List.class, JavaType.constructType(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, new JavaType[] { stringType, listIntType });
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testFindBoundTypeUnboundType() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType listIntType = JavaType.constructType(List.class, JavaType.constructType(Integer.class));
        TypeBindings bindings = TypeBindings.create(mapClass, new JavaType[] { stringType, listIntType });
        JavaType unboundType = JavaType.constructType(String.class);
        assertNull(bindings.findBoundType(unboundType.getName()));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<Map<String, String>>>> mapClass = Map.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType mapStringStringType = JavaType.constructType(Map.class, stringType, stringType);
        TypeBindings bindings = TypeBindings.create(mapClass, new JavaType[] { mapStringStringType });
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("V");
        JavaType unboundType = JavaType.constructType(String.class);
        JavaType expectedBoundType = JavaType.constructType(Map.class, stringType, unboundType);
        String[] expectedNames = {"K", "V"};
        JavaType[] expectedTypes = {stringType, expectedBoundType};
        assertArrayEquals(expectedNames, bindingsWithUnbound.getNames());
        assertArrayEquals(expectedTypes, bindingsWithUnbound.getTypes());
        assertTrue(bindingsWithUnbound.hasUnbound("V"));
        assertNull(bindingsWithUnbound.findBoundType(""));
    }

    @Test
    public void testToString() {
        List<JavaType> types = new ArrayList<>();
        Class<Map<String, List<Map<String, String>>>> mapClass = Map.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType listStringType = JavaType.constructType(List.class, stringType);
        JavaType mapStringListStringType = JavaType.constructType(Map.class, stringType, listStringType);
        types.add(stringType);
        types.add(mapStringListStringType);
        TypeBindings bindings = TypeBindings.create(mapClass, types);
        String expected = "<Map<java.lang.String,List<Map<java.lang.String,String>>>>";
        assertEquals(expected, bindings.toString());
    }
}