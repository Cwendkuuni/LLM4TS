package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(collectionLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType elementType = null;

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(collectionLikeType);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        JavaType javaType = collectionLikeType._narrow(subclass);
        assertNotNull(javaType);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        JavaType javaType = collectionLikeType.withContentType(contentType);
        assertNotNull(javaType);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        CollectionLikeType result = collectionLikeType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        CollectionLikeType result = collectionLikeType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        CollectionLikeType result = collectionLikeType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        CollectionLikeType result = collectionLikeType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        CollectionLikeType result = collectionLikeType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        JavaType javaType = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(javaType);
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        boolean result = collectionLikeType.isContainerType();
        assertTrue(result);
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        boolean result = collectionLikeType.isCollectionLikeType();
        assertTrue(result);
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        JavaType javaType = collectionLikeType.getContentType();
        assertNotNull(javaType);
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        Object result = collectionLikeType.getContentValueHandler();
        assertNotNull(result);
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        Object result = collectionLikeType.getContentTypeHandler();
        assertNotNull(result);
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        boolean result = collectionLikeType.hasHandlers();
        assertTrue(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        StringBuilder result = collectionLikeType.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        StringBuilder result = collectionLikeType.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        String result = collectionLikeType.buildCanonicalName();
        assertNotNull(result);
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        boolean result = collectionLikeType.isTrueCollectionType();
        assertTrue(result);
    }

    @Test
    public void testEquals() {
        Object o = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType();
        boolean result = collectionLikeType.equals(o);
        assertFalse(result);
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        String result = collectionLikeType.toString();
        assertNotNull(result);
    }
}