package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertNotNull(listType);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertEquals(0, listType.getGenericTypes().length);
        assertEquals(false, listType.isStatic());

        JavaType setType = CollectionType.construct(Set.class, null, null, null, Integer.class);
        assertNotNull(setType);
        assertEquals(Set.class, setType.getRawClass());
        assertEquals(Integer.class, setType.getContentType());
        assertEquals(0, setType.getGenericTypes().length);
        assertEquals(false, setType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(List.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            JavaType setType = CollectionType.construct(Set.class, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType narrowedListType = listType.narrow(ArrayList.class);
        assertNotNull(narrowedListType);
        assertEquals(ArrayList.class, narrowedListType.getRawClass());
        assertEquals(String.class, narrowedListType.getContentType());
        assertEquals(0, narrowedListType.getGenericTypes().length);
        assertEquals(false, narrowedListType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withContentTypeListType = listType.withContentType(ArrayList.class);
        assertNotNull(withContentTypeListType);
        assertEquals(ArrayList.class, withContentTypeListType.getRawClass());
        assertEquals(ArrayList.class, withContentTypeListType.getContentType());
        assertEquals(0, withContentTypeListType.getGenericTypes().length);
        assertEquals(false, withContentTypeListType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withTypeHandlerListType = listType.withTypeHandler(new TypeHandler<ArrayList>() {});
        assertNotNull(withTypeHandlerListType);
        assertEquals(ArrayList.class, withTypeHandlerListType.getRawClass());
        assertEquals(String.class, withTypeHandlerListType.getContentType());
        assertEquals(0, withTypeHandlerListType.getGenericTypes().length);
        assertEquals(false, withTypeHandlerListType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withContentTypeHandlerListType = listType.withContentTypeHandler(new TypeHandler<ArrayList>() {});
        assertNotNull(withContentTypeHandlerListType);
        assertEquals(ArrayList.class, withContentTypeHandlerListType.getRawClass());
        assertEquals(ArrayList.class, withContentTypeHandlerListType.getContentType());
        assertEquals(0, withContentTypeHandlerListType.getGenericTypes().length);
        assertEquals(false, withContentTypeHandlerListType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withValueHandlerListType = listType.withValueHandler(new ValueHandlerWithInitialValue<String>() {});
        assertNotNull(withValueHandlerListType);
        assertEquals(List.class, withValueHandlerListType.getRawClass());
        assertEquals(String.class, withValueHandlerListType.getContentType());
        assertEquals(0, withValueHandlerListType.getGenericTypes().length);
        assertEquals(false, withValueHandlerListType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withContentValueHandlerListType = listType.withContentValueHandler(new ValueHandlerWithInitialValue<String>() {});
        assertNotNull(withContentValueHandlerListType);
        assertEquals(List.class, withContentValueHandlerListType.getRawClass());
        assertEquals(String.class, withContentValueHandlerListType.getContentType());
        assertEquals(0, withContentValueHandlerListType.getGenericTypes().length);
        assertEquals(false, withContentValueHandlerListType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType withStaticTypingListType = listType.withStaticTyping();
        assertNotNull(withStaticTypingListType);
        assertEquals(List.class, withStaticTypingListType.getRawClass());
        assertEquals(String.class, withStaticTypingListType.getContentType());
        assertEquals(0, withStaticTypingListType.getGenericTypes().length);
        assertEquals(true, withStaticTypingListType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType refinedListType = listType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedListType);
        assertEquals(ArrayList.class, refinedListType.getRawClass());
        assertEquals(String.class, refinedListType.getContentType());
        assertEquals(0, refinedListType.getGenericTypes().length);
        assertEquals(false, refinedListType.isStatic());
    }
}