package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(List.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testConstructWithBindings() {
        TypeVariable<?>[] vars = List.class.getTypeParameters();
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, bindings, null, null, elemType);
        assertEquals(List.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(ArrayList.class);
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawType());
        assertEquals(elemType, collectionLikeType.getElementType());
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleType(Object.class);
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    @Test
    public void testIsContainerType() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertEquals(elemType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.util.ArrayList;");
        assertEquals(expected.toString(), collectionLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.util.ArrayList;<Ljava.lang.String;>]");
        assertEquals(expected.toString(), collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        StringBuilder expected = new StringBuilder("java.util.ArrayList");
        assertEquals(expected.toString(), collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(LinkedList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        assertEquals(collectionLikeType1, collectionLikeType2);
        assertFalse(collectionLikeType1.equals(collectionLikeType3));
    }

    @Test
    public void testToString() {
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType, null, null, false);
        StringBuilder expected = new StringBuilder("[collection-like type; class java.util.ArrayList, contains class java.lang.String]");
        assertEquals(expected.toString(), collectionLikeType.toString());
    }
}