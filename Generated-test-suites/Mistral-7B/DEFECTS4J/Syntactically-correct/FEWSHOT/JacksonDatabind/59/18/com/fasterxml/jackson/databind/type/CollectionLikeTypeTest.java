package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertEquals(listType.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(IntType.INSTANCE, collectionLikeType.getElementType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        TypeVariable<?> intTypeVariable = Integer.TYPE.getTypeParameters()[0];
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, intTypeVariable.getName());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.standardBindings());
        assertEquals(listType.getRawClass(), collectionLikeType.getRawClass());
        assertEquals(intTypeVariable, collectionLikeType.getTypeBindings().getBindingForTypeVariable(intTypeVariable.getName()));
        assertEquals(IntType.INSTANCE, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, intType);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        JavaType upgradedType = CollectionLikeType.upgradeFrom(collectionLikeType, IntType.INSTANCE);
        assertEquals(ArrayListType.construct(Integer.TYPE), upgradedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, intType);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        CollectionLikeType.upgradeFrom(collectionLikeType, IntType.INSTANCE);
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertEquals(Integer.TYPE, collectionLikeType.getContentType());
    }

    @Test
    public void testGetElementType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertEquals(Integer.TYPE, collectionLikeType.getElementType());
    }

    @Test
    public void testHasHandlers() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        assertTrue(collectionLikeType.isTrueCollectionType());
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.TYPE);
        CollectionLikeType mapCollectionLikeType = CollectionLikeType.construct(mapType.getRawClass(), null);
        assertFalse(mapCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType listType1 = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        JavaType listType2 = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(listType1.getRawClass(), null);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(listType2.getRawClass(), null);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        JavaType listType3 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);
        CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(listType3.getRawClass(), null);
        assertFalse(collectionLikeType1.equals(collectionLikeType3));
        assertFalse(collectionLikeType1.equals(""));
        assertFalse(collectionLikeType1.equals(null));
    }

    @Test
    public void testToString() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        String actual = collectionLikeType.toString();
        String expected = "[collection-like type; class java.util.Collection<java.lang.Integer>, contains java.lang.Integer]";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        StringBuilder expected = new StringBuilder();
        expected.append("[collection-like type; class java.util.Collection, contains java.lang.Integer]");
        StringBuilder actual = collectionLikeType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(listType.getRawClass(), null);
        StringBuilder expected = new StringBuilder();
        expected.append("[collection-like type; class java.util.Collection<java.lang.Integer>]");
        StringBuilder actual = new StringBuilder();
        collectionLikeType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }
}