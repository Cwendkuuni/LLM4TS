package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), _bogusSuperClass(Collection.class), null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), _bogusSuperClass(String.class), null, null, null, null, false);
        assertFalse(nonCollectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), _bogusSuperClass(Collection.class), null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), _bogusSuperClass(String.class), null, null, null, null, false);
        assertTrue(nonCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new TypeFactory().constructType(String[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, javaType, null, null, false);
        assertEquals(javaType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, null, null, null, false);
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, null, null, null, false);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, null, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());

        CollectionLikeType collectionLikeTypeWithHandlers = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, null, new Object(), new Object(), false);
        assertTrue(collectionLikeTypeWithHandlers.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, null, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.util.ArrayList;");
        StringBuilder actual = collectionLikeType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        JavaType javaType = new TypeFactory().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, javaType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.util.ArrayList;<T:Ljava.lang.String;>]");
        StringBuilder actual = new StringBuilder();
        collectionLikeType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        JavaType javaType = new TypeFactory().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, javaType, null, null, false);
        String expected = "[Ljava.util.ArrayList;";
        String actual = collectionLikeType.buildCanonicalName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsTrueCollectionType() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        CollectionLikeType collectionLikeType = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        Class<String> stringClass = String.class;
        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(stringClass, TypeBindings.emptyBindings(), _bogusSuperClass(stringClass), null, null, null, null, false);
        assertFalse(nonCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        TypeVariable<?>[] typeVariables = arrayListClass.getTypeParameters();
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, null, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, null, null, null, false);

        CollectionLikeType collectionLikeType3 = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, null, null, null, false);
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(LinkedList.class, TypeBindings.emptyBindings(), _bogusSuperClass(LinkedList.class), null, null, null, null, false);

        CollectionLikeType collectionLikeType5 = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, null, null, null, true);

        assertEquals(collectionLikeType1, collectionLikeType2);
        assertNotEquals(collectionLikeType1, collectionLikeType3);
        assertNotEquals(collectionLikeType1, collectionLikeType4);
        assertNotEquals(collectionLikeType1, collectionLikeType5);
    }

    @Test
    public void testToString() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        JavaType javaType = new TypeFactory().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, javaType, null, null, false);
        String expected = "[collection-like type; class java.util.ArrayList, contains class java.lang.String]";
        String actual = collectionLikeType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstruct() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        JavaType stringJavaType = new TypeFactory().constructType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(arrayListClass, TypeBindings.emptyBindings(), _bogusSuperClass(arrayListClass), null, stringJavaType);
        assertEquals(arrayListClass, collectionLikeType._class);
        assertEquals(stringJavaType, collectionLikeType._elementType);
    }

    @Test
    public void testConstructDeprecated() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        JavaType stringJavaType = new TypeFactory().constructType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(arrayListClass, stringJavaType);
        assertEquals(arrayListClass, collectionLikeType._class);
        assertEquals(stringJavaType, collectionLikeType._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        Class<ArrayList> arrayListClass = ArrayList.class;
        JavaType intJavaType = new TypeFactory().constructType(int.class);
        CollectionLikeType.construct(arrayListClass, intJavaType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new TypeFactory().constructType(String.class);
        JavaType collectionLikeType = CollectionLikeType.construct(String[].class, TypeBindings.emptyBindings(), _bogusSuperClass(String[].class), null, javaType);
        JavaType upgradedCollectionLikeType = CollectionLikeType.upgradeFrom(collectionLikeType, javaType);
        assertEquals(collectionLikeType, upgradedCollectionLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromIllegalArgumentException() {
        JavaType javaType = new TypeFactory().constructType(String.class);
        JavaType notTypeBase = new TypeFactory().constructType(ArrayList.class);
        CollectionLikeType.upgradeFrom(javaType, notTypeBase);
    }
}