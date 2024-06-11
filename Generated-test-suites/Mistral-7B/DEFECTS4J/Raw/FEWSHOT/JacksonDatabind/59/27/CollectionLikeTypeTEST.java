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
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isContainerType());

        Class<?> rawType = new Object() {}.getClass(); // anonymous class
        CollectionLikeType collectionLikeTypeAnonymous = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeTypeAnonymous.isContainerType());

        Class<List<String>> listStringType = List.class.getTypeParameters()[0].getType();
        CollectionLikeType collectionLikeTypeList = CollectionLikeType.construct(listStringType, TypeBindings.emptyBindings(), null, null, String.class);
        assertTrue(collectionLikeTypeList.isContainerType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType.isTrueCollectionType());

        Class<?> rawType = new Object() {}.getClass(); // anonymous class
        CollectionLikeType collectionLikeTypeAnonymous = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), null, null, null);
        assertFalse(collectionLikeTypeAnonymous.isTrueCollectionType());

        Class<List<String>> listStringType = List.class.getTypeParameters()[0].getType();
        CollectionLikeType collectionLikeTypeList = CollectionLikeType.construct(listStringType, TypeBindings.emptyBindings(), null, null, String.class);
        assertTrue(collectionLikeTypeList.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertNull(collectionLikeType.getContentType());

        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.construct(javaType, TypeBindings.emptyBindings(), null, null, String.class);
        assertEquals(String.class, collectionLikeTypeString.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertNull(collectionLikeType.getContentValueHandler());

        Object valueHandler = new Object() {};
        JavaType valueTypeWithHandler = TypeFactory.defaultInstance().constructType(Collection.class, String.class, valueHandler);
        CollectionLikeType collectionLikeTypeWithHandler = CollectionLikeType.construct(valueTypeWithHandler, TypeBindings.emptyBindings(), null, null, String.class, valueHandler, null, false);
        assertEquals(valueHandler, collectionLikeTypeWithHandler.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertNull(collectionLikeType.getContentTypeHandler());

        Object typeHandler = new Object() {};
        JavaType valueTypeWithHandler = TypeFactory.defaultInstance().constructType(Collection.class, String.class, null, typeHandler);
        CollectionLikeType collectionLikeTypeWithHandler = CollectionLikeType.construct(valueTypeWithHandler, TypeBindings.emptyBindings(), null, null, String.class, null, typeHandler, false);
        assertEquals(typeHandler, collectionLikeTypeWithHandler.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertFalse(collectionLikeType.hasHandlers());

        Object valueHandler = new Object() {};
        JavaType valueTypeWithHandler = TypeFactory.defaultInstance().constructType(Collection.class, String.class, null, null, valueHandler);
        CollectionLikeType collectionLikeTypeWithHandler = CollectionLikeType.construct(valueTypeWithHandler, TypeBindings.emptyBindings(), null, null, String.class, null, null, false);
        assertTrue(collectionLikeTypeWithHandler.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals("java.util.Collection", collectionLikeType.getErasedSignature().toString());

        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertEquals("java.util.Collection<java.lang.String>", collectionLikeTypeString.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals("java.util.Collection<T>", collectionLikeType.getGenericSignature().toString());

        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertEquals("java.util.Collection<T>", collectionLikeTypeString.getGenericSignature().toString());
        assertEquals("java.lang.String", collectionLikeTypeString.getContentType().getTypeName());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals("java.util.Collection", collectionLikeType.buildCanonicalName());

        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertEquals("java.util.Collection<java.lang.String>", collectionLikeTypeString.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));

        CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, null);
        assertFalse(collectionLikeType1.equals(collectionLikeType3));

        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeTypeString1 = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeTypeString2 = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertTrue(collectionLikeTypeString1.equals(collectionLikeTypeString2));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals("[collection-like type; class java.util.Collection, contains null]", collectionLikeType.toString());

        JavaType valueType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        CollectionLikeType collectionLikeTypeString = CollectionLikeType.construct(valueType, TypeBindings.emptyBindings(), null, null, String.class);
        assertEquals("[collection-like type; class java.util.Collection<java.lang.String>, contains java.lang.String]", collectionLikeTypeString.toString());
    }
}