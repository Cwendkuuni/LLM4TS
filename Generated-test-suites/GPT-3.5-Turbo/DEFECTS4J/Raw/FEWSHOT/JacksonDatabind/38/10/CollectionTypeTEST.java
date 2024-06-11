package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {
    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(Collection.class, TypeBindings.emptyBindings(),
                null, null, TypeFactory.defaultInstance().constructType(String.class));
    }

    @Test
    public void testConstruct() {
        assertEquals(Collection.class, collectionType.getRawClass());
        assertTrue(collectionType.isCollectionLikeType());
        assertFalse(collectionType.isMapLikeType());
        assertNull(collectionType.getKeyType());
        assertNotNull(collectionType.getContentType());
        assertEquals(String.class, collectionType.getContentType().getRawClass());
    }

    @Test
    public void testConstructWithSuperClassAndSuperInterfaces() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(SuperClass.class);
        JavaType[] superInts = { TypeFactory.defaultInstance().constructType(SuperInterface.class) };
        collectionType = CollectionType.construct(Collection.class, TypeBindings.emptyBindings(),
                superClass, superInts, TypeFactory.defaultInstance().constructType(String.class));

        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts[0], collectionType.getInterfaces().get(0));
        assertEquals(String.class, collectionType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);

        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(newContentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);

        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
        JavaType contentType = newCollectionType.getContentType();

        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(typeHandler, newCollectionType.getContentTypeHandler());
        assertEquals(typeHandler, contentType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);

        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler);
        JavaType contentType = newCollectionType.getContentType();

        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(valueHandler, newCollectionType.getContentValueHandler());
        assertEquals(valueHandler, contentType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType newCollectionType = collectionType.withStaticTyping();

        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertTrue(newCollectionType.isJavaLangObject());
        assertTrue(newCollectionType.getContentType().isJavaLangObject());
    }

    @Test
    public void testRefine() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(SuperClass.class);
        JavaType[] superInts = { TypeFactory.defaultInstance().constructType(SuperInterface.class) };
        CollectionType refinedType = collectionType.refine(Collection.class, TypeBindings.emptyBindings(),
                superClass, superInts);

        assertEquals(Collection.class, refinedType.getRawClass());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInts[0], refinedType.getInterfaces().get(0));
        assertEquals(String.class, refinedType.getContentType().getRawClass());
    }

    @Test
    public void testToString() {
        String stringValue = collectionType.toString();

        assertTrue(stringValue.contains("[collection type;"));
        assertTrue(stringValue.contains("class java.util.Collection"));
        assertTrue(stringValue.contains("contains class java.lang.String"));
    }
}