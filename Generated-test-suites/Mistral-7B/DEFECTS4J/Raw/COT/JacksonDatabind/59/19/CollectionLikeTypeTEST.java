package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Assert;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertEquals(collectionClass, collectionLikeType.getRawClass());
        Assert.assertNotNull(collectionLikeType.getElementType());
        Assert.assertNull(collectionLikeType.getTypeBindings());
        Assert.assertEquals(false, collectionLikeType.isStatic());
    }

    @Test
    public void testConstructWithBindings() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        TypeBindings bindings = TypeBindings.create(collectionClass, JavaType.intType);
        JavaType javaType = CollectionLikeType.construct(collectionClass, bindings, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertEquals(collectionClass, collectionLikeType.getRawClass());
        Assert.assertNotNull(collectionLikeType.getElementType());
        Assert.assertEquals(bindings, collectionLikeType.getTypeBindings());
        Assert.assertEquals(false, collectionLikeType.isStatic());
    }

    @Test
    public void testUpgradeFrom() {
        Class<List<Integer>> listClass = List.class;
        JavaType javaType = JavaType.constructRawListType(JavaType.intType, null);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        JavaType upgradedType = CollectionLikeType.upgradeFrom(javaType, JavaType.intType);
        CollectionLikeType upgradedCollectionLikeType = (CollectionLikeType) upgradedType;
        Assert.assertEquals(listClass, upgradedCollectionLikeType.getRawClass());
        Assert.assertEquals(collectionLikeType.getElementType(), upgradedCollectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.constructRawClass(String.class);
        CollectionLikeType.upgradeFrom(javaType, JavaType.intType);
    }

    @Test
    public void testIsTrueCollectionType() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeFalse() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = CollectionLikeType.construct(mapClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertEquals(JavaType.intType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertEquals(JavaType.intType.getValueHandler(), collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertEquals(JavaType.intType.getTypeHandler(), collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        Assert.assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testHasHandlersFalse() {
        Class<List<Integer>> listClass = List.class;
        JavaType javaType = CollectionLikeType.construct(listClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        JavaType intType = JavaType.intType;
        collectionLikeType = collectionLikeType.withContentTypeHandler(intType.getValueHandler());
        Assert.assertTrue(collectionLikeType.hasHandlers());
        collectionLikeType = collectionLikeType.withContentTypeHandler(null);
        Assert.assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testEquals() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType1 = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        JavaType javaType2 = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType1 = (CollectionLikeType) javaType1;
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType2;
        Assert.assertEquals(collectionLikeType1, collectionLikeType2);
    }

    @Test
    public void testNotEquals() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType1 = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        JavaType javaType2 = CollectionLikeType.construct(List.class, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType1 = (CollectionLikeType) javaType1;
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType2;
        Assert.assertNotEquals(collectionLikeType1, collectionLikeType2);
    }

    @Test
    public void testToString() {
        Class<Collection<Integer>> collectionClass = Collection.class;
        JavaType javaType = CollectionLikeType.construct(collectionClass, null, null, null, JavaType.intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        String toString = collectionLikeType.toString();
        Assert.assertEquals("[collection-like type; class " + collectionClass.getName() + ", contains " + JavaType.intType + "]", toString);
    }
}