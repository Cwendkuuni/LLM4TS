package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerTypeAndIsCollectionLikeType() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        assertTrue(intCollectionType.isContainerType());
        assertTrue(intCollectionType.isCollectionLikeType());

        JavaType arrayType = JavaType.constructType(int[].class);
        assertFalse(arrayType.isContainerType());
        assertFalse(arrayType.isCollectionLikeType());

        JavaType listType = CollectionLikeType.construct(java.util.List.class, null, null, null, JavaType.of(int.class));
        assertTrue(listType.isContainerType());
        assertTrue(listType.isCollectionLikeType());

        JavaType mapType = CollectionLikeType.construct(java.util.Map.class, null, null, null, new CollectionLikeType(Collection.class, null, null, null, JavaType.of(int.class)), JavaType.of(String.class), null, false);
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        assertTrue(intCollectionType.isTrueCollectionType());

        JavaType arrayType = JavaType.constructType(int[].class);
        assertFalse(arrayType.isTrueCollectionType());

        JavaType listType = CollectionLikeType.construct(java.util.List.class, null, null, null, JavaType.of(int.class));
        assertTrue(listType.isTrueCollectionType());

        JavaType mapType = CollectionLikeType.construct(java.util.Map.class, null, null, null, new CollectionLikeType(Collection.class, null, null, null, JavaType.of(int.class)), JavaType.of(String.class), null, false);
        assertTrue(mapType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        JavaType intType = intCollectionType.getContentType();
        assertTrue(intType.isPrimitive());
        assertTrue(intType.isNumber());
        assertTrue(intType.equals(JavaType.of(int.class)));
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        Object intHandler = intCollectionType.getContentTypeHandler();
        assertNotNull(intHandler);
        // Testing instanceOf is not recommended, but it's the only way to test for this specific handler class
        assertTrue(intHandler instanceof CollectionTypeHandler);
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        Object intHandler = intCollectionType.getContentValueHandler();
        assertNull(intHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        assertTrue(intCollectionType.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        String canonicalName = intCollectionType.buildCanonicalName();
        assertTrue(canonicalName.contains("java.util.Collection"));
        assertTrue(canonicalName.contains("int"));
    }

    @Test
    public void testGetGenericSignature() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("[L");
        expectedSignature.append(Collection.class.getName());
        expectedSignature.append("<Ljava/lang/Integer;>");

        StringBuilder actualSignature = new StringBuilder();
        intCollectionType.getGenericSignature(actualSignature);

        assertTrue(actualSignature.toString().equals(expectedSignature.toString()));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append("[L");
        expectedSignature.append(Collection.class.getName());
        expectedSignature.append(";");

        StringBuilder actualSignature = new StringBuilder();
        intCollectionType.getErasedSignature(actualSignature);

        assertTrue(actualSignature.toString().equals(expectedSignature.toString()));
    }

    @Test
    public void testWithContentType() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        JavaType stringCollectionType = intCollectionType.withContentType(JavaType.of(String.class));
        assertFalse(intCollectionType.equals(stringCollectionType));
        assertTrue(stringCollectionType.isContainerType());
        assertTrue(stringCollectionType.isCollectionLikeType());
        assertTrue(stringCollectionType.getContentType().equals(JavaType.of(String.class)));
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType intCollectionType = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.of(int.class));
        JavaType intStringCollectionType = intCollectionType.withContentTypeHandler(new CollectionTypeHandler(JavaType.of(String.class)));
        assertFalse(intCollectionType.equals(intStringCollectionType));
        assertTrue(intStringCollectionType.isContainerType());
        assertTrue(intStringCollectionType.isCollectionLikeType());
        assertTrue(intStringCollectionType.getContentType().equals(JavaType.of(String.class)));
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intType = JavaType.of(int[].class);
        JavaType intCollectionType = CollectionLikeType.upgradeFrom(intType, JavaType.of(int.class));
        assertTrue(intCollectionType.isContainerType());
        assertTrue(intCollectionType.isCollectionLikeType());
        assertTrue(intCollectionType.getContentType().equals(JavaType.of(int.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType intType = JavaType.of(int.class);
        CollectionLikeType.upgradeFrom(intType, JavaType.of(String.class));
    }

    @Test
    public void testConstructFromRawTypeAndElementType() {
        JavaType intCollectionType = CollectionLikeType.construct(java.util.List.class, null, null, null, JavaType.of(int.class));
        JavaType intListType = CollectionLikeType.construct(intCollectionType.getType().getRawType(), null, null, null, JavaType.of(int.class));
        assertTrue(intListType.isContainerType());
        assertTrue(intListType.isCollectionLikeType());
        assertTrue(intListType.getContentType().equals(JavaType.of(int.class)));
    }

    @Test
    public void testRefine() {
        JavaType intCollectionType = CollectionLikeType.construct(java.util.List.class, null, null, null, JavaType.of(int.class));
        JavaType intArrayListType = intCollectionType.refine(java.util.ArrayList.class, null, null, null);
        assertTrue(intArrayListType.isContainerType());
        assertTrue(intArrayListType.isCollectionLikeType());
        assertTrue(intArrayListType.getContentType().equals(JavaType.of(int.class)));
    }
}