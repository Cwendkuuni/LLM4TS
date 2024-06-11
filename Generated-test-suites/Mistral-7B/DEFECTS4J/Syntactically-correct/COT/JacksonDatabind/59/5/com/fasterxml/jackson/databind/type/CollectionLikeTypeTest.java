package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeVariableUtils;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructorWithClassAndBindings() {
        JavaType elementType = new SimpleType("java.util.String");
        JavaType collectionType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elementType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getTypeParameters()[0]);
    }

    @Test
    public void testConstructorWithClassAndElementType() {
        JavaType elementType = new SimpleType("java.util.String");
        JavaType collectionType = CollectionLikeType.construct(Collection.class, elementType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getTypeParameters()[0]);
    }

    @Test
    public void testIsContainerType() {
        JavaType collectionType = new SimpleType("java.util.ArrayList");
        JavaType mapType = new SimpleType("java.util.HashMap");
        assertTrue(collectionType.isContainerType());
        assertFalse(mapType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType collectionType = new SimpleType("java.util.ArrayList");
        JavaType mapType = new SimpleType("java.util.HashMap");
        assertTrue(collectionType.isCollectionLikeType());
        assertFalse(mapType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaUtilList = new SimpleType("java.util.List");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(javaUtilList, TypeBindings.emptyBindings(), null, null, stringType);
        assertEquals(stringType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaUtilList = new SimpleType("java.util.List");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(javaUtilList, TypeBindings.emptyBindings(), null, null, stringType);
        assertEquals(stringType.getValueHandler(), collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaUtilList = new SimpleType("java.util.List");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(javaUtilList, TypeBindings.emptyBindings(), null, null, stringType);
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType invalidBaseType = new SimpleType("java.lang.Integer");
        JavaType stringType = new SimpleType("java.lang.String");
        CollectionLikeType.construct(invalidBaseType, stringType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaUtilList = new SimpleType("java.util.List");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeTypeFromBase = CollectionLikeType.construct(javaUtilList, TypeBindings.emptyBindings(), null, null, stringType);
        JavaType collectionLikeTypeFromType = CollectionLikeType.upgradeFrom(javaUtilList, stringType);
        assertEquals(collectionLikeTypeFromBase, collectionLikeTypeFromType);
    }

    @Test
    public void testRefine() {
        JavaType rawType = Collection.class;
        JavaType bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = new SimpleType("java.util.String");
        JavaType collectionLikeType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elementType);
        JavaType refinedCollectionLikeType = collectionLikeType.refine(Collection.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(Collection.class, refinedCollectionLikeType.getRawClass());
        assertEquals(elementType, refinedCollectionLikeType.getTypeParameters()[0]);
    }

    @Test
    public void testEquals() {
        JavaType collectionType1 = new SimpleType("java.util.ArrayList");
        JavaType collectionType2 = new SimpleType("java.util.ArrayList");
        JavaType collectionType3 = new SimpleType("java.util.LinkedList");
        JavaType collectionLikeType1 = CollectionLikeType.construct(collectionType1, TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        JavaType collectionLikeType2 = CollectionLikeType.construct(collectionType1, TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        JavaType collectionLikeType3 = CollectionLikeType.construct(collectionType2, TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.String"));
        JavaType collectionLikeType4 = CollectionLikeType.construct(collectionType1, TypeBindings.emptyBindings(), null, null, new SimpleType("java.lang.Integer"));
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        assertFalse(collectionLikeType1.equals(collectionLikeType3));
        assertFalse(collectionLikeType1.equals(collectionLikeType4));
    }

    @Test
    public void testToString() {
        JavaType collectionType = new SimpleType("java.util.ArrayList");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(), null, null, stringType);
        String expected = "[collection-like type; class java.util.ArrayList, contains java.lang.String]";
        assertEquals(expected, collectionLikeType.toString());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaUtilList = new SimpleType("java.util.ArrayList");
        JavaType javaUtilMap = new SimpleType("java.util.HashMap");
        assertTrue(javaUtilList.isTrueCollectionType());
        assertFalse(javaUtilMap.isTrueCollectionType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType collectionType = new SimpleType("java.util.ArrayList");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(), null, null, stringType);
        StringBuilder erasedSignature = new StringBuilder();
        collectionLikeType.getErasedSignature(erasedSignature);
        String expected = "java.util.List<java.lang.String>";
        assertEquals(expected, erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType collectionType = new SimpleType("java.util.ArrayList");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(), null, null, stringType);
        StringBuilder genericSignature = new StringBuilder();
        collectionLikeType.getGenericSignature(genericSignature);
        String expected = "[Ljava.util.Collection<Ljava.lang.String;>;]";
        assertEquals(expected, genericSignature.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType collectionType = new SimpleType("java.util.ArrayList");
        JavaType stringType = new SimpleType("java.lang.String");
        JavaType collectionLikeType = CollectionLikeType.construct(collectionType, TypeBindings.emptyBindings(), null, null, stringType);
        String expected = "java.util.ArrayList<java.lang.String>";
        assertEquals(expected, collectionLikeType.buildCanonicalName());
    }
}