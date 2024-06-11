package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionLikeType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionLikeType c = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(c);
        assertEquals(rawType, c._class);
        assertEquals(bindings, c._bindings);
        assertEquals(superClass, c._superClass);
        assertArrayEquals(superInts, c._superInterfaces);
        assertEquals(elemT, c._elementType);
        assertNull(c._valueHandler);
        assertNull(c._typeHandler);
        assertFalse(c._asStatic);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType elementType = null;

        try {
            CollectionLikeType.upgradeFrom(baseType, elementType);
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not upgrade from an instance of null", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;

        CollectionLikeType c = new CollectionLikeType(subclass, null, null, null, null, null, null, false);

        JavaType narrowed = c._narrow(subclass);

        assertNotNull(narrowed);
        assertTrue(narrowed instanceof CollectionLikeType);
        assertEquals(subclass, narrowed._class);
        assertNull(narrowed._bindings);
        assertNull(narrowed._superClass);
        assertArrayEquals(null, narrowed._superInterfaces);
        assertNull(narrowed._elementType);
        assertNull(narrowed._valueHandler);
        assertNull(narrowed._typeHandler);
        assertFalse(narrowed._asStatic);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionLikeType c = new CollectionLikeType(null, null, null, null, null, null, null, false);

        JavaType withContentType = c.withContentType(contentType);

        assertNotNull(withContentType);
        assertTrue(withContentType instanceof CollectionLikeType);
        assertEquals(null, withContentType._class);
        assertNull(withContentType._bindings);
        assertNull(withContentType._superClass);
        assertArrayEquals(null, withContentType._superInterfaces);
        assertEquals(contentType, withContentType._elementType);
        assertNull(withContentType._valueHandler);
        assertNull(withContentType._typeHandler);
        assertFalse(withContentType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        CollectionLikeType c = new CollectionLikeType(null, null, null, null, null, null, null, false);

        CollectionLikeType withTypeHandler = c.withTypeHandler(h);

        assertNotNull(withTypeHandler);
        assertTrue(withTypeHandler instanceof CollectionLikeType);
        assertEquals(null, withTypeHandler._class);
        assertNull(withTypeHandler._bindings);
        assertNull(withTypeHandler._superClass);
        assertArrayEquals(null, withTypeHandler._superInterfaces);
        assertNull(withTypeHandler._elementType);
        assertNull(withTypeHandler._valueHandler);
        assertEquals(h, withTypeHandler._typeHandler);
        assertFalse(withTypeHandler._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        CollectionLikeType c = new CollectionLikeType(null, null, null, null, null, null, null, false);

        CollectionLikeType withContentTypeHandler = c.withContentTypeHandler(h);

        assertNotNull(withContentTypeHandler);
        assertTrue(withContentTypeHandler instanceof CollectionLikeType);
        assertEquals(null, withContentTypeHandler._class);
        assertNull(withContentTypeHandler._bindings);
        assertNull(withContentTypeHandler._superClass);
        assertArrayEquals(null, withContentTypeHandler._superInterfaces);
        assertNull(withContentTypeHandler._elementType);
        assertNull(withContentTypeHandler._valueHandler);
        assertNotNull(withContentTypeHandler._elementType);
        assertEquals(h, withContentTypeHandler._typeHandler);
        assertFalse(withContentTypeHandler._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionLikeType c = new CollectionLikeType(null, null, null, null, null, null, null, false);

        CollectionLikeType withStaticTyping = c.withStaticTyping();

        assertNotNull(withStaticTyping);
        assertTrue(withStaticTyping instanceof CollectionLikeType);
        assertEquals(null, withStaticTyping._class);
        assertNull(withStaticTyping._bindings);
        assertNull(withStaticTyping._superClass);
        assertArrayEquals(null, withStaticTyping._superInterfaces);
        assertNull(withStaticTyping._elementType);
        assertNull(withStaticTyping._valueHandler);
        assertNull(withStaticTyping._typeHandler);
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> rawType = CollectionLikeType.class;
        JavaType elemT = null;
        CollectionLikeType c = new CollectionLikeType(rawType, null, null, null, elemT, null, null, false);

        String canonicalName = c.buildCanonicalName();

        assertNotNull(canonicalName);
        assertEquals(rawType.getName(), canonicalName);
    }

    @Test
    public void testIsTrueCollectionType() {
        Class<?> rawType = CollectionLikeType.class;
        JavaType elemT = null;
        CollectionLikeType c = new CollectionLikeType(rawType, null, null, null, elemT, null, null, false);

        boolean isTrueCollectionType = c.isTrueCollectionType();

        assertTrue(isTrueCollectionType);
    }

    @Test
    public void testEquals() {
        Class<?> rawType = CollectionLikeType.class;
        JavaType elemT = null;
        CollectionLikeType c1 = new CollectionLikeType(rawType, null, null, null, elemT, null, null, false);
        CollectionLikeType c2 = new CollectionLikeType(rawType, null, null, null, elemT, null, null, false);

        boolean equals = c1.equals(c2);

        assertTrue(equals);
    }

    @Test
    public void testToString() {
        Class<?> rawType = CollectionLikeType.class;
        JavaType elemT = null;
        CollectionLikeType c = new CollectionLikeType(rawType, null, null, null, elemT, null, null, false);

        String str = c.toString();

        assertNotNull(str);
        assertTrue(str.contains(rawType.getName()));
        assertTrue(str.contains(String.valueOf(elemT)));
    }

}