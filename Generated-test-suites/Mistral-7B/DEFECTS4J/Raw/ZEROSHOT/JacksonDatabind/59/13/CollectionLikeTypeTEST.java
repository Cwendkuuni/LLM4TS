package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * CollectionLikeTypeTest.java - tests for CollectionLikeType class
 *
 * @author Johnson Shu
 */
public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        // Given:
        JavaType elementType = JavaType.construct(int[].class);
        TypeBindings bindings = TypeBindings.emptyBindings();

        // When:
        JavaType javaType1 = CollectionLikeType.construct(ArrayList.class, bindings, null, null, elementType);
        JavaType javaType2 = CollectionLikeType.construct(ArrayList.class, elementType);
        JavaType javaType3 = CollectionLikeType.construct(LinkedList.class, bindings, null, null, elementType);
        JavaType javaType4 = CollectionLikeType.construct(LinkedList.class, elementType);

        // Then:
        assertNotNull(javaType1);
        assertSame(ArrayList.class, javaType1.getRawClass());
        assertEquals(elementType, javaType1.getContentType());

        assertNotNull(javaType2);
        assertSame(ArrayList.class, javaType2.getRawClass());
        assertEquals(elementType, javaType2.getContentType());

        assertNotNull(javaType3);
        assertSame(LinkedList.class, javaType3.getRawClass());
        assertEquals(elementType, javaType3.getContentType());

        assertNotNull(javaType4);
        assertSame(LinkedList.class, javaType4.getRawClass());
        assertEquals(elementType, javaType4.getContentType());
    }

    @Test
    public void testCollectionLikeTypeUpgrade() {
        // Given:
        JavaType baseType = JavaType.construct(ArrayList.class);
        JavaType elementType = JavaType.construct(int.class);

        // When:
        CollectionLikeType collectionType1 = CollectionLikeType.upgradeFrom(baseType, elementType);

        // Then:
        assertNotNull(collectionType1);
        assertEquals(ArrayList.class, collectionType1.getRawClass());
        assertEquals(elementType, collectionType1.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionLikeTypeUpgradeWithInvalidBaseType() {
        // Given:
        JavaType baseType = JavaType.construct(String.class);
        JavaType elementType = JavaType.construct(int.class);

        // When:
        CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(baseType, elementType);

        // Then:
        // Assertion error should be thrown
    }

    @Test
    public void testCollectionLikeTypeNarrowing() {
        // Given:
        JavaType baseType = JavaType.construct(ArrayList.class);
        JavaType collectionType = CollectionLikeType.construct(LinkedList.class, baseType, JavaType.construct(int.class));

        // When:
        JavaType narrowedType = collectionType.narrow(ArrayList.class);

        // Then:
        assertNotNull(narrowedType);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(JavaType.construct(int.class), narrowedType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        // Given:
        JavaType javaType = JavaType.construct(ArrayList.class);

        // When:
        boolean result = javaType.isContainerType();

        // Then:
        assertTrue(result);
    }

    @Test
    public void testCollectionLikeTypeIsCollectionLikeType() {
        // Given:
        JavaType javaType = JavaType.construct(ArrayList.class);

        // When:
        boolean result = javaType.isCollectionLikeType();

        // Then:
        assertTrue(result);
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        // Given:
        JavaType javaType = JavaType.construct(ArrayList.class);

        // When:
        boolean result = javaType.isTrueCollectionType();

        // Then:
        assertTrue(result);

        JavaType javaType2 = JavaType.construct(HashMap.class);

        // When:
        assertFalse(javaType2.isTrueCollectionType());
    }

    @Test
    public void testCollectionLikeTypeGetContentType() {
        // Given:
        JavaType javaType = JavaType.construct(ArrayList.class);

        // When:
        JavaType contentType = javaType.getContentType();

        // Then:
        assertNotNull(contentType);
        assertEquals(JavaType.construct(Object.class), contentType);

        JavaType javaType2 = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        JavaType contentType2 = javaType2.getContentType();

        // Then:
        assertNotNull(contentType2);
        assertEquals(JavaType.construct(int.class), contentType2);
    }

    @Test
    public void testCollectionLikeTypeToString() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        String result = javaType.toString();

        // Then:
        assertTrue(result.contains("[collection-like type"));
        assertTrue(result.contains("class ArrayList<"));
        assertTrue(result.contains("contains " + JavaType.construct(int.class).toString()));
    }

    @Test
    public void testCollectionLikeTypeToCanonical() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        String result = javaType.toCanonical();

        // Then:
        assertTrue(result.contains("[Ljava.util.ArrayList;"));
        assertTrue(result.contains("Ljava/lang/Integer;"));
    }

    @Test
    public void testCollectionLikeTypeGenericSignature() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);

        // Then:
        String result = sb.toString();

        // Then:
        assertTrue(result.contains("<T>"));
        assertTrue(result.contains("java.util.ArrayList<T>"));
        assertTrue(result.contains("<T extends java.lang.Object, java.lang.Comparable<T>>"));
        assertTrue(result.contains("java.lang.Integer>"));
    }

    @Test
    public void testCollectionLikeTypeErasedSignature() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);

        // Then:
        String result = sb.toString();

        // Then:
        assertTrue(result.contains("java.util.ArrayList"));
    }

    @Test
    public void testCollectionLikeTypeWithContentTypeHandler() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));
        Object handler = new Object() {};

        // When:
        JavaType javaType2 = javaType.withContentTypeHandler(handler);

        // Then:
        assertEquals(javaType, javaType2);
        assertNotNull(javaType2.getValueHandler());
        assertEquals(handler, javaType2.getValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentTypeHandlerNull() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        JavaType javaType2 = javaType.withContentTypeHandler(null);

        // Then:
        assertEquals(javaType, javaType2);
        assertNull(javaType2.getValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithTypeHandler() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));
        Object handler = new Object() {};

        // When:
        JavaType javaType2 = javaType.withTypeHandler(handler);

        // Then:
        assertEquals(javaType, javaType2);
        assertNotNull(javaType2.getTypeHandler());
        assertEquals(handler, javaType2.getTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentValueHandler() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));
        Object handler = new Object() {};

        // When:
        JavaType javaType2 = javaType.withContentValueHandler(handler);

        // Then:
        assertEquals(javaType, javaType2);
        assertNotNull(javaType2.getValueHandler());
        assertEquals(handler, javaType2.getValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentValueHandlerNull() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        JavaType javaType2 = javaType.withContentValueHandler(null);

        // Then:
        assertEquals(javaType, javaType2);
        assertNull(javaType2.getValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithStaticTyping() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        JavaType javaType2 = javaType.withStaticTyping();

        // Then:
        assertEquals(javaType, javaType2);
        assertTrue(javaType2.isStaticTyping());
    }

    @Test
    public void testCollectionLikeTypeRefine() {
        // Given:
        JavaType rawType = ArrayList.class;
        JavaType javaType = JavaType.construct(rawType, TypeBindings.emptyBindings(), null, null, JavaType.construct(int.class));

        // When:
        JavaType javaTypeRefined = javaType.refine(LinkedList.class, TypeBindings.emptyBindings(), null, null);

        // Then:
        assertNotNull(javaTypeRefined);
        assertEquals(LinkedList.class, javaTypeRefined.getRawClass());
        assertEquals(JavaType.construct(int.class), javaTypeRefined.getContentType());
    }

    @Test
    public void testCollectionLikeTypeHasHandlers() {
        // Given:
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class));

        // When:
        boolean hasHandlers = javaType.hasHandlers();

        // Then:
        assertTrue(hasHandlers);

        JavaType javaType2 = CollectionLikeType.construct(ArrayList.class, JavaType.construct(int.class), null, null);

        // When:
        boolean hasHandlers2 = javaType2.hasHandlers();

        // Then:
        assertFalse(hasHandlers2);
    }
}