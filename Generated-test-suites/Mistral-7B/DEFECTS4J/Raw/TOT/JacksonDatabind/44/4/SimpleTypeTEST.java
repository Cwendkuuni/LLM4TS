package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType simpleType = TypeFactory.defaultInstance().construct(stringClass);
        Assert.assertEquals(String.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(String.class.getName(), simpleType.toCanonical().toString());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<Integer>> listClass = List.class.getClassGenericType(Integer.class);
        JavaType simpleType = TypeFactory.defaultInstance().construct(listClass);
        TypeBindings bindings = TypeBindings.map();
        JavaType simpleTypeWithBindings = new SimpleType(simpleType, bindings);
        Assert.assertEquals(List.class, simpleTypeWithBindings.getRawClass());
        Assert.assertFalse(simpleTypeWithBindings.isContainerType());
        Assert.assertEquals(List.class.getName() + '<' + Integer.class.getName() + '>', simpleTypeWithBindings.toCanonical().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForMapConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class.getClassGenericType(String.class, Integer.class);
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForCollectionConstruction() {
        Class<List<String>> listClass = List.class.getClassGenericType(String.class);
        new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForArrayConstruction() {
        Class<int[]> intArrayClass = int[].class;
        new SimpleType(intArrayClass);
    }

    @Test
    public void testNarrowingSimpleType() {
        Class<List<String>> listClass = List.class.getClassGenericType(String.class);
        JavaType simpleType = new SimpleType(listClass);
        JavaType narrowedType = simpleType.narrow(List.class);
        Assert.assertEquals(List.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNarrowingToUnrelatedType() {
        Class<List<String>> listClass = List.class.getClassGenericType(String.class);
        JavaType simpleType = new SimpleType(listClass);
        JavaType unrelatedType = TypeFactory.defaultInstance().construct(Integer.class);
        simpleType.narrow(unrelatedType);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType simpleType = new SimpleType(stringClass, null, null, null, new MyStringValueHandler(), null, false);
        Assert.assertEquals(String.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(String.class.getName(), simpleType.toCanonical().toString());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<List<Integer>> listClass = List.class.getClassGenericType(Integer.class);
        JavaType simpleType = new SimpleType(listClass, null, null, null, null, new MyListTypeHandler(), false);
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(List.class.getName() + '<' + Integer.class.getName() + '>', simpleType.toCanonical().toString());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType simpleType = new SimpleType(stringClass, null, null, null, null, null, false);
        JavaType staticTyping = simpleType.withStaticTyping();
        Assert.assertEquals(String.class, staticTyping.getRawClass());
        Assert.assertTrue(staticTyping.isStatic());
        Assert.assertEquals(String.class.getName(), staticTyping.toCanonical().toString());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<List<List<Integer>>> listListClass = List.class.getClassGenericType(List.class.getClassGenericType(Integer.class));
        JavaType simpleType = new SimpleType(listListClass, null, null, Arrays.asList(List.class, Collection.class).toArray(JavaType[].class));
        Assert.assertEquals(List.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals(List.class.getName() + '<' + List.class.getName() + '>' + '[' + Integer.class.getName() + ']', simpleType.toCanonical().toString());
    }

    @Test
    public void testSimpleTypeWithCustomCanonicalName() {
        Class<List<List<Integer>>> listListClass = List.class.getClassGenericType(List.class.getClassGenericType(Integer.class));
        JavaType simpleType = new SimpleType(listListClass, null, null, null, null, null, false) {
            @Override
            public String buildCanonicalName() {
                StringBuilder sb = new StringBuilder("[custom_name, class ");
                sb.append(super.buildCanonicalName());
                sb.append(']');
                return sb.toString();
            }
        };
        Assert.assertEquals("[custom_name, class " + listListClass.getName() + '[' + List.class.getName() + ']' + '[' + Integer.class.getName() + ']', simpleType.toCanonical().toString());
    }
}

class MyStringValueHandler {
    // empty handler for testing purposes
}

class MyListTypeHandler {
    // empty handler for testing purposes
}