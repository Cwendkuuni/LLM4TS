package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

/**
 * Test class for CollectionLikeType.
 */
public class CollectionLikeTypeTest {

    @Test
    public void testNewInstance() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertNotNull(collectionLikeType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertNull(collectionLikeType.getElementType());
    }

    @Test
    public void testNewInstanceWithElementType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, intType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertNotNull(collectionLikeType);
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(intType, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType javaType = CollectionLikeType.construct(int[].class, null);
        CollectionLikeType baseType = (CollectionLikeType) javaType;
        JavaType upgradedType = CollectionLikeType.upgradeFrom(baseType, intType);
        CollectionLikeType upgradedCollectionLikeType = (CollectionLikeType) upgradedType;
        assertEquals(CollectionLikeType.class, upgradedCollectionLikeType.getClass());
        assertEquals(Collection.class, upgradedCollectionLikeType.getRawClass());
        assertEquals(intType, upgradedCollectionLikeType.getElementType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType javaType2 = CollectionLikeType.construct(Map.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType mapCollectionLikeType = (CollectionLikeType) javaType2;
        assertFalse(mapCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertFalse(collectionLikeType.hasHandlers());

        JavaType javaTypeWithValueHandler = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class, new StringValueHandler());
        CollectionLikeType collectionLikeTypeWithValueHandler = (CollectionLikeType) javaTypeWithValueHandler;
        assertTrue(collectionLikeTypeWithValueHandler.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.String]", collectionLikeType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals("[collection-like type; class java.util.ArrayList<T>, contains T]", collectionLikeType.getGenericSignature().toString());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        JavaType javaType2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        assertTrue(javaType1.equals(javaType2));

        JavaType javaType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, Integer.class);
        assertFalse(javaType1.equals(javaType3));
    }
}