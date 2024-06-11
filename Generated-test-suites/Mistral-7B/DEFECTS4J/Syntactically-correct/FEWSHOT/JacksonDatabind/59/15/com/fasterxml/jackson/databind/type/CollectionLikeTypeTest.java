package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isContainerType());

        JavaType nonCollectionType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(nonCollectionType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType nonCollectionLikeType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(nonCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, new TypeVariable<Integer>() {}.getGenericComponentType());
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.TYPE);
        assertEquals(Integer.TYPE, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, new TypeVariable<Integer>() {}.getGenericComponentType());
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.TYPE);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, new TypeVariable<Integer>() {}.getGenericComponentType());
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.TYPE);
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType nonCollectionType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType first = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType second = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(first.equals(second));

        Object nonCollectionLikeType = new Object();
        assertFalse(first.equals(nonCollectionLikeType));
    }

    @Test
    public void testToString() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, new TypeVariable<Integer>() {}.getGenericComponentType());
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.TYPE);
        assertEquals("[collection-like type; class java.util.List, contains class java.lang.Integer]", collectionLikeType.toString());
    }
}