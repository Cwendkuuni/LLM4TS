package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * Test for {@link CollectionLikeType} class.
 */
public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        // Test constructing CollectionLikeType using no-args constructor
        JavaType javaType = CollectionLikeType.construct(java.util.Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertNotNull(javaType);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
        assertEquals(Collection.class, collectionLikeType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), collectionLikeType.getTypeBindings());
        assertNull(collectionLikeType.getElementType());

        // Test constructing CollectionLikeType using TypeBase constructor
        JavaType typeBaseJavaType = TypeBase.construct(Collection.class, null, null, null);
        CollectionLikeType typeBaseCollectionLikeType = (CollectionLikeType) typeBaseJavaType;
        assertNotNull(typeBaseCollectionLikeType);
        assertEquals(Collection.class, typeBaseCollectionLikeType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), typeBaseCollectionLikeType.getTypeBindings());
        assertNull(typeBaseCollectionLikeType.getElementType());

        // Test constructing CollectionLikeType using construct static method
        JavaType javaTypeFromConstruct = CollectionLikeType.construct(java.util.Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals(javaType, javaTypeFromConstruct);

        // Test constructing CollectionLikeType using upgradeFrom static method
        JavaType javaTypeToUpgrade = TypeBase.construct(java.util.ArrayList.class, null, null, null);
        JavaType collectionLikeTypeToUpgrade = CollectionLikeType.upgradeFrom(javaTypeToUpgrade, String.class);
        assertEquals(java.util.List.class, collectionLikeTypeToUpgrade.getRawClass());
        assertEquals(String.class, collectionLikeTypeToUpgrade.getElementType().getRawClass());
    }

    @Test
    public void testCollectionLikeTypeMethods() {
        JavaType javaType = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        // Test isContainerType method
        assertTrue(collectionLikeType.isContainerType());

        // Test isCollectionLikeType method
        assertTrue(collectionLikeType.isCollectionLikeType());

        // Test getContentType method
        assertEquals(String.class, collectionLikeType.getContentType());

        // Test getContentValueHandler method
        assertNull(collectionLikeType.getContentValueHandler());

        // Test getContentTypeHandler method
        assertNotNull(collectionLikeType.getContentTypeHandler());

        // Test hasHandlers method
        assertTrue(collectionLikeType.hasHandlers());

        // Test isTrueCollectionType method
        assertTrue(collectionLikeType.isTrueCollectionType());

        // Test equals method
        JavaType javaType2 = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType2;
        assertTrue(collectionLikeType.equals(collectionLikeType2));

        // Test getErasedSignature method
        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());

        // Test getGenericSignature method
        sb.setLength(0);
        collectionLikeType.getGenericSignature(sb);
        assertEquals("java.util.List<T>", sb.toString());

        // Test buildCanonicalName method
        assertEquals("java.util.List", collectionLikeType.buildCanonicalName());

        // Test toString method
        assertEquals("[collection-like type; class java.util.List, contains java.lang.String]", collectionLikeType.toString());
    }

    @Test
    public void testCollectionLikeTypeRefine() {
        JavaType javaType = CollectionLikeType.construct(java.util.List.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        JavaType javaTypeRefined = collectionLikeType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        CollectionLikeType collectionLikeTypeRefined = (CollectionLikeType) javaTypeRefined;

        assertEquals(ArrayList.class, collectionLikeTypeRefined.getRawClass());
        assertEquals(String.class, collectionLikeTypeRefined.getElementType().getRawClass());
    }

    @Test
    public void testCollectionLikeTypeNarrow() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        JavaType javaTypeNarrow = collectionLikeType.narrow(LinkedList.class);
        CollectionLikeType collectionLikeTypeNarrow = (CollectionLikeType) javaTypeNarrow;

        assertNotNull(javaTypeNarrow);
        assertEquals(LinkedList.class, collectionLikeTypeNarrow.getRawClass());
        assertEquals(String.class, collectionLikeTypeNarrow.getElementType().getRawClass());
    }

    @Test
    public void testCollectionLikeTypeWithHandlers() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        CollectionHandler collectionHandler = new CollectionHandler();
        ValueHandler valueHandler = new ValueHandler();

        JavaType javaTypeWithHandlers = collectionLikeType.withContentTypeHandler(valueHandler).withValueHandler(valueHandler);
        CollectionLikeType collectionLikeTypeWithHandlers = (CollectionLikeType) javaTypeWithHandlers;

        assertEquals(javaType.getRawClass(), collectionLikeTypeWithHandlers.getRawClass());
        assertEquals(valueHandler, collectionLikeTypeWithHandlers.getValueHandler());
        assertEquals(valueHandler, collectionLikeTypeWithHandlers.getContentValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithStaticTyping() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        JavaType javaTypeWithStaticTyping = collectionLikeType.withStaticTyping();
        CollectionLikeType collectionLikeTypeWithStaticTyping = (CollectionLikeType) javaTypeWithStaticTyping;

        assertEquals(javaType.getRawClass(), collectionLikeTypeWithStaticTyping.getRawClass());
        assertTrue(collectionLikeTypeWithStaticTyping.isStaticTyping());
    }

    @JsonTypeName("MyList")
    private static class MyList<T> extends ArrayList<T> {
        // empty
    }

    @Test
    public void testCollectionLikeTypeWithGenericType() {
        JavaType javaType = CollectionLikeType.construct(MyList.class, TypeBindings.emptyBindings(), null, null, String.class);
        CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;

        assertEquals(MyList.class, collectionLikeType.getRawClass());
        assertEquals(String.class, collectionLikeType.getElementType().getRawClass());
    }
}

class CollectionHandler {
    // empty
}

class ValueHandler {
    // empty
}