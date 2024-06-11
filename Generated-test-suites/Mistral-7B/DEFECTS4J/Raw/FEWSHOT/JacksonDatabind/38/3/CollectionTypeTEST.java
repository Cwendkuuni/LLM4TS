package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertNotNull(listType);
        assertEquals("java.util.List", listType.getClass().getName());
        assertEquals(String.class, listType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullRawType() {
        CollectionType.construct(null, null, null, null, String.class);
    }

    @Test
    public void testConstructWithBindings() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class, null, null, false);
        assertNotNull(listType);
        assertEquals("java.util.List", listType.getClass().getName());
        assertEquals(String.class, listType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType listType = CollectionType.construct(List.class, String.class);
        assertNotNull(listType);
        assertEquals("java.util.List", listType.getClass().getName());
        assertEquals(String.class, listType.getContentType());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType narrowedType = listType._narrow(ArrayList.class);
        assertNotNull(narrowedType);
        assertEquals("java.util.ArrayList", narrowedType.getClass().getName());
        assertEquals(String.class, narrowedType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithIntType = listType.withContentType(Integer.TYPE);
        assertNotNull(listWithIntType);
        assertEquals("java.util.List", listWithIntType.getClass().getName());
        assertNotNull(listWithIntType.getContentType());
        assertEquals(Integer.TYPE, listWithIntType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithHandler = listType.withTypeHandler(new MyTypeHandler());
        assertNotNull(listWithHandler);
        assertEquals("java.util.List", listWithHandler.getClass().getName());
        assertEquals(String.class, listWithHandler.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithHandler = listType.withContentTypeHandler(new MyContentTypeHandler());
        assertNotNull(listWithHandler);
        assertEquals("java.util.List", listWithHandler.getClass().getName());
        assertNotNull(listWithHandler.getContentType());
        assertEquals(String.class, listWithHandler.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithHandler = listType.withValueHandler(new MyValueHandler());
        assertNotNull(listWithHandler);
        assertEquals("java.util.List", listWithHandler.getClass().getName());
        assertEquals(String.class, listWithHandler.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listWithHandler = listType.withContentValueHandler(new MyContentValueHandler());
        assertNotNull(listWithHandler);
        assertEquals("java.util.List", listWithHandler.getClass().getName());
        assertNotNull(listWithHandler.getContentType());
        assertEquals(String.class, listWithHandler.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType staticListType = listType.withStaticTyping();
        assertNotNull(staticListType);
        assertTrue(staticListType._asStatic);
        assertEquals("java.util.List", staticListType.getClass().getName());
        assertEquals(String.class, staticListType.getContentType());
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType refinedListType = listType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedListType);
        assertEquals("java.util.ArrayList", refinedListType.getClass().getName());
        assertEquals(String.class, refinedListType.getContentType());
    }

    // Add more test cases if needed

    static class MyTypeHandler {}
    static class MyContentTypeHandler {}
    static class MyValueHandler {}
    static class MyContentValueHandler {}
}