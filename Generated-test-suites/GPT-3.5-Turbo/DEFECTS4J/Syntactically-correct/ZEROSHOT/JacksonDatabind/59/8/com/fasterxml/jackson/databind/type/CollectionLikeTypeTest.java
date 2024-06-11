package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    CollectionLikeType collectionLikeType;

    @Before
    public void setUp() {
        collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(collectionLikeType);
        assertEquals(Collection.class, collectionLikeType._class);
        assertNull(collectionLikeType._bindings);
        assertNull(collectionLikeType._superClass);
        assertNull(collectionLikeType._superInterfaces);
        assertNull(collectionLikeType._elementType);
        assertNull(collectionLikeType._valueHandler);
        assertNull(collectionLikeType._typeHandler);
        assertFalse(collectionLikeType._asStatic);
    }

    @Test
    public void testConstruct() {
        CollectionLikeType result = CollectionLikeType.construct(Collection.class, null, null, null, null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testConstructDeprecated() {
        CollectionLikeType result = CollectionLikeType.construct(Collection.class, null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testUpgradeFromInvalid() {
        JavaType baseType = new SimpleType(Collection.class);
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeBase();
        JavaType elementType = new SimpleType(String.class);
        CollectionLikeType result = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(result);
        assertEquals(baseType, result._superClass);
        assertEquals(elementType, result._elementType);
    }

    @Test
    public void test_narrow() {
        JavaType result = collectionLikeType._narrow(String.class);
        assertNotNull(result);
        assertEquals(String.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithContentType() {
        JavaType result = collectionLikeType.withContentType(null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType result = collectionLikeType.withTypeHandler(null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType result = collectionLikeType.withContentTypeHandler(null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        CollectionLikeType result = collectionLikeType.withValueHandler(null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType result = collectionLikeType.withContentValueHandler(null);
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = collectionLikeType.withStaticTyping();
        assertNotNull(result);
        assertEquals(Collection.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType result = collectionLikeType.refine(String.class, null, null, null);
        assertNotNull(result);
        assertEquals(String.class, result._class);
        assertNull(result._bindings);
        assertNull(result._superClass);
        assertNull(result._superInterfaces);
        assertNull(result._elementType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testIsContainerType() {
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        assertNull(collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = collectionLikeType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(collectionLikeType._class.getName(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = collectionLikeType.getGenericSignature(sb);
        assertNotNull(result);
        assertTrue(result.toString().contains(collectionLikeType._class.getName()));
        assertNull(collectionLikeType._elementType);
    }

    @Test
    public void testBuildCanonicalName() {
        String result = collectionLikeType.buildCanonicalName();
        assertNotNull(result);
        assertTrue(result.contains(collectionLikeType._class.getName()));
        assertNull(collectionLikeType._elementType);
    }

    @Test
    public void testIsTrueCollectionType() {
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        assertTrue(collectionLikeType.equals(collectionLikeType));
    }

    @Test
    public void testToString() {
        String result = collectionLikeType.toString();
        assertNotNull(result);
        assertTrue(result.contains(collectionLikeType._class.getName()));
        assertNotNull(collectionLikeType._elementType);
    }
}