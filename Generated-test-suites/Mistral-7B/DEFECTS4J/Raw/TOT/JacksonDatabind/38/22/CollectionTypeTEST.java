package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Integer.class, listType.getContentType());
        assertEquals(null, listType.getTypeHandler());
        assertEquals(false, listType.isStatic());
    }

    @Test
    public void testConstructWithBindingsDeprecated() {
        Class<List<String>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, String.class);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertEquals(null, listType.getTypeHandler());
        assertEquals(false, listType.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructWithWrongArguments() {
        CollectionType.construct(String.class, new TypeReference<Object>() {}.getType());
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType listType = CollectionType.construct(List.class, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType narrowedType = listType.narrow(ArrayList.class);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(Integer.class, narrowedType.getContentType());
        assertEquals(null, narrowedType.getTypeHandler());
        assertEquals(false, narrowedType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.withContentType(new TypeReference<String>() {}.getType());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(String.class, newListType.getContentType());
        assertEquals(null, newListType.getTypeHandler());
        assertEquals(false, newListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.withTypeHandler(new MyListTypeHandler());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(Integer.class, newListType.getContentType());
        assertEquals(MyListTypeHandler.class, newListType.getTypeHandler().getClass());
        assertEquals(false, newListType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.withContentTypeHandler(new MyContentHandler());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(Integer.class, newListType.getContentType().getContentType());
        assertEquals(MyContentHandler.class, newListType.getTypeHandler().getTypeHandler().getClass());
        assertEquals(false, newListType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.withValueHandler(new MyValueHandler());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(Integer.class, newListType.getContentType());
        assertEquals(MyValueHandler.class, newListType.getValueHandler().getClass());
        assertEquals(false, newListType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.withContentValueHandler(new MyContentValueHandler());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(Integer.class, newListType.getContentType().getContentType());
        assertEquals(MyContentValueHandler.class, newListType.getValueHandler().getValueHandler().getClass());
        assertEquals(false, newListType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.withStaticTyping();
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(List.class, newListType.getRawClass());
        assertEquals(Integer.class, newListType.getContentType());
        assertEquals(null, newListType.getTypeHandler());
        assertEquals(true, newListType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType listType = CollectionType.construct(List.class, null, null, null, new TypeReference<Integer>() {}.getType());
        JavaType newListType = listType.refine(ArrayList.class, null, null, null);
        assertEquals(ArrayList.class, newListType.getRawClass());
        assertEquals(ArrayList.class, newListType.getRawClass());
        assertEquals(Integer.class, newListType.getContentType());
        assertEquals(null, newListType.getTypeHandler());
        assertEquals(false, newListType.isStatic());
    }
}