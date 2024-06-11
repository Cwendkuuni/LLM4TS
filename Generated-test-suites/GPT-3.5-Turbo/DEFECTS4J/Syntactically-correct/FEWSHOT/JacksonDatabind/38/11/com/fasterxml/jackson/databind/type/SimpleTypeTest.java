
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor1() {
        SimpleType type = new SimpleType(String.class);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testConstructor3() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testConstructor4() {
        SimpleType base = new SimpleType(String.class);
        SimpleType type = new SimpleType(base);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithMapType() {
        SimpleType.construct(Map.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithCollectionType() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithArrayType() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType base = new SimpleType(String.class);
        SimpleType type = (SimpleType) base.withContentType(SimpleType.constructUnsafe(Integer.class));
        assertEquals(String.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertTrue(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertEquals(SimpleType.constructUnsafe(Integer.class), type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType base = new SimpleType(String.class);
        base.withContentType(SimpleType.constructUnsafe(Map.class));
    }

    @Test
    public void testWithValueHandler() {
        SimpleType base = new SimpleType(String.class);
        Object valueHandler = new Object();
        SimpleType type = (SimpleType) base.withValueHandler(valueHandler);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertEquals(valueHandler, type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType base = new SimpleType(String.class);
        SimpleType type = (SimpleType) base.withStaticTyping();
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertTrue(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testRefine() {
        SimpleType base = new SimpleType(String.class);
        SimpleType type = (SimpleType) base.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
        assertFalse(type.hasContentType());
        assertFalse(type.hasGenericTypes());
        assertFalse(type.isContainerType());
        assertNull(type.getKeyType());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isConcrete());
        assertFalse(type.isArrayType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isMapLikeType());
        assertFalse(type.isReferenceType());
    }
}
