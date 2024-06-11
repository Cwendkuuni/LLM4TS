package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() throws Exception {
        JavaType collectionType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, new JavaType(int[].class));
        CollectionLikeType t = new CollectionLikeType(collectionType);
        assertEquals(java.util.ArrayList.class, t.getRawClass());
        assertEquals(int[].class, t.getContentType());
        assertFalse(t.isTrueCollectionType());
    }

    @Test
    public void testConstructWithBindings() throws Exception {
        JavaType collectionType = CollectionLikeType.construct(List.class, null, null, null, new JavaType(int.class), null);
        CollectionLikeType t = new CollectionLikeType(collectionType);
        assertEquals(java.util.List.class, t.getRawClass());
        assertEquals(int.class, t.getContentType());
        assertTrue(t.isTrueCollectionType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() throws Exception {
        CollectionLikeType.construct(String[].class, null, null, null, new JavaType(int[].class));
    }

    @Test
    public void testUpgradeFrom() throws Exception {
        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, int[].class);
        CollectionLikeType t = new CollectionLikeType(collectionLikeType);
        assertEquals(javaType.getRawClass(), t.getRawClass());
        assertEquals(int[].class, t.getContentType());
        assertTrue(t.isTrueCollectionType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() throws Exception {
        JavaType javaType = new SimpleType(String.class);
        CollectionLikeType.upgradeFrom(javaType, int[].class);
    }

    @Test
    public void testNarrow() throws Exception {
        JavaType javaType = new SimpleType(java.util.ArrayList.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, null);
        CollectionLikeType narrow = collectionLikeType.narrow(ArrayList.class);
        CollectionLikeType t = new CollectionLikeType(narrow);
        assertEquals(javaType.getRawClass(), t.getRawClass());
        assertEquals(Collection.class, t.getRawClass().getInterfaces()[0]);
        assertTrue(t.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionType() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testGetContentType() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertEquals(Object.class, collectionLikeType.getContentType().getRawClass());
        JavaType javaType = new SimpleType(String.class);
        CollectionLikeType t = new CollectionLikeType(javaType, null);
        assertEquals(String.class, t.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertNull(collectionLikeType.getContentValueHandler());
        JavaType javaType = new SimpleType(String.class);
        CollectionLikeType t = new CollectionLikeType(javaType, null);
        assertNull(t.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertFalse(collectionLikeType.hasHandlers());
        JavaType javaType = new SimpleType(String.class);
        CollectionLikeType t = new CollectionLikeType(javaType, null);
        assertFalse(t.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertEquals("java.util.ArrayList", collectionLikeType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertEquals("java.util.ArrayList<java.lang.Object>", collectionLikeType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() throws Exception {
        CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null);
        assertEquals("java.util.ArrayList", collectionLikeType.buildCanonicalName());
    }
}