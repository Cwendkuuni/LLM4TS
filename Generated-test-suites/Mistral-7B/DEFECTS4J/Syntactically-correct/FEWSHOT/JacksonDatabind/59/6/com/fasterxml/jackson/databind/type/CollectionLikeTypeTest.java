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
    public void testConstruct() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertTrue(type.isCollectionLikeType());
        assertTrue(type.isContainerType());
        assertEquals(Collection.class, type.getRawType());
        assertEquals(elemType, type.getContentType());
        assertFalse(type.isTrueCollectionType());
    }

    @Test
    public void testConstructWithBindings() {
        TypeVariable<?>[] vars = List.class.getTypeParameters();
        TypeBindings bindings = TypeBindings.create(List.class, Integer.class);
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(List.class, bindings, null, null, elemType);
        assertTrue(type.isCollectionLikeType());
        assertTrue(type.isContainerType());
        assertEquals(List.class, type.getRawType());
        assertEquals(Integer.class, type.getTypeVariables()[0].getType());
        assertEquals(elemType, type.getContentType());
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testUpgradeFromSimpleType() {
        JavaType baseType = new SimpleType(List.class);
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertTrue(type.isCollectionLikeType());
        assertTrue(type.isContainerType());
        assertEquals(List.class, type.getRawType());
        assertEquals(Integer.class, type.getTypeVariables()[0].getType());
        assertEquals(elemType, type.getContentType());
        assertTrue(type.isTrueCollectionType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    @Test
    public void testRefine() {
        JavaType baseType = new SimpleType(ArrayList.class);
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.refine(baseType, Collection.class, null, null);
        assertTrue(type.isCollectionLikeType());
        assertTrue(type.isContainerType());
        assertEquals(ArrayList.class, type.getRawType());
        assertEquals(Integer.class, type.getTypeVariables()[0].getType());
        assertEquals(elemType, type.getContentType());
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testIsCollecionLikeType() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Map.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertTrue(type.isCollectionLikeType());
        assertFalse(type.isTrueCollectionType());
    }

    @Test
    public void testIsContainerType() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Map.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertTrue(type.isContainerType());
        assertFalse(type.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(elemType, type.getContentType());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object handler = new Object();
        JavaType elemType = new SimpleType(Integer.class).withTypeHandler(handler);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals(handler, type.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertTrue(type.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("collection-like type; class java.util.Collection, contains java.lang.Integer", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("[collection-like type; class java.util.Collection<java.lang.Integer>]", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.Integer]", type.toString());
    }

    @Test
    public void testEquals() {
        JavaType elemType = new SimpleType(Integer.class);
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, elemType);
        CollectionLikeType type3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, elemType);
        CollectionLikeType type4 = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, elemType);
        CollectionLikeType type5 = new CollectionLikeType(type1);

        assertFalse(type1.equals(null));
        assertFalse(type1.equals(""));
        assertFalse(type1.equals(new Object()));

        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertTrue(type1.equals(type5));

        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(type4));
    }
}