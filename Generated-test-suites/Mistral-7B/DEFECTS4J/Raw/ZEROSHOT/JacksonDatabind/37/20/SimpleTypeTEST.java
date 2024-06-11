package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.constructUnsafe(intClass);
        JavaType actual = intType.getRawClass();
        JavaType expected = JavaType.constructType(intClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<Map<String, Integer>> mapClass = Map.class.cast(HashMap.class);
        SimpleType mapType = SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<List<String>> listClass = List.class.cast(ArrayList.class);
        SimpleType listType = SimpleType.construct(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<int[]> intArrayClass = int[].class;
        SimpleType intArrayType = SimpleType.construct(intArrayClass);
    }

    @Test
    public void testNarrow() {
        Class<Integer> intClass = Integer.class;
        Class<Long> longClass = Long.class;
        SimpleType intType = SimpleType.construct(intClass);
        JavaType narrowed = intType.narrow(longClass);
        JavaType expected = SimpleType.construct(longClass);
        assertEquals(expected, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        SimpleType intType = SimpleType.construct(intClass);
        intType.narrow(stringClass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.construct(stringClass);
        Object stringHandler = new Object() {};
        JavaType resultingType = stringType.withTypeHandler(stringHandler);
        JavaType expected = SimpleType.construct(stringClass, null, null, null, null, stringHandler, false);
        assertEquals(expected, resultingType);
    }

    @Test
    public void testIsContainerType() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.construct(intClass);
        boolean result = intType.isContainerType();
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<Map<String, List<Integer>>> mapListType = Map.class.cast(HashMap.class).cast(Map.class).cast(Hashtable.class)
                .cast(Map.class).cast(LinkedHashMap.class).cast(Map.class)
                .cast(Map.class).cast(HashMap.class).cast(Map.class)
                .cast(Map.class).cast(HashMap.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType mapListTypeSimple = new SimpleType(mapListType, bindings, null, null);
        String canonicalName = mapListTypeSimple.buildCanonicalName();
        String expected = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
        assertEquals(expected, canonicalName);
    }

    @Test
    public void testToString() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.construct(intClass);
        String result = intType.toString();
        String expected = "[simple type, class [Ljava.lang.Class;@15db9742]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = SimpleType.construct(intClass);
        SimpleType intType2 = SimpleType.construct(intClass);
        boolean result = intType1.equals(intType2);
        boolean expected = true;
        assertEquals(expected, result);
    }

    @Test
    public void testNotEquals() {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        SimpleType intType = SimpleType.construct(intClass);
        SimpleType stringType = SimpleType.construct(stringClass);
        boolean result = intType.equals(stringType);
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.construct(intClass);
        StringBuilder result = intType.getErasedSignature(new StringBuilder());
        StringBuilder expected = new StringBuilder("[I");
        assertEquals(expected, result);
    }

    @Test
    public void testGetGenericSignature() {
        Class<Map<String, List<Integer>>> mapListType = Map.class.cast(HashMap.class).cast(Map.class).cast(Hashtable.class)
                .cast(Map.class).cast(LinkedHashMap.class).cast(Map.class)
                .cast(Map.class).cast(HashMap.class).cast(Map.class)
                .cast(Map.class).cast(HashMap.class);
        SimpleType mapListTypeSimple = new SimpleType(mapListType, TypeBindings.emptyBindings(), null, null);
        StringBuilder result = mapListTypeSimple.getGenericSignature(new StringBuilder());
        StringBuilder expected = new StringBuilder("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = SimpleType.construct(intClass);
        SimpleType intType2 = SimpleType.construct(intClass);
        int hashCode1 = intType1.hashCode();
        int hashCode2 = intType2.hashCode();
        int expected = intType1.getClass().hashCode();
        assertEquals(expected, hashCode1);
        assertEquals(expected, hashCode2);
    }
}