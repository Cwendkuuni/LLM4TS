package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    // Basic test cases for each method

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        JavaType javaType = new JavaType(String[].class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertFalse(collectionLikeType1.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType javaType = new JavaType(String[].class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertFalse(collectionLikeType1.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new JavaType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, javaType, null, null, false);
        assertEquals(javaType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = new JavaType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, javaType, null, null, false);
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = new JavaType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, javaType, null, null, false);
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = new JavaType(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, javaType, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("collection-like type; class java.util.Collection, contains java.lang.Object", collectionLikeType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaTypeElement = new JavaType(String.class);
        JavaType javaType = new JavaType(Collection.class, javaTypeElement);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("collection-like type; class java.util.Collection<java.lang.String>", collectionLikeType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaTypeElement = new JavaType(String.class);
        JavaType javaType = new JavaType(Collection.class, javaTypeElement);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection<java.lang.String>]", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType javaType1 = new JavaType(String[].class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType1.getRawClass(), null, null, null, null, null, null, false);
        assertFalse(collectionLikeType1.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType1.equals(collectionLikeType2));

        CollectionLikeType collectionLikeType3 = new CollectionLikeType(ArrayList.class, null, null, null, null, null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType3));
    }

    @Test
    public void testToString() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.Object]", collectionLikeType.toString());
    }

    // Test cases for constructors

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        Class<?> rawType = String[].class;
        JavaType elemT = new JavaType(Integer.class);
        CollectionLikeType.construct(rawType, elemT);
    }

    @Test
    public void testConstructWithValidBaseTypeAndElementType() {
        Class<?> rawType = Collection.class;
        JavaType elemT = new JavaType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, null, null, null, elemT);
        assertEquals(rawType, collectionLikeType.getRawClass());
        assertEquals(elemT, collectionLikeType.getElementType());
    }

    @Test
    public void testConstructWithRawTypeAndElementType() {
        Class<?> rawType = Collection.class;
        JavaType elemT = new JavaType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(rawType, elemT);
        assertEquals(rawType, collectionLikeType.getRawClass());
        assertEquals(elemT, collectionLikeType.getElementType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = new JavaType(String.class);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(rawType, null);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.upgradeFrom(collectionLikeType1.getType(), elemT);
        assertEquals(rawType, collectionLikeType2.getRawClass());
        assertEquals(elemT, collectionLikeType2.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new JavaType(String.class);
        JavaType elemT = new JavaType(Integer.class);
        CollectionLikeType.upgradeFrom(baseType, elemT);
    }

    // Test cases for methods with handlers

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Object handler = new Object();
        CollectionLikeType collectionLikeType1 = collectionLikeType.withContentTypeHandler(handler);
        assertEquals(handler, collectionLikeType1.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Object handler = new Object();
        CollectionLikeType collectionLikeType1 = collectionLikeType.withValueHandler(handler);
        assertEquals(handler, collectionLikeType1.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Object handler = new Object();
        CollectionLikeType collectionLikeType1 = collectionLikeType.withContentValueHandler(handler);
        assertEquals(handler, collectionLikeType1.getContentValueHandler());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Object handler = new Object();
        CollectionLikeType collectionLikeType1 = collectionLikeType.withTypeHandler(handler);
        assertEquals(handler, collectionLikeType1.getTypeHandler());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        JavaType contentType = new JavaType(String.class);
        CollectionLikeType collectionLikeType1 = collectionLikeType.withContentType(contentType);
        assertEquals(contentType, collectionLikeType1.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType1 = collectionLikeType.withStaticTyping();
        assertTrue(collectionLikeType1.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType javaType = new JavaType(Collection.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = new JavaType(String.class);
        CollectionLikeType collectionLikeType1 = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces, elemT);
        assertEquals(rawType, collectionLikeType1.getRawClass());
        assertEquals(elemT, collectionLikeType1.getElementType());
    }
}