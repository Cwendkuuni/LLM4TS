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
        CollectionLikeType result = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(result);
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = null;
        JavaType elemT = null;
        CollectionLikeType result = CollectionLikeType.construct(rawType, elemT);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType elementType = null;
        CollectionLikeType result = CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        JavaType result = CollectionLikeType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        JavaType result = withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionLikeType result = withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType result = CollectionLikeType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void test_refine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        JavaType result = CollectionLikeType.refine(rawType, bindings, superClass, superInterfaces, elementType);
        assertNotNull(result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = CollectionLikeType.isContainerType();
        assertEquals(true, result);
    }

    @Test
    public void testIsCollectionLikeType() {
        boolean result = CollectionLikeType.isCollectionLikeType();
        assertEquals(true, result);
    }

    @Test
    public void testGetContentType() {
        JavaType result = CollectionLikeType.getContentType();
        assertNotNull(result);
    }

    @Test
    public void testGetContentValueHandler() {
        Object result = CollectionLikeType.getContentValueHandler();
        assertNotNull(result);
    }

    @Test
    public void testGetContentTypeHandler() {
        Object result = CollectionLikeType.getContentTypeHandler();
        assertNotNull(result);
    }

    @Test
    public void testHasHandlers() {
        boolean result = CollectionLikeType.hasHandlers();
        assertEquals(true, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = null;
        StringBuilder result = CollectionLikeType.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = null;
        StringBuilder result = CollectionLikeType.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        String result = CollectionLikeType.buildCanonicalName();
        assertNotNull(result);
    }

    @Test
    public void testIsTrueCollectionType() {
        boolean result = CollectionLikeType.isTrueCollectionType();
        assertEquals(true, result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        boolean result = CollectionLikeType.equals(o);
        assertEquals(false, result);
    }

    @Test
    public void testToString() {
        String result = CollectionLikeType.toString();
        assertNotNull(result);
    }
}