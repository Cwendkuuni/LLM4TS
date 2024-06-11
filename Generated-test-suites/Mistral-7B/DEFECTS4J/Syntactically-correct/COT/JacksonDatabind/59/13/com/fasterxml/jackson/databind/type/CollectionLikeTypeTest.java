package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructor() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, elemType);
        assertEquals(Collection.class, type.getRawClass());
        assertEquals(elemType, type.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType("java.util.ArrayList");
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleType("java.lang.String");
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(type.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(type.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elemType, null, null, false);
        assertEquals(elemType, type.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elemType1 = new SimpleType("java.lang.Integer");
        JavaType elemType2 = new SimpleType("java.lang.Long");
        CollectionLikeType type1 = new CollectionLikeType(Collection.class, null, null, null, elemType1, null, null, false);
        CollectionLikeType type2 = new CollectionLikeType(Collection.class, null, null, null, elemType2, null, null, false);
        assertFalse(type1.equals(type2));

        CollectionLikeType type3 = new CollectionLikeType(Collection.class, null, null, null, elemType1, null, null, false);
        assertTrue(type1.equals(type3));
    }

    @Test
    public void testToString() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elemType, null, null, false);
        String result = type.toString();
        assertTrue(result.contains("[collection-like type; class Collection<java.lang.Integer>, contains java.lang.Integer]"));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elemType, null, null, false);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        String result = sb.toString();
        assertTrue(result.contains("Collection<java.lang.Integer>"));
    }

    @Test
    public void testGetGenericSignature() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elemType, null, null, false);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        String result = sb.toString();
        assertTrue(result.contains("Collection<java.lang.Integer>"));
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elemType, null, null, false);
        String result = type.buildCanonicalName();
        assertTrue(result.contains("CollectionLikeType"));
        assertTrue(result.contains("[collection-like type; class Collection, contains java.lang.Integer]"));
    }
}