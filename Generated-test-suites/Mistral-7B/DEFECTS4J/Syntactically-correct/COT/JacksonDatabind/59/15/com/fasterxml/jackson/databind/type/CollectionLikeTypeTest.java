package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    // Basic tests for constructors

    @Test
    public void testConstructors() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType, TypeBindings.emptyBindings(), null, null, javaType);
        assertInstanceOf(CollectionLikeType.class, collectionLikeType);
        assertEquals(javaType, collectionLikeType.getContentType());
    }

    @Test
    public void testConstructorsWithBindings() {
        Class<?> listType = Collection.class;
        TypeVariable<?> listTypeVariable = listType.getTypeParameters()[0];
        TypeBindings bindings = TypeBindings.create(listType, String.class);
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType, bindings, null, null, javaType);
        assertEquals(listTypeVariable.getName(), collectionLikeType.getBindings().get(0).getName());
        assertEquals(javaType, collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithInvalidBaseType() {
        Class<?> invalidBaseType = String.class;
        JavaType javaType = JavaType.constructCollectionType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(invalidBaseType, javaType);
    }

    // Test for upgradeFrom method

    @Test
    public void testUpgradeFrom() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType baseType = CollectionLikeType.construct(listType, null, null, null, javaType);
        JavaType elementType = JavaType.constructRawClass(Integer.class);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertInstanceOf(CollectionLikeType.class, upgradedType);
        assertEquals(elementType, upgradedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = JavaType.constructRawClass(String.class);
        JavaType elementType = JavaType.constructRawClass(Integer.class);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    // Test for isTrueCollectionType method

    @Test
    public void testIsTrueCollectionType() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeForEmptyList() {
        Class<?> emptyListType = java.util.ArrayList.class;
        JavaType javaType = JavaType.constructCollectionType(emptyListType, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(emptyListType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    // Test for equals method

    @Test
    public void testEquals() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(java.util.HashMap.class, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);

        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        assertTrue(collectionLikeType1.equals(collectionLikeType3));
        assertFalse(collectionLikeType1.equals(collectionLikeType4));
    }

    // Test for toString method

    @Test
    public void testToString() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        String expectedString = "[collection-like type; class " + listType.getName() + ", contains " + javaType + "]";
        String actualString = collectionLikeType.toString();
        assertEquals(expectedString, actualString);
    }

    // Test for getCanonicalName method

    @Test
    public void testGetCanonicalName() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        String expectedString = "[collection-like type; class " + listType.getName() + "<String>, contains " + javaType + "]";
        String actualString = collectionLikeType.getCanonicalName();
        assertEquals(expectedString, actualString);
    }

    // Test for getGenericSignature method

    @Test
    public void testGetGenericSignature() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        StringBuilder expectedStringBuilder = new StringBuilder();
        collectionLikeType.getGenericSignature(expectedStringBuilder);
        String expectedString = expectedStringBuilder.toString();
        String expectedSignature = "<T>" + expectedString.substring(expectedString.indexOf("<") + 1, expectedString.length() - 1);
        StringBuilder actualStringBuilder = new StringBuilder();
        collectionLikeType.getGenericSignature(actualStringBuilder);
        String actualString = actualStringBuilder.toString();
        assertEquals(expectedSignature, actualString.substring(actualString.indexOf("<") + 1, actualString.length() - 1));
    }

    // Test for getErasedSignature method

    @Test
    public void testGetErasedSignature() {
        Class<?> listType = Collection.class;
        JavaType javaType = JavaType.constructCollectionType(listType, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), null, null, javaType, null, null, false);
        StringBuilder expectedStringBuilder = new StringBuilder();
        collectionLikeType.getErasedSignature(expectedStringBuilder);
        String expectedString = expectedStringBuilder.toString();
        String expectedSignature = "[collection-like type; class " + listType.getName() + "]";
        StringBuilder actualStringBuilder = new StringBuilder();
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(JavaType.constructRawClass(listType), javaType);
        upgradedType.getErasedSignature(actualStringBuilder);
        String actualString = actualStringBuilder.toString();
        assertEquals(expectedSignature, actualString);
    }
}