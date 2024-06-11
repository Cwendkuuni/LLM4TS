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
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        JavaType javaType = new SimpleType("java.util.ArrayList", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeTypeFromSimpleType = CollectionLikeType.upgradeFrom(javaType, null);
        assertTrue(collectionLikeTypeFromSimpleType.isContainerType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType javaType = new SimpleType("com.example.MyClass", TypeBindings.emptyBindings());
        CollectionLikeType collectionLikeTypeFromSimpleType = new CollectionLikeType(javaType, null);
        assertFalse(collectionLikeTypeFromSimpleType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaTypeOfString = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, javaTypeOfString, null, null, false);
        JavaType contentType = collectionLikeType.getContentType();
        assertNotNull(contentType);
        assertTrue(contentType.equals(javaTypeOfString));

        TypeVariable<?>[] typeVariables = Collection.class.getTypeParameters();
        JavaType javaTypeOfElementType = new SimpleType(typeVariables[0].getType());
        CollectionLikeType collectionLikeTypeWithTypeVariables = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, javaTypeOfElementType, null, null, false);
        JavaType contentTypeWithTypeVariables = collectionLikeTypeWithTypeVariables.getContentType();
        assertNotNull(contentTypeWithTypeVariables);
        assertTrue(contentTypeWithTypeVariables.equals(javaTypeOfElementType));
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        Object contentValueHandler = collectionLikeType.getContentValueHandler();
        assertNull(contentValueHandler);

        JavaType javaTypeOfString = new SimpleType(String.class);
        CollectionLikeType collectionLikeTypeWithStringElement = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, javaTypeOfString, null, null, false);
        Object contentValueHandlerWithStringElement = collectionLikeTypeWithStringElement.getContentValueHandler();
        assertNull(contentValueHandlerWithStringElement);

        Object stringValueHandler = new StringValueHandler();
        CollectionLikeType collectionLikeTypeWithStringValueHandler = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, stringValueHandler, null, false);
        Object contentValueHandlerWithStringValueHandler = collectionLikeTypeWithStringValueHandler.getContentValueHandler();
        assertSame(stringValueHandler, contentValueHandlerWithStringValueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        Object contentTypeHandler = collectionLikeType.getContentTypeHandler();
        assertNull(contentTypeHandler);

        JavaType javaTypeOfString = new SimpleType(String.class);
        CollectionLikeType collectionLikeTypeWithStringElement = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, javaTypeOfString, null, null, false);
        Object contentTypeHandlerWithStringElement = collectionLikeTypeWithStringElement.getContentTypeHandler();
        assertNull(contentTypeHandlerWithStringElement);

        Object stringTypeHandler = new StringTypeHandler();
        CollectionLikeType collectionLikeTypeWithStringTypeHandler = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, stringTypeHandler, false);
        Object contentTypeHandlerWithStringTypeHandler = collectionLikeTypeWithStringTypeHandler.getContentTypeHandler();
        assertSame(stringTypeHandler, contentTypeHandlerWithStringTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeTypeWithNoHandlers = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(collectionLikeTypeWithNoHandlers.hasHandlers());

        Object stringValueHandler = new StringValueHandler();
        CollectionLikeType collectionLikeTypeWithStringValueHandler = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, stringValueHandler, null, false);
        assertTrue(collectionLikeTypeWithStringValueHandler.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        StringBuilder expectedErasedSignature = new StringBuilder("[collection-like type; class java.util.Collection, contains null]");
        StringBuilder actualErasedSignature = collectionLikeType.getErasedSignature(new StringBuilder());
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        StringBuilder expectedGenericSignature = new StringBuilder("[collection-like type; class java.util.Collection<E>, extends java.util.Collection<E>, contains java.lang.Object]");
        StringBuilder actualGenericSignature = collectionLikeType.getGenericSignature(new StringBuilder());
        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        String expectedCanonicalName = "[collection-like type; class java.util.Collection, contains null]";
        String actualCanonicalName = collectionLikeType.buildCanonicalName();
        assertEquals(expectedCanonicalName, actualCanonicalName);

        JavaType javaTypeOfElementType = new SimpleType("java.lang.String");
        CollectionLikeType collectionLikeTypeWithElementType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, javaTypeOfElementType, null, null, false);
        String expectedCanonicalNameWithElementType = "[collection-like type; class java.util.Collection, contains java.lang.String]";
        String actualCanonicalNameWithElementType = collectionLikeTypeWithElementType.buildCanonicalName();
        assertEquals(expectedCanonicalNameWithElementType, actualCanonicalNameWithElementType);
    }
}