package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Integer.class, collectionType.getContentType().getRawClass());
        assertNotNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = CollectionType.construct(listType, elemT);

        // Incorrect superclass and interfaces passed to the constructor, but should not fail
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Integer.class, collectionType.getContentType().getRawClass());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType baseCollectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        Class<ArrayList<String>> stringListType = new ArrayList<String>().getClass();

        CollectionType refinedCollectionType = baseCollectionType.refine(stringListType, bindings, superClass, superInts);
        assertEquals(ArrayList.class, refinedCollectionType.getRawClass());
        assertEquals(String.class, refinedCollectionType.getContentType().getRawClass());
        assertNotNull(refinedCollectionType.getValueHandler());
        assertNull(refinedCollectionType.getTypeHandler());
        assertFalse(refinedCollectionType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        JavaType contentType = StringType.getInstance();

        CollectionType withContentType = collectionType.withContentType(contentType);
        assertEquals(List.class, withContentType.getRawClass());
        assertEquals(String.class, withContentType.getContentType().getRawClass());
        assertNotNull(withContentType.getValueHandler());
        assertNull(withContentType.getTypeHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        Object typeHandler = new Object() {};

        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertEquals(List.class, withTypeHandler.getRawClass());
        assertEquals(Integer.class, withTypeHandler.getContentType().getRawClass());
        assertNotNull(withTypeHandler.getValueHandler());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(valueHandler);
        assertEquals(List.class, withContentTypeHandler.getRawClass());
        assertEquals(Integer.class, withContentTypeHandler.getContentType().getRawClass());
        assertNotNull(withContentTypeHandler.getValueHandler());
        assertEquals(typeHandler, withContentTypeHandler.getTypeHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        Object valueHandler = new Object() {};

        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertEquals(List.class, withValueHandler.getRawClass());
        assertEquals(Integer.class, withValueHandler.getContentType().getRawClass());
        assertNotNull(withValueHandler.getValueHandler());
        assertNull(withValueHandler.getTypeHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        CollectionType withContentValueHandler = collectionType.withContentValueHandler(valueHandler);
        assertEquals(List.class, withContentValueHandler.getRawClass());
        assertEquals(Integer.class, withContentValueHandler.getContentType().getRawClass());
        assertNotNull(withContentValueHandler.getValueHandler());
        assertEquals(typeHandler, withContentValueHandler.getTypeHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listType = new ArrayList<Integer>().getClass();
        TypeBindings bindings = TypeBindings.defaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(listType, bindings, superClass, superInts, elemT);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals(List.class, withStaticTyping.getRawClass());
        assertEquals(Integer.class, withStaticTyping.getContentType().getRawClass());
        assertNotNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }
}