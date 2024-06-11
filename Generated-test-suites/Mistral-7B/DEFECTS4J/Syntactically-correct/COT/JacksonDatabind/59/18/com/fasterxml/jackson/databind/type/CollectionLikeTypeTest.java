package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

    @Test
    public void testConstructor() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        TypeBase base = new TypeBase(javaType.getRawClass(), bindings);
        CollectionLikeType type = CollectionLikeType.construct(javaType.getRawClass(), bindings, null, null, javaType.getComponentType());
        assertEquals(javaType, type.getRawType());
        assertEquals(bindings, type.getBindings());
        assertEquals(javaType.getComponentType(), type.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertEquals(javaType.getRawClass(), collectionLikeType.getRawType().getRawClass());
        assertEquals(elementType, collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeFalse() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        JavaType expected = TypeFactory.defaultInstance().constructType(Integer.class);
        assertEquals(expected, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        TypeSerializer typeSerializer = TypeSerializer.instance;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertEquals(typeSerializer, collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        TypeDeserializer typeDeserializer = TypeDeserializer.instance;
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertEquals(typeDeserializer, collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        StringBuilder expected = new StringBuilder("ArrayList<java.lang.Integer>");
        StringBuilder actual = collectionLikeType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        StringBuilder expected = new StringBuilder("[class java.util.ArrayList<java.lang.Integer>]");
        StringBuilder actual = new StringBuilder();
        collectionLikeType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        StringBuilder expected = new StringBuilder("class java.util.ArrayList<java.lang.Integer>");
        StringBuilder actual = collectionLikeType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testRefine() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        JavaType expected = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        JavaType actual = collectionLikeType.refine(ArrayList.class, bindings, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        Object handler = new Object();
        CollectionLikeType expected = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, javaType.getComponentType(), null, handler, false);
        CollectionLikeType actual = collectionLikeType.withContentTypeHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        Object handler = new Object();
        CollectionLikeType expected = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, javaType.getComponentType(), handler, null, false);
        CollectionLikeType actual = collectionLikeType.withContentValueHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        Object handler = new Object();
        CollectionLikeType expected = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, javaType.getComponentType(), null, handler, false);
        CollectionLikeType actual = collectionLikeType.withTypeHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        Object handler = new Object();
        CollectionLikeType expected = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, javaType.getComponentType(), handler, null, false);
        CollectionLikeType actual = collectionLikeType.withValueHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        CollectionLikeType expected = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, contentType, null, null, false);
        CollectionLikeType actual = collectionLikeType.withContentType(contentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType, TypeBindings.emptyBindings());
        CollectionLikeType expected = new CollectionLikeType(javaType, TypeBindings.emptyBindings(), null, null, javaType.getComponentType(), null, null, true);
        CollectionLikeType actual = collectionLikeType.withStaticTyping();
        assertEquals(expected, actual);
    }

}