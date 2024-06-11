package com.example; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null, null, null, null);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertNull(collectionLikeType.getTypeBindings());
        assertNull(collectionLikeType.getSuperClass());
        assertArrayEquals(new Class[0], collectionLikeType.getSuperInterfaces());
        assertNull(collectionLikeType.getElementType());
    }

    @Test
    public void testConstructWithTypeBindingsAndElementType() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertNull(collectionLikeType.getTypeBindings());
        assertNull(collectionLikeType.getSuperClass());
        assertArrayEquals(new Class[0], collectionLikeType.getSuperInterfaces());
        assertEquals(Integer.TYPE, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = JavaTypeFactory.instance.constructType(ArrayList.class);
        CollectionLikeType collectionLikeType1 = (CollectionLikeType) javaType;
        JavaType javaTypeWithElementType = JavaTypeFactory.instance.constructType(Integer.TYPE);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(collectionLikeType1, javaTypeWithElementType);
        assertEquals(collectionLikeType1.getRawClass(), collectionLikeType2.getRawClass());
        assertEquals(javaTypeWithElementType, collectionLikeType2.getElementType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        assertEquals(Integer.TYPE, javaType.getContentType());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        assertTrue(javaType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        assertTrue(javaType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        JavaType javaType2 = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        JavaType javaType3 = CollectionLikeType.construct(LinkedList.class, null, null, null, Integer.TYPE);
        assertEquals(javaType1, javaType1);
        assertEquals(javaType1, javaType2);
        assertNotEquals(javaType1, javaType3);
    }

    @Test
    public void testToString() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, null, null, null, Integer.TYPE);
        String toString = javaType.toString();
        assertTrue(toString.contains("collection-like type"));
        assertTrue(toString.contains("class ArrayList"));
        assertTrue(toString.contains("contains " + Integer.TYPE.getName()));
    }
}