package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertNull(listType.getRawType());
        assertNull(listType.getRawClass().getSuperclass());
        assertNull(listType.getSuperClass());
        assertArrayEquals(new Class[]{}, listType.getSuperInterfaces());
        assertNull(listType.getValueHandler());
        assertNull(listType.getTypeHandler());
        assertFalse(listType.isStatic());

        JavaType setType = CollectionType.construct(Set.class, null, null, null, String.class);
        assertEquals(Set.class, setType.getRawClass());
        assertEquals(String.class, setType.getContentType());
        assertNull(setType.getRawType());
        assertNull(setType.getRawClass().getSuperclass());
        assertNull(setType.getSuperClass());
        assertArrayEquals(new Class[]{}, setType.getSuperInterfaces());
        assertNull(setType.getValueHandler());
        assertNull(setType.getTypeHandler());
        assertFalse(setType.isStatic());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithBindings = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        assertEquals(List.class, listTypeWithBindings.getRawClass());
        assertEquals(String.class, listTypeWithBindings.getContentType());
        assertNull(listTypeWithBindings.getRawType());
        assertNull(listTypeWithBindings.getRawClass().getSuperclass());
        assertNull(listTypeWithBindings.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithBindings.getSuperInterfaces());
        assertNull(listTypeWithBindings.getValueHandler());
        assertNull(listTypeWithBindings.getTypeHandler());
        assertFalse(listTypeWithBindings.isStatic());

        TypeBindings bindings = new TypeBindings();
        bindings.put(List.class, Collection.class);
        JavaType listTypeWithBindingsAndSuperClass = CollectionType.construct(List.class, bindings, Collection.class, new Class[0], String.class);
        assertEquals(List.class, listTypeWithBindingsAndSuperClass.getRawClass());
        assertEquals(String.class, listTypeWithBindingsAndSuperClass.getContentType());
        assertEquals(Collection.class, listTypeWithBindingsAndSuperClass.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithBindingsAndSuperClass.getSuperInterfaces());
        assertNull(listTypeWithBindingsAndSuperClass.getValueHandler());
        assertNull(listTypeWithBindingsAndSuperClass.getTypeHandler());
        assertFalse(listTypeWithBindingsAndSuperClass.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        try {
            JavaType listType = CollectionType.construct(List.class, null);
            fail("Expected deprecated constructor to throw an exception");
        } catch (DeprecationException e) {
            // Expected
        }

        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());
        assertNull(listType.getRawType());
        assertNull(listType.getRawClass().getSuperclass());
        assertNull(listType.getSuperClass());
        assertArrayEquals(new Class[]{}, listType.getSuperInterfaces());
        assertNull(listType.getValueHandler());
        assertNull(listType.getTypeHandler());
        assertFalse(listType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeNarrowedToArrayList = listType.narrow(ArrayList.class);
        assertEquals(ArrayList.class, listTypeNarrowedToArrayList.getRawClass());
        assertEquals(String.class, listTypeNarrowedToArrayList.getContentType());
        assertEquals(List.class, listTypeNarrowedToArrayList.getRawType());
        assertEquals(ArrayList.class, listTypeNarrowedToArrayList.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeNarrowedToArrayList.getSuperInterfaces());
        assertNull(listTypeNarrowedToArrayList.getValueHandler());
        assertNull(listTypeNarrowedToArrayList.getTypeHandler());
        assertFalse(listTypeNarrowedToArrayList.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithIntContentType = listType.withContentType(Integer.TYPE);
        assertEquals(List.class, listTypeWithIntContentType.getRawClass());
        assertEquals(Integer.TYPE, listTypeWithIntContentType.getContentType());
        assertNull(listTypeWithIntContentType.getRawType());
        assertNull(listTypeWithIntContentType.getRawClass().getSuperclass());
        assertNull(listTypeWithIntContentType.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithIntContentType.getSuperInterfaces());
        assertNull(listTypeWithIntContentType.getValueHandler());
        assertNull(listTypeWithIntContentType.getTypeHandler());
        assertFalse(listTypeWithIntContentType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithIntTypeHandler = listType.withTypeHandler(Integer.class);
        assertEquals(List.class, listTypeWithIntTypeHandler.getRawClass());
        assertEquals(String.class, listTypeWithIntTypeHandler.getContentType());
        assertNull(listTypeWithIntTypeHandler.getRawType());
        assertNull(listTypeWithIntTypeHandler.getRawClass().getSuperclass());
        assertNull(listTypeWithIntTypeHandler.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithIntTypeHandler.getSuperInterfaces());
        assertEquals(Integer.class, listTypeWithIntTypeHandler.getValueHandler());
        assertNull(listTypeWithIntTypeHandler.getTypeHandler());
        assertFalse(listTypeWithIntTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithIntContentTypeHandler = listType.withContentTypeHandler(Integer.class);
        assertEquals(List.class, listTypeWithIntContentTypeHandler.getRawClass());
        assertEquals(String.class, listTypeWithIntContentTypeHandler.getContentType());
        assertNull(listTypeWithIntContentTypeHandler.getRawType());
        assertNull(listTypeWithIntContentTypeHandler.getRawClass().getSuperclass());
        assertNull(listTypeWithIntContentTypeHandler.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithIntContentTypeHandler.getSuperInterfaces());
        assertNull(listTypeWithIntContentTypeHandler.getValueHandler());
        assertEquals(Integer.class, listTypeWithIntContentTypeHandler.getTypeHandler());
        assertFalse(listTypeWithIntContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithIntValueHandler = listType.withValueHandler(Integer.class);
        assertEquals(List.class, listTypeWithIntValueHandler.getRawClass());
        assertEquals(String.class, listTypeWithIntValueHandler.getContentType());
        assertNull(listTypeWithIntValueHandler.getRawType());
        assertNull(listTypeWithIntValueHandler.getRawClass().getSuperclass());
        assertNull(listTypeWithIntValueHandler.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithIntValueHandler.getSuperInterfaces());
        assertNull(listTypeWithIntValueHandler.getTypeHandler());
        assertEquals(Integer.class, listTypeWithIntValueHandler.getValueHandler());
        assertFalse(listTypeWithIntValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithIntContentValueHandler = listType.withContentValueHandler(Integer.class);
        assertEquals(List.class, listTypeWithIntContentValueHandler.getRawClass());
        assertEquals(String.class, listTypeWithIntContentValueHandler.getContentType());
        assertNull(listTypeWithIntContentValueHandler.getRawType());
        assertNull(listTypeWithIntContentValueHandler.getRawClass().getSuperclass());
        assertNull(listTypeWithIntContentValueHandler.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithIntContentValueHandler.getSuperInterfaces());
        assertEquals(Integer.class, listTypeWithIntContentValueHandler.getValueHandler());
        assertEquals(Integer.class, listTypeWithIntContentValueHandler.getTypeHandler());
        assertFalse(listTypeWithIntContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeWithStaticTyping = listType.withStaticTyping();
        assertEquals(List.class, listTypeWithStaticTyping.getRawClass());
        assertEquals(String.class, listTypeWithStaticTyping.getContentType());
        assertNull(listTypeWithStaticTyping.getRawType());
        assertNull(listTypeWithStaticTyping.getRawClass().getSuperclass());
        assertNull(listTypeWithStaticTyping.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeWithStaticTyping.getSuperInterfaces());
        assertNull(listTypeWithStaticTyping.getValueHandler());
        assertNull(listTypeWithStaticTyping.getTypeHandler());
        assertTrue(listTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct(List.class, null, null, null, String.class);
        JavaType listTypeRefinedToArrayList = listType.refine(ArrayList.class, null, null, null);
        assertEquals(ArrayList.class, listTypeRefinedToArrayList.getRawClass());
        assertEquals(String.class, listTypeRefinedToArrayList.getContentType());
        assertEquals(List.class, listTypeRefinedToArrayList.getRawType());
        assertEquals(ArrayList.class, listTypeRefinedToArrayList.getSuperClass());
        assertArrayEquals(new Class[]{}, listTypeRefinedToArrayList.getSuperInterfaces());
        assertNull(listTypeRefinedToArrayList.getValueHandler());
        assertNull(listTypeRefinedToArrayList.getTypeHandler());
        assertFalse(listTypeRefinedToArrayList.isStatic());
    }
}