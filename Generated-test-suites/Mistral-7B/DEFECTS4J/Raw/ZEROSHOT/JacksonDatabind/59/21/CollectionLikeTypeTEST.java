package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

public class CollectionLikeTypeTest {

    private static final JavaType ELEMENT_TYPE = TypeFactory.defaultInstance().constructType(int[].class);
    private static final JavaType COLLECTION_TYPE = TypeFactory.defaultInstance().constructType(Collection.class);
    private static final JavaType COLLECTION_LIKE_TYPE = TypeFactory.defaultInstance().constructType(ArrayList.class);

    private CollectionLikeType collectionLikeType;

    @Before
    public void setUp() {
        collectionLikeType = CollectionLikeType.upgradeFrom(COLLECTION_TYPE, ELEMENT_TYPE);
    }

    @Test
    public void testIsContainerType() {
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        assertEquals(ELEMENT_TYPE, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        assertNotNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        assertEquals("java.util.Collection", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.Object;>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        assertEquals("java.util.ArrayList", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionLikeType.narrow(ArrayList.class);
        assertEquals(collectionLikeType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidType() {
        collectionLikeType.narrow(String.class);
    }

    @Test
    public void testWithContentType() {
        JavaType newElementType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(newElementType);
        assertEquals(newElementType, newCollectionLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);
        assertEquals(handler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
        assertEquals(handler, newCollectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(handler);
        assertEquals(handler, newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object() {};
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
        assertEquals(handler, newCollectionLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        assertTrue(newCollectionLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType refinedType = collectionLikeType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(collectionLikeType, refinedType);
    }

    @Test
    public void testConstruct() {
        JavaType customCollectionType = TypeFactory.defaultInstance().constructType(MyArrayList.class);
        CollectionLikeType customCollectionLikeType = CollectionLikeType.construct(customCollectionType.getRawClass(), TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
        assertEquals(ELEMENT_TYPE, customCollectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidType() {
        CollectionLikeType.construct(String.class, ELEMENT_TYPE);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(listType, ELEMENT_TYPE);
        assertEquals(ELEMENT_TYPE, upgradedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        CollectionLikeType.upgradeFrom(String.class, ELEMENT_TYPE);
    }
}

class MyArrayList extends ArrayList<Integer> {
    // Empty class
}