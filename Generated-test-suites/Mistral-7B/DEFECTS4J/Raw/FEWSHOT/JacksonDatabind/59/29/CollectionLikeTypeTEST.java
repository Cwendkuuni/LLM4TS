package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, null);
        assertTrue(listType.isContainerType());

        CollectionLikeType stringType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, null);
        assertTrue(listType.isCollectionLikeType());

        CollectionLikeType stringType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertTrue(stringType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertEquals(java.lang.Integer.TYPE, listType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertNull(listType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertNull(listType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertFalse(listType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertTrue(listType.isTrueCollectionType());

        CollectionLikeType stringType = CollectionLikeType.construct(String.class, null, null, null, null);
        assertFalse(stringType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType listType1 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        CollectionLikeType listType2 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertEquals(listType1, listType2);

        CollectionLikeType listType3 = CollectionLikeType.construct(java.util.LinkedList.class, null, null, null, java.lang.Integer.TYPE);
        assertNotEquals(listType1, listType3);

        CollectionLikeType listType4 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Long.TYPE);
        assertNotEquals(listType1, listType4);

        CollectionLikeType listType5 = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE, null, null, true);
        assertNotEquals(listType1, listType5);
    }

    @Test
    public void testToString() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]", listType.toString());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        StringBuilder sb = new StringBuilder();
        listType.getErasedSignature(sb);
        assertEquals("java.util.ArrayList", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.Integer;]", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType listType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
        assertEquals("java.util.ArrayList<java.lang.Integer>", listType.buildCanonicalName());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, intType);

        JavaType upgraded = CollectionLikeType.upgradeFrom(intType, intType);
        assertEquals(collectionType, upgraded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType.upgradeFrom(intType, stringType);
    }
}