package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {
    @Test
    public void testIsContainerType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, null);
        CollectionLikeType hashSetType = CollectionLikeType.construct(java.util.HashSet.class, null, null, null, null);
        CollectionLikeType customCollectionType = CollectionLikeType.construct(MyCustomCollection.class, null, null, null, null);
        assertTrue(listType.isContainerType());
        assertTrue(hashSetType.isContainerType());
        assertTrue(customCollectionType.isContainerType());

        assertFalse(JavaType.constructRawType(String.class).isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, null);
        CollectionLikeType hashSetType = CollectionLikeType.construct(java.util.HashSet.class, null, null, null, null);
        CollectionLikeType customCollectionType = CollectionLikeType.construct(MyCustomCollection.class, null, null, null, null);

        assertTrue(listType.isCollectionLikeType());
        assertTrue(hashSetType.isCollectionLikeType());
        assertTrue(customCollectionType.isCollectionLikeType());

        assertFalse(JavaType.constructRawType(String.class).isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, String.class);
        assertEquals(String.class, listType.getContentType());

        CollectionLikeType hashSetType = CollectionLikeType.construct(java.util.HashSet.class, null, null, null, Integer.class);
        assertEquals(Integer.class, hashSetType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, String.class);
        assertTrue(listType.isTrueCollectionType());

        CollectionLikeType customCollectionType = CollectionLikeType.construct(MyCustomCollection.class, null, null, null, null);
        assertFalse(customCollectionType.isTrueCollectionType());
    }

    @Test
    public void testConstruct() {
        Class<MyCustomCollection> customCollectionClass = MyCustomCollection.class;
        JavaType stringType = JavaType.constructType(String.class);

        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, null);
        CollectionLikeType customCollectionType = CollectionLikeType.construct(customCollectionClass, null, null, null, stringType);

        assertEquals(customCollectionClass, customCollectionType._class);
        assertEquals(stringType, customCollectionType._elementType);
    }

    @Test
    public void testUpgradeFrom() {
        Class<MyCustomCollection> customCollectionClass = MyCustomCollection.class;
        JavaType stringType = JavaType.constructType(String.class);
        JavaType collectionType = JavaType.constructType(java.util.Collection.class, JavaType.constructTypeVariable(String.class));

        CollectionLikeType baseType = CollectionLikeType.construct(collectionType, null, null, null, null);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, stringType);

        assertEquals(customCollectionClass, upgradedType._class);
        assertEquals(stringType, upgradedType._elementType);
    }

    @Test
    public void testRefine() {
        Class<MyCustomCollection> customCollectionClass = MyCustomCollection.class;
        JavaType stringType = JavaType.constructType(String.class);

        JavaType collectionType = JavaType.constructType(java.util.Collection.class, JavaType.constructTypeVariable(String.class));
        CollectionLikeType baseType = CollectionLikeType.construct(collectionType, null, null, null, null);

        CollectionLikeType refinedType = (CollectionLikeType) baseType.refine(customCollectionClass, null, null, null);

        assertEquals(customCollectionClass, refinedType._class);
        assertEquals(stringType, refinedType._elementType);
    }

    private static class MyCustomCollection implements Collection<String> {
        // Empty implementation for testing purposes
    }
}