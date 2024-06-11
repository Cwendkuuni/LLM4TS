package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    private static JavaType createSimpleType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    @Test
    public void testConstructSimpleType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = (SimpleType) createSimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testConstructSimpleTypeWithBindings() {
        Class<List<Integer>> listClass = List.class.getClassGenType(Integer.class);
        TypeBindings bindings = TypeBindings.builder()
                .bindValueType(List.class, new SimpleType(List.class))
                .build();
        SimpleType simpleType = (SimpleType) createSimpleType(listClass, bindings);
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testConstructSimpleTypeWithSuperClass() {
        Class<List<Integer>> listClass = List.class.getClassGenType(Integer.class);
        JavaType superClass = createSimpleType(ArrayList.class);
        SimpleType simpleType = (SimpleType) createSimpleType(listClass, null, (JavaType) superClass);
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(ArrayList.class, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testConstructSimpleTypeWithSuperInterfaces() {
        Class<List<Integer>> listClass = List.class.getClassGenType(Integer.class);
        JavaType[] superInterfaces = new JavaType[]{createSimpleType(Collection.class)};
        SimpleType simpleType = (SimpleType) createSimpleType(listClass, null, null, superInterfaces);
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(false, simpleType.isStatic());
    }

    @Test
    public void testConstructSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType sourceSimpleType = (SimpleType) createSimpleType(stringClass);
        SimpleType targetSimpleType = new SimpleType(sourceSimpleType);
        assertEquals(String.class, targetSimpleType.getRawClass());
        assertEquals(sourceSimpleType.getBindings(), targetSimpleType.getBindings());
        assertEquals(sourceSimpleType.getSuperClass(), targetSimpleType.getSuperClass());
        assertEquals(sourceSimpleType.getSuperInterfaces(), targetSimpleType.getSuperInterfaces());
        assertEquals(sourceSimpleType.isStatic(), targetSimpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeSimpleType() {
        createSimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithArrayClass() {
        Class<int[]> intArrayClass = int[].class;
        createSimpleType(intArrayClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithCollectionClass() {
        Class<List<String>> stringListClass = List.class.getClassGenType(String.class);
        createSimpleType(stringListClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithMapClass() {
        Class<Map<String, Integer>> stringIntegerMapClass = Map.class.getClassGenType(String.class, Integer.class);
        createSimpleType(stringIntegerMapClass);
    }
}