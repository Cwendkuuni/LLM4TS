package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = CollectionLikeType.construct(java.util.List.class, null, null, null, String.class);
        assertEquals(java.util.List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());

        assertEquals(listType.getClass(), listType.getClass());
        assertEquals(String.class, listType.getContentType().getClass());
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType listType = CollectionLikeType.construct(java.util.List.class, TypeBindings.defaultBindings(), null, null, String.class);
        assertEquals(java.util.List.class, listType.getRawClass());
        assertEquals(String.class, listType.getContentType());

        assertEquals(listType.getClass(), listType.getClass());
        assertEquals(String.class, listType.getContentType().getClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = JavaType.constructType(java.util.ArrayList.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(listType, String.class);

        assertEquals(java.util.List.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getContentType());

        assertEquals(collectionLikeType.getClass(), collectionLikeType.getClass());
        assertEquals(String.class, collectionLikeType.getContentType().getClass());
    }

    @Test
    public void testRefine() {
        JavaType listType = JavaType.constructType(java.util.ArrayList.class);
        JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, String.class);
        JavaType refinedType = collectionLikeType.refine(java.util.List.class, null, null, null);

        assertEquals(java.util.List.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getContentType());

        assertEquals(refinedType.getClass(), refinedType.getClass());
        assertEquals(String.class, refinedType.getContentType().getClass());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType listType = JavaType.constructType(java.util.ArrayList.class);
        JavaType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, String.class);

        assertTrue(listType.isInstanceOf(Collection.class));
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeFalse() {
        JavaType intType = JavaType.constructType(int.class);
        JavaType collectionLikeType = CollectionLikeType.construct(Integer.TYPE, null, null, null, String.class);

        assertFalse(intType.isInstanceOf(Collection.class));
        assertFalse(collectionLikeType.isTrueCollectionType());
    }
}