package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeBindings.TypeBinding;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getElementType().getType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = JavaType.constructRawListType(ArrayList.class, String.class);
        CollectionLikeType baseType = (CollectionLikeType) javaType;
        JavaType elementType = JavaType.constructValueType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getElementType().getType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertTrue(javaType.isTrueCollectionType());

        JavaType javaType2 = JavaType.constructMapType(HashMap.class, Integer.class, String.class);
        assertFalse(javaType2.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals(String.class, javaType.getContentType().getType());
    }

    @Test
    public void testGetElementType() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals(String.class, javaType.getElementType().getType());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertTrue(javaType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals("java.util.ArrayList<java.lang.String>", javaType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals("java.util.ArrayList<java.lang.String>", javaType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals("java.util.ArrayList<java.lang.String>", javaType.getCanonicalName());
    }
}