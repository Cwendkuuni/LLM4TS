package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(List.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());

        collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType elemType = new SimpleType(String.class);
        JavaType listType = new ParameterizedType(List.class, new Type[]{elemType});
        TypeBindings bindings = TypeBindings.create(List.class, elemType);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, bindings, null, null, elemType);
        assertEquals(listType, collectionLikeType.getType());
        assertEquals(elemType, collectionLikeType.getElementType());

        collectionLikeType = CollectionLikeType.construct(ArrayList.class, bindings, null, null, elemType);
        JavaType arrayListType = new ParameterizedType(ArrayList.class, new Type[]{elemType});
        assertEquals(arrayListType, collectionLikeType.getType());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new SimpleType(List.class);
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elemType);
        assertEquals(List.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());

        JavaType arrayListType = new ParameterizedType(ArrayList.class, new Type[]{elemType});
        JavaType javaListType = new ParameterizedType(List.class, new Type[]{elemType});
        collectionLikeType = CollectionLikeType.upgradeFrom(javaListType, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());

        try {
            collectionLikeType = CollectionLikeType.upgradeFrom(new SimpleType(Object.class), elemType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertTrue(collectionLikeType.isTrueCollectionType());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType arrayType = new SimpleType(int[].class);
        collectionLikeType = new CollectionLikeType(arrayType, TypeBindings.emptyBindings(), null, null, new SimpleType(int.class));
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertTrue(collectionLikeType.isCollectionLikeType());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType arrayType = new SimpleType(int[].class);
        collectionLikeType = new CollectionLikeType(arrayType, TypeBindings.emptyBindings(), null, null, new SimpleType(int.class));
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertEquals(new SimpleType(String.class), collectionLikeType.getContentType());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertEquals(new SimpleType(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertNull(collectionLikeType.getContentValueHandler());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertNull(collectionLikeType.getContentTypeHandler());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertFalse(collectionLikeType.hasHandlers());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        assertEquals("[Ljava.util.List;", sb.toString());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        assertEquals("[Ljava.util.ArrayList;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);
        assertEquals("java.util.List<T>", sb.toString());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);
        assertEquals("java.util.ArrayList<T>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertEquals("java.util.List<java.lang.String>", collectionLikeType.buildCanonicalName());

        collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class));
        assertEquals("java.util.ArrayList<java.lang.String>", collectionLikeType.buildCanonicalName());
    }
}