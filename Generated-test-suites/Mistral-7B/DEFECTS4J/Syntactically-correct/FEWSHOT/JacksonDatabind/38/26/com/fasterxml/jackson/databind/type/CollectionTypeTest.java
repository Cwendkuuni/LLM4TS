package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, String.class);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getContentType());
        assertNull(collectionType.getSuperclass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(String.class, collectionType.getElementType());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, String.class);
        assertEquals(List.class, collectionType.getRawClass());
        assertNull(collectionType.getContentType());
        assertNull(collectionType.getSuperclass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(String.class, collectionType.getElementType());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<ArrayList<String>> arrayListType = ArrayList.class;
        CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType arrayListTypeWrapper = new CollectionType(arrayListType, null);
        CollectionType narrowedType = listType._narrow(ArrayList.class);
        assertEquals(arrayListTypeWrapper, narrowedType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowUnsupported() {
        Class<ArrayList<String>> arrayListType = ArrayList.class;
        CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        listType._narrow(Object.class);
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listType = new ArrayList<Integer>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, Integer.TYPE);
        CollectionType withContentType = collectionType.withContentType(Integer.class);
        assertEquals(List.class, withContentType.getRawClass());
        assertEquals(Integer.class, withContentType.getContentType());
        assertNull(withContentType.getSuperclass());
        assertNull(withContentType.getSuperInterfaces());
        assertEquals(Integer.TYPE, withContentType.getElementType());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, String.class);
        CollectionType withTypeHandler = collectionType.withTypeHandler(new Object() {});
        assertEquals(List.class, withTypeHandler.getRawClass());
        assertNull(withTypeHandler.getContentType());
        assertNull(withTypeHandler.getSuperclass());
        assertNull(withTypeHandler.getSuperInterfaces());
        assertEquals(String.class, withTypeHandler.getElementType());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, String.class);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(new Object() {});
        assertEquals(List.class, withContentTypeHandler.getRawClass());
        assertEquals(String.class, withContentTypeHandler.getContentType());
        assertNull(withContentTypeHandler.getSuperclass());
        assertNull(withContentTypeHandler.getSuperInterfaces());
        assertEquals(String.class, withContentTypeHandler.getElementType());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, String.class);
        CollectionType withValueHandler = collectionType.withValueHandler(new Object() {});
        assertEquals(List.class, withValueHandler.getRawClass());
        assertNull(withValueHandler.getContentType());
        assertNull(withValueHandler.getSuperclass());
        assertNull(withValueHandler.getSuperInterfaces());
        assertEquals(String.class, withValueHandler.getElementType());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, String.class);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(new Object() {});
        assertEquals(List.class, withContentValueHandler.getRawClass());
        assertEquals(String.class, withContentValueHandler.getContentType());
        assertNull(withContentValueHandler.getSuperclass());
        assertNull(withContentValueHandler.getSuperInterfaces());
        assertEquals(String.class, withContentValueHandler.getElementType());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<String>> listType = new ArrayList<>() {}.getClass();
        CollectionType collectionType = CollectionType.construct(listType, null, null, null, String.class);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals(List.class, withStaticTyping.getRawClass());
        assertNull(withStaticTyping.getContentType());
        assertNull(withStaticTyping.getSuperclass());
        assertNull(withStaticTyping.getSuperInterfaces());
        assertEquals(String.class, withStaticTyping.getElementType());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<ArrayList<String>> arrayListType = ArrayList.class;
        CollectionType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        CollectionType refinedType = listType.refine(arrayListType.getClass(), null, null, null);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType());
        assertNull(refinedType.getSuperclass());
        assertNull(refinedType.getSuperInterfaces());
        assertEquals(String.class, refinedType.getElementType());
        assertFalse(refinedType.isStatic());
    }
}