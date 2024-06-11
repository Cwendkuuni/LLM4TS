package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        JavaType javaType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(java.util.List.class, collectionLikeType.getRawClass());
        assertEquals(new JavaType(int.class), collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeUpgradeFrom() {
        JavaType javaType = new JavaType(java.util.ArrayList.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, new JavaType(int.class));
        assertEquals(java.util.Collection.class, collectionLikeType.getRawClass());
        assertEquals(new JavaType(int.class), collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionLikeTypeUpgradeFromInvalidBaseType() {
        JavaType javaType = new JavaType(String.class);
        CollectionLikeType.upgradeFrom(javaType, new JavaType(int.class));
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testCollectionLikeTypeIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings(),
                _bogusSuperClass(java.util.ArrayList.class), null, new JavaType(int.class));
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        assertEquals(new JavaType(int.class), collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        assertNotNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testCollectionLikeTypeGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        assertNotNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testCollectionLikeTypeEquality() {
        JavaType javaType1 = new CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings(),
                _bogusSuperClass(java.util.ArrayList.class), null, new JavaType(int.class));
        JavaType javaType2 = new CollectionLikeType.construct(java.util.ArrayList.class, new TypeBindings(),
                _bogusSuperClass(java.util.ArrayList.class), null, new JavaType(int.class));
        assertEquals(javaType1, javaType2);
    }

    @Test
    public void testCollectionLikeTypeToString() {
        JavaType javaType = new CollectionLikeType.construct(java.util.List.class, new TypeBindings(),
                _bogusSuperClass(java.util.List.class), null, new JavaType(int.class));
        String expected = "[collection-like type; class java.util.List, contains class int]";
        assertEquals(expected, javaType.toString());
    }

}