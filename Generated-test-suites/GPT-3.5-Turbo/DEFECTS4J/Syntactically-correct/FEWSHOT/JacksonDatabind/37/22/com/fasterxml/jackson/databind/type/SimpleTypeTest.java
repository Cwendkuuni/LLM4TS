package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
        assertTrue(simpleType.hasRawClass(String.class));
        assertFalse(simpleType.hasContentType());
        assertFalse(simpleType.hasValueHandler());
        assertFalse(simpleType.hasTypeHandler());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
        assertTrue(simpleType.hasRawClass(String.class));
        assertFalse(simpleType.hasContentType());
        assertFalse(simpleType.hasValueHandler());
        assertFalse(simpleType.hasTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        JavaType contentType = SimpleType.constructUnsafe(String.class);

        JavaType result = simpleType.withContentType(contentType);

        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertTrue(result.isContainerType());
        assertTrue(result.hasRawClass(List.class));
        assertTrue(result.hasContentType());
        assertFalse(result.hasValueHandler());
        assertFalse(result.hasTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();

        SimpleType result = simpleType.withTypeHandler(typeHandler);

        assertEquals(String.class, result.getRawClass());
        assertNull(result.getContentType());
        assertNull(result.getValueHandler());
        assertEquals(typeHandler, result.getTypeHandler());
        assertFalse(result.isContainerType());
        assertTrue(result.hasRawClass(String.class));
    }

    @Test
    public void testWithContentTypeHandlerThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);

        try {
            simpleType.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object valueHandler = new Object();

        SimpleType result = simpleType.withValueHandler(valueHandler);

        assertEquals(String.class, result.getRawClass());
        assertNull(result.getContentType());
        assertEquals(valueHandler, result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isContainerType());
        assertTrue(result.hasRawClass(String.class));
    }

    @Test
    public void testWithContentValueHandlerThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);

        try {
            simpleType.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        SimpleType result = simpleType.withStaticTyping();

        assertEquals(String.class, result.getRawClass());
        assertNull(result.getContentType());
        assertNull(result.getValueHandler());
        assertNull(result.getTypeHandler());
        assertFalse(result.isContainerType());
        assertTrue(result.hasRawClass(String.class));
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        JavaType result = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);

        assertNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        String result = simpleType.buildCanonicalName();

        assertEquals("java.lang.String", result);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);

        assertEquals("class java.lang.String", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);

        assertEquals("Ljava/lang/String;", result.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);

        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        String result = simpleType.toString();

        assertEquals("[simple type, class java.lang.String]", result);
    }
}