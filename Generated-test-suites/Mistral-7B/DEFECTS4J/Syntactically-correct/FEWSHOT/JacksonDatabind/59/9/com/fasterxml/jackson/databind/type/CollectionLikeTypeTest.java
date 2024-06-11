package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawType(), null);
        assertEquals(javaType.getRawType(), collectionLikeType.getRawType());
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class, TypeVariable.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawType(), new TypeBindings());
        assertEquals(javaType.getRawType(), collectionLikeType.getRawType());
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test
    public void testConstructWithGenericType() {
        JavaType javaType = new TypeFactory().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType);
        assertEquals(javaType.getRawType(), collectionLikeType.getRawType());
        assertEquals(String.class, collectionLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new TypeFactory().constructType(ArrayList.class, String.class);
        JavaType elementType = new TypeFactory().constructType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);
        assertEquals(javaType.getRawType(), collectionLikeType.getRawType());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType javaType = new TypeFactory().constructType(Integer.class);
        JavaType elementType = new TypeFactory().constructType(String.class);
        CollectionLikeType.upgradeFrom(javaType, elementType);
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertEquals(javaType.getComponentType(), collectionLikeType.getContentType());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = new TypeFactory().constructType(List.class, String.class);
        JavaType javaType2 = new TypeFactory().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType1.getRawType(), null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType2.getRawType(), null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(javaType1.getRawType(), null, null, null, null, null, null, false);
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(javaType2.getRawType(), null, null, null, null, null, null, true);

        assertEquals(collectionLikeType1, collectionLikeType1);
        assertEquals(collectionLikeType1, collectionLikeType3);
        assertFalse(collectionLikeType1.equals(null));
        assertFalse(collectionLikeType1.equals(new Object()));
        assertFalse(collectionLikeType1.equals(javaType1));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
        assertFalse(collectionLikeType1.equals(collectionLikeType4));
    }

    @Test
    public void testToString() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        String expected = "[collection-like type; class java.util.List, contains class java.lang.String]";
        assertEquals(expected, collectionLikeType.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertEquals("java.util.List", collectionLikeType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[collection-like type; class java.util.List<T>, T extends java.lang.Object>");
        StringBuilder actual = new StringBuilder();
        collectionLikeType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        assertEquals("java.util.List<java.lang.String>", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testRefine() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        JavaType refinedType = collectionLikeType.refine(javaType.getRawType(), new TypeBindings(), null, null);
        assertSame(javaType, refinedType);
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        JavaType refinedType = collectionLikeType.withContentType(new TypeFactory().constructType(Integer.class));
        assertFalse(javaType.equals(refinedType));
        assertTrue(collectionLikeType.getClass().isAssignableFrom(refinedType.getClass()));
        assertEquals(Integer.class, refinedType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        Object handler = new Object();
        JavaType refinedType = collectionLikeType.withTypeHandler(handler);
        assertFalse(javaType.equals(refinedType));
        assertTrue(collectionLikeType.getClass().isAssignableFrom(refinedType.getClass()));
        assertSame(handler, refinedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        Object handler = new Object();
        JavaType refinedType = collectionLikeType.withContentTypeHandler(handler);
        assertFalse(javaType.equals(refinedType));
        assertTrue(collectionLikeType.getClass().isAssignableFrom(refinedType.getClass()));
        assertEquals(handler, refinedType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        Object handler = new Object();
        JavaType refinedType = collectionLikeType.withValueHandler(handler);
        assertFalse(javaType.equals(refinedType));
        assertTrue(collectionLikeType.getClass().isAssignableFrom(refinedType.getClass()));
        assertSame(handler, refinedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        Object handler = new Object();
        JavaType refinedType = collectionLikeType.withContentValueHandler(handler);
        assertFalse(javaType.equals(refinedType));
        assertTrue(collectionLikeType.getClass().isAssignableFrom(refinedType.getClass()));
        assertEquals(handler, refinedType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new TypeFactory().constructType(List.class, String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), null, null, null, null, null, null, false);
        JavaType refinedType = collectionLikeType.withStaticTyping();
        assertFalse(javaType.equals(refinedType));
        assertTrue(collectionLikeType.getClass().isAssignableFrom(refinedType.getClass()));
        assertTrue(refinedType.isStatic());
    }
}