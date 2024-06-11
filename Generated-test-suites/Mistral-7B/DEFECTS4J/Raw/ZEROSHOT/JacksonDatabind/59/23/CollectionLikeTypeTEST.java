package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

/**
 * Test class for CollectionLikeType
 */
public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertEquals(javaType.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(SimpleType.construct(JavaType.getTypeFactory().constructType(Collection.class), TypeBindings.emptyBindings()), collectionLikeType.getRawType());
        assertEquals(JavaType.getTypeFactory().constructType(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeUpgrade() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        JavaType elementType = new SimpleType("java.lang.String");
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertEquals(javaType.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(JavaType.getTypeFactory().constructType(Collection.class, elementType), collectionLikeType.getRawType());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertTrue(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testCollectionLikeTypeGetContentType() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertEquals(JavaType.getTypeFactory().constructType(String.class), collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertTrue(collectionLikeType.isTrueCollectionType());
        JavaType javaListType = new SimpleType("java.util.List", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeListType = CollectionLikeType.construct(javaListType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertFalse(collectionLikeListType.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeEquals() {
        JavaType javaType1 = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        JavaType javaType2 = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(javaType1.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(javaType2.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertTrue(collectionLikeType1.equals(collectionLikeType2));

        JavaType javaType3 = new SimpleType("java.util.LinkedList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(javaType3.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertFalse(collectionLikeType1.equals(collectionLikeType3));

        CollectionLikeType collectionLikeType4 = new CollectionLikeType(collectionLikeType1);
        assertTrue(collectionLikeType1.equals(collectionLikeType4));

        TypeBase typeBase = new TypeBase(javaType1.getRawClass(), TypeBindings.emptyBindings(), null, null);
        CollectionLikeType collectionLikeType5 = new CollectionLikeType(typeBase, new SimpleType("java.lang.String"));
        assertFalse(collectionLikeType1.equals(collectionLikeType5));
    }

    @Test
    public void testCollectionLikeTypeGetErasedSignature() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        StringBuilder erasedSignature = new StringBuilder();
        collectionLikeType.getErasedSignature(erasedSignature);
        assertEquals("Collection<String>", erasedSignature.toString());
    }

    @Test
    public void testCollectionLikeTypeGetGenericSignature() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        StringBuilder genericSignature = new StringBuilder();
        collectionLikeType.getGenericSignature(genericSignature);
        assertEquals("[collection-like type; class java.util.ArrayList<T>, contains java.lang.String]", genericSignature.toString());
    }

    @Test
    public void testCollectionLikeTypeToString() {
        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        assertEquals("[collection-like type; class java.util.ArrayList<String>, contains java.lang.String]", collectionLikeType.toString());
    }
}