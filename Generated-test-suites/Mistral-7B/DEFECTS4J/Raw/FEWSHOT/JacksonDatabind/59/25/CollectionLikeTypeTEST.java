package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        Class<String> stringClass = String.class;
        CollectionLikeType stringCollectionLikeType = new CollectionLikeType(stringClass, null, null, null, null, null, null, false);
        assertFalse(stringCollectionLikeType.isContainerType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        Class<String> stringClass = String.class;
        CollectionLikeType stringCollectionLikeType = new CollectionLikeType(stringClass, null, null, null, null, null, null, false);
        assertFalse(stringCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        Class<String> stringClass = String.class;
        CollectionLikeType stringCollectionLikeType = new CollectionLikeType(stringClass, null, null, null, null, null, null, false);
        assertTrue(stringCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[0]);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        JavaType contentType = collectionLikeType.getContentType();

        assertTrue(contentType instanceof Class);
        assertEquals(Object.class, contentType.getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[0]);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Object contentValueHandler = collectionLikeType.getContentValueHandler();

        assertNull(contentValueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[0]);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Object contentTypeHandler = collectionLikeType.getContentTypeHandler();

        assertNull(contentTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[0]);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        boolean hasHandlers = collectionLikeType.hasHandlers();

        assertFalse(hasHandlers);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[0]);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder("Ljava/util/Collection;");
        StringBuilder actualSignature = collectionLikeType.getErasedSignature(new StringBuilder());

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[] { new TypeVariable("T") {}});
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        StringBuilder expectedSignature = new StringBuilder("[Ljava.util.Collection;<T:Ljava/lang/Object;>;");
        StringBuilder actualSignature = collectionLikeType.getGenericSignature(new StringBuilder());

        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[0]);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        String expectedCanonicalName = "class java.util.Collection";
        String actualCanonicalName = collectionLikeType.buildCanonicalName();

        assertEquals(expectedCanonicalName, actualCanonicalName);
    }
}