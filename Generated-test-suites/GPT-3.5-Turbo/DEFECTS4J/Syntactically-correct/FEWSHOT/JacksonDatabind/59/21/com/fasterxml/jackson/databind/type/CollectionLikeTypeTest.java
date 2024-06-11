package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

public class CollectionLikeTypeTest {
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testIsNotTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(TestClass.class, null, null, null, null, null, null, false);
        assertFalse(type.isTrueCollectionType());
    }

    @Test
    public void testGetElementType() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertEquals(elementType, type.getElementType());
    }

    @Test
    public void testGetContentValueHandlerNotNull() {
        JavaType elementType = JavaType.builder().withValueHandler("valueHandler").build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertNotNull(type.getContentValueHandler());
    }

    @Test
    public void testGetContentValueHandlerNull() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertNull(type.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandlerNotNull() {
        JavaType elementType = JavaType.builder().withTypeHandler("typeHandler").build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertNotNull(type.getContentTypeHandler());
    }

    @Test
    public void testGetContentTypeHandlerNull() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertNull(type.getContentTypeHandler());
    }

    @Test
    public void testHasHandlersTrue() {
        JavaType elementType = JavaType.builder().withValueHandler("valueHandler").build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertTrue(type.hasHandlers());
    }

    @Test
    public void testHasHandlersFalse() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertFalse(type.hasHandlers());
    }

    @Test
    public void testBuildCanonicalNameWithElementType() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);

        String expectedName = "java.util.Collection<com.fasterxml.jackson.databind.JavaType>";
        assertEquals(expectedName, type.buildCanonicalName());
    }

    @Test
    public void testBuildCanonicalNameWithoutElementType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);

        String expectedName = "java.util.Collection";
        assertEquals(expectedName, type.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type1 = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        CollectionLikeType type2 = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testNotEquals() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type1 = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        CollectionLikeType type2 = new CollectionLikeType(TestClass.class, null, null, null, elementType, null, null, false);
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testToString() {
        JavaType elementType = JavaType.builder().build();
        CollectionLikeType type = new CollectionLikeType(Collection.class, null, null, null, elementType, null, null, false);
        String expectedString = "[collection-like type; class java.util.Collection, contains com.fasterxml.jackson.databind.JavaType]";
        assertEquals(expectedString, type.toString());
    }

    private static class TestClass {
        // used for testing
    }
}