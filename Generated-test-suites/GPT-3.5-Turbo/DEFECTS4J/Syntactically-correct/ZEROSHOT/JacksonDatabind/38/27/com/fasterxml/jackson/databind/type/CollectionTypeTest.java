package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        Assert.assertNotNull(collectionType);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentType(contentType);

        Assert.assertNotNull(result);
        Assert.assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withTypeHandler(h);

        Assert.assertNotNull(result);
        Assert.assertEquals(h, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentTypeHandler(h);

        Assert.assertNotNull(result);
        Assert.assertEquals(h, result.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withValueHandler(h);

        Assert.assertNotNull(result);
        Assert.assertEquals(h, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentValueHandler(h);

        Assert.assertNotNull(result);
        Assert.assertEquals(h, result.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withStaticTyping();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = (CollectionType) collectionType.refine(rawType, bindings, superClass, superInterfaces);

        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        Class<?> rawType = null;
        JavaType elemT = null;

        CollectionType collectionType = new CollectionType(rawType, null, null, null, elemT, null, null, false);
        String result = collectionType.toString();

        String expected = "[collection type; class null, contains null]";
        Assert.assertEquals(expected, result);
    }
}