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
        // Add additional assertions
    }

    @Test
    @Deprecated
    public void testConstruct1() {
        Class<?> rawType = null;
        JavaType elemT = null;
        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        Assert.assertNotNull(collectionType);
        // Add additional assertions
    }

    @Test
    @Deprecated
    public void test_narrow() {
        Class<?> subclass = null;
        JavaType narrowedType = collectionType._narrow(subclass);

        Assert.assertNotNull(narrowedType);
        // Add additional assertions
    }

    @Test
    public void testWithContentType_sameContentType() {
        JavaType contentType = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, contentType, null, null, false);
        CollectionType withContentType = collectionType.withContentType(contentType);

        Assert.assertEquals(collectionType, withContentType);
    }

    @Test
    public void testWithContentType_differentContentType() {
        JavaType contentType = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType withContentType = collectionType.withContentType(contentType);

        Assert.assertNotEquals(collectionType, withContentType);
        Assert.assertEquals(contentType, withContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType withTypeHandler = collectionType.withTypeHandler(h);

        Assert.assertNotEquals(collectionType, withTypeHandler);
        Assert.assertEquals(h, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(h);

        Assert.assertNotEquals(collectionType, withContentTypeHandler);
        Assert.assertEquals(h, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType withValueHandler = collectionType.withValueHandler(h);

        Assert.assertNotEquals(collectionType, withValueHandler);
        Assert.assertEquals(h, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(h);

        Assert.assertNotEquals(collectionType, withContentValueHandler);
        Assert.assertEquals(h, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping_alreadyStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, true);
        CollectionType withStaticTyping = collectionType.withStaticTyping();

        Assert.assertEquals(collectionType, withStaticTyping);
    }

    @Test
    public void testWithStaticTyping_notAlreadyStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType withStaticTyping = collectionType.withStaticTyping();

        Assert.assertNotEquals(collectionType, withStaticTyping);
        Assert.assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = null;
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        Assert.assertNotNull(refinedType);
        // Add additional assertions
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);

        String str = collectionType.toString();

        Assert.assertNotNull(str);
    }
}