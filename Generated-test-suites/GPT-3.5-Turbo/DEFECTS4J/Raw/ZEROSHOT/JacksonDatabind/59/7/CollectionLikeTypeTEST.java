package com.fasterxml.jackson.databind.type;

import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    private CollectionLikeType collectionLikeType;

    @Before
    public void setUp() {
        JavaType elementType = JavaTypeFactory.defaultInstance().constructType(String.class);
        collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, elementType, null, null, false);
    }

    @Test
    public void testConstruct() {
        // Given
        JavaType superClass = JavaTypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = {};
        JavaType elementType = JavaTypeFactory.defaultInstance().constructType(Integer.class);

        // When
        CollectionLikeType result = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), superClass, superInts, elementType);

        // Then
        assertNotNull(result);
        assertEquals(Collection.class, result.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInts, result.getInterfaces());
        assertEquals(elementType, result.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        // Given
        JavaType baseType = JavaTypeFactory.defaultInstance().constructType(Object.class);
        JavaType elementType = JavaTypeFactory.defaultInstance().constructType(Double.class);

        // When
        CollectionLikeType result = CollectionLikeType.upgradeFrom(baseType, elementType);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(elementType, result.getContentType());

        // Given
        baseType = JavaTypeFactory.defaultInstance().constructType(String.class);
        elementType = JavaTypeFactory.defaultInstance().constructType(Boolean.class);

        // When
        Exception exception = null;
        try {
            result = CollectionLikeType.upgradeFrom(baseType, elementType);
        } catch (IllegalArgumentException e) {
            exception = e;
        }

        // Then
        assertNotNull(exception);
        assertEquals("Can not upgrade from an instance of class com.fasterxml.jackson.databind.type.SimpleType", exception.getMessage());
    }

    @Test
    public void testNarrow() {
        // Given
        Class<?> subclass = Integer.class;

        // When
        JavaType result = collectionLikeType._narrow(subclass);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(subclass, result.getRawClass());
    }

    @Test
    public void testWithContentType() {
        // Given
        JavaType contentType = JavaTypeFactory.defaultInstance().constructType(Boolean.class);

        // When
        JavaType result = collectionLikeType.withContentType(contentType);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(collectionLikeType.getRawClass(), result.getRawClass());
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        // Given
        Object typeHandler = new Object();

        // When
        CollectionLikeType result = collectionLikeType.withTypeHandler(typeHandler);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(collectionLikeType.getRawClass(), result.getRawClass());
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Given
        Object contentTypeHandler = new Object();

        // When
        CollectionLikeType result = collectionLikeType.withContentTypeHandler(contentTypeHandler);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(collectionLikeType.getRawClass(), result.getRawClass());
        assertEquals(contentTypeHandler, result.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Given
        Object valueHandler = new Object();

        // When
        CollectionLikeType result = collectionLikeType.withValueHandler(valueHandler);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(collectionLikeType.getRawClass(), result.getRawClass());
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Given
        Object contentValueHandler = new Object();

        // When
        CollectionLikeType result = collectionLikeType.withContentValueHandler(contentValueHandler);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(collectionLikeType.getRawClass(), result.getRawClass());
        assertEquals(contentValueHandler, result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // When
        CollectionLikeType result = collectionLikeType.withStaticTyping();

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(collectionLikeType.getRawClass(), result.getRawClass());
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Given
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaTypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = {};

        // When
        JavaType result = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);

        // Then
        assertNotNull(result);
        assertEquals(CollectionLikeType.class, result.getClass());
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInterfaces, result.getInterfaces());
        assertEquals(collectionLikeType.getContentType(), result.getContentType());
    }

    @Test
    public void testIsContainerType() {
        // When
        boolean result = collectionLikeType.isContainerType();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsCollectionLikeType() {
        // When
        boolean result = collectionLikeType.isCollectionLikeType();

        // Then
        assertTrue(result);
    }

    @Test
    public void testGetContentType() {
        // When
        JavaType result = collectionLikeType.getContentType();

        // Then
        assertNotNull(result);
        assertEquals(collectionLikeType.getContentType(), result);
    }

    @Test
    public void testGetContentValueHandler() {
        // When
        Object result = collectionLikeType.getContentValueHandler();

        // Then
        assertNull(result);
    }

    @Test
    public void testGetContentTypeHandler() {
        // When
        Object result = collectionLikeType.getContentTypeHandler();

        // Then
        assertNull(result);
    }

    @Test
    public void testHasHandlers() {
        // When
        boolean result = collectionLikeType.hasHandlers();

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetErasedSignature() {
        // Given
        StringBuilder sb = new StringBuilder();

        // When
        StringBuilder result = collectionLikeType.getErasedSignature(sb);

        // Then
        assertNotNull(result);
        assertEquals(collectionLikeType.getErasedSignature(sb).toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        // Given
        StringBuilder sb = new StringBuilder();

        // When
        StringBuilder result = collectionLikeType.getGenericSignature(sb);

        // Then
        assertNotNull(result);
        assertEquals(collectionLikeType.getGenericSignature(sb).toString(), result.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        // When
        String result = collectionLikeType.buildCanonicalName();

        // Then
        assertNotNull(result);
        assertEquals("[collection-like type; class java.util.Collection, contains com.fasterxml.jackson.databind.JavaType]", result);
    }

    @Test
    public void testIsTrueCollectionType() {
        // When
        boolean result = collectionLikeType.isTrueCollectionType();

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals() {
        // When
        boolean result = collectionLikeType.equals(collectionLikeType);

        // Then
        assertTrue(result);
    }

    @Test
    public void testToString() {
        // When
        String result = collectionLikeType.toString();

        // Then
        assertNotNull(result);
        assertEquals("[collection-like type; class java.util.Collection, contains com.fasterxml.jackson.databind.JavaType]", result);
    }
}