package com.fasterxml.jackson.databind.type;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructWithType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isFinal());
        assertFalse(simpleType.isArrayType());
        assertEquals("SimpleType", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapType() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionType() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayType() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isFinal());
        assertFalse(simpleType.isArrayType());
        assertEquals("SimpleType", simpleType.toString());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = (SimpleType) simpleType1.narrow(CharSequence.class);
        assertNotNull(simpleType2);
        assertEquals(CharSequence.class, simpleType2.getRawClass());
        assertFalse(simpleType2.isContainerType());
        assertNull(simpleType2.getKeyType());
        assertNull(simpleType2.getContentType());
        assertNull(simpleType2.getValueHandler());
        assertNull(simpleType2.getTypeHandler());
        assertFalse(simpleType2.isFinal());
        assertFalse(simpleType2.isArrayType());
        assertEquals("SimpleType", simpleType2.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentType(SimpleType.construct(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withTypeHandler(new TypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentTypeHandler(new TypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withValueHandler(new ValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentValueHandler(new ValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = simpleType1.withStaticTyping();
        assertNotNull(simpleType2);
        assertEquals(String.class, simpleType2.getRawClass());
        assertFalse(simpleType2.isContainerType());
        assertNull(simpleType2.getKeyType());
        assertNull(simpleType2.getContentType());
        assertNull(simpleType2.getValueHandler());
        assertNull(simpleType2.getTypeHandler());
        assertFalse(simpleType2.isFinal());
        assertFalse(simpleType2.isArrayType());
        assertEquals("SimpleType", simpleType2.toString());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType refinedType = simpleType.refine(CharSequence.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}