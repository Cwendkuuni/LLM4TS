package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeBinding;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(Integer.class), collectionLikeType.getSuperClass());
        assertEquals(JavaType.of(Integer.class), collectionLikeType.getElementType());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType javaType = CollectionLikeType.construct(List.class, TypeBindings.standardMapBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(List.class, collectionLikeType.getRawClass());
        assertEquals(JavaType.constructRawListType(Integer.class), collectionLikeType.getSuperClass());
        assertEquals(JavaType.of(Integer.class), collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = JavaType.constructRawListType(Integer.class);
        JavaType elementType = JavaType.of(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertEquals(JavaType.constructRawListType(Integer.class).getClass(), javaType.getClass());
        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(JavaType.constructRawListType(Integer.class), collectionLikeType.getSuperClass());
        assertEquals(JavaType.of(Integer.class), collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType javaType = JavaType.constructRawListType(String.class);
        JavaType elementType = JavaType.of(Integer.class);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType javaType = JavaType.constructRawListType(String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.class));
        assertFalse(collectionLikeTypeString.isTrueCollectionType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType javaType = JavaType.constructRawListType(String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.class));
        assertTrue(collectionLikeTypeString.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertEquals(JavaType.of(Integer.class), collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertEquals(null, collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertEquals(null, collectionLikeType.getContentTypeHandler());

        JavaType javaType = JavaType.constructRawListType(Integer.class);
        CollectionLikeType collectionLikeTypeInt = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.class));
        assertNotNull(collectionLikeTypeInt.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertFalse(collectionLikeType.hasHandlers());

        JavaType javaType = JavaType.constructRawListType(Integer.class);
        CollectionLikeType collectionLikeTypeInt = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.class));
        assertTrue(collectionLikeTypeInt.hasHandlers());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);

        CollectionLikeType collectionLikeType2 = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));

        CollectionLikeType collectionLikeType3 = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType3));

        CollectionLikeType collectionLikeType4 = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Long.class), null, JavaType.of(Integer.class), null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType4));

        CollectionLikeType collectionLikeType5 = new CollectionLikeType(List.class, TypeBindings.standardMapBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType5));

        CollectionLikeType collectionLikeType6 = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, true);
        assertFalse(collectionLikeType1.equals(collectionLikeType6));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertEquals("[collection-like type; class java.util.List, contains class java.lang.Integer]", collectionLikeType.toString());
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("java.util.List<java.lang.Integer>");
        assertEquals(expected.toString(), collectionLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("java.util.List<java.lang.Integer>");
        collectionLikeType.getGenericSignature(expected);
        assertEquals("[list]<T extends java.lang.Object>, java.lang.Class<T>>", expected.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Integer.class), null, JavaType.of(Integer.class), null, null, false);
        assertEquals("java.util.List<java.lang.Integer>", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testRefine() {
        JavaType javaType = JavaType.constructRawListType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.class));
        CollectionLikeType refined = collectionLikeType.refine(List.class, TypeBindings.emptyBindings(),
                JavaType.constructRawListType(Long.class), null);
        assertEquals(JavaType.constructRawListType(Long.class), refined.getSuperClass());
        assertEquals(JavaType.of(Long.class), refined.getElementType());
    }
}