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
    public void testIsTrueCollectionType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(CollectionLikeType.class.isAssignableFrom(listType.getClass()));
        assertTrue(CollectionLikeType.class.isInstance(listType));
        assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), null, null, false)));
        assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(Map.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class), null, null, false)));
        assertFalse(CollectionLikeType.class.isInstance(TypeFactory.defaultInstance().constructType(String.class)));
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(CollectionLikeType.class.isAssignableFrom(listType.getClass()));
        assertTrue(CollectionLikeType.class.isInstance(listType));
        assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), null, null, false)));
        assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(Map.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class), null, null, false)));
        assertTrue(listType.isContainerType());
        assertFalse(TypeFactory.defaultInstance().constructType(String.class).isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(CollectionLikeType.class.isAssignableFrom(listType.getClass()));
        assertTrue(CollectionLikeType.class.isInstance(listType));
        assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), null, null, false)));
        assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(Map.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(String.class), null, null, false)));
        assertTrue(CollectionLikeType.class.isInstance(listType));
        assertTrue(listType.isCollectionLikeType());
        assertFalse(TypeFactory.defaultInstance().constructType(String.class).isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType expectedElementType = TypeFactory.defaultInstance().constructType(Integer.class);
        assertEquals(expectedElementType, CollectionLikeType.class.cast(listType).getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertNull(CollectionLikeType.class.cast(listType).getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertNull(CollectionLikeType.class.cast(listType).getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertFalse(CollectionLikeType.class.cast(listType).hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]", CollectionLikeType.class.cast(listType).getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertEquals("[collection-like type; class java.util.ArrayList<T>, contains class java.lang.Integer]", CollectionLikeType.class.cast(listType).getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]", CollectionLikeType.class.cast(listType).buildCanonicalName());
    }
}