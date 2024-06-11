package com.fasterxml.jackson.databind.type;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Assert;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        Assert.assertNotNull(collectionLikeType);
    }

    @Test
    public void testConstruct2() {
        Class<?> rawType = Collection.class;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        Assert.assertNotNull(collectionLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType elementType = null;

        try {
            CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Can not upgrade from an instance of null", e.getMessage());
        }

    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType javaType = collectionLikeType._narrow(subclass);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType javaType = collectionLikeType.withContentType(contentType);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType javaType = collectionLikeType.withTypeHandler(h);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType javaType = collectionLikeType.withContentTypeHandler(h);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType javaType = collectionLikeType.withValueHandler(h);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType javaType = collectionLikeType.withContentValueHandler(h);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        CollectionLikeType javaType = collectionLikeType.withStaticTyping();
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType javaType = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = collectionLikeType.isContainerType();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = collectionLikeType.isCollectionLikeType();
        Assert.assertTrue(result);
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        JavaType result = collectionLikeType.getContentType();
        Assert.assertNull(result);
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object result = collectionLikeType.getContentValueHandler();
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        Object result = collectionLikeType.getContentTypeHandler();
        Assert.assertNull(result);
    }    

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = collectionLikeType.hasHandlers();
        Assert.assertFalse(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        StringBuilder result = collectionLikeType.getErasedSignature(sb);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        StringBuilder result = collectionLikeType.getGenericSignature(sb);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        String result = collectionLikeType.buildCanonicalName();
        Assert.assertNotNull(result);
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = collectionLikeType.isTrueCollectionType();
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals() {
        Object o = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        boolean result = collectionLikeType.equals(o);
        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, null, null, null, null, null, null, false);
        String result = collectionLikeType.toString();
        Assert.assertNotNull(result);
    }

}