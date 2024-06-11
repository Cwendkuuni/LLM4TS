package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getTypeHandler());
        assertNull(simpleType.getValueHandler());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType staticType = simpleType.withStaticTyping();

        assertEquals(simpleType, staticType);
        assertNotSame(simpleType, staticType);
        assertTrue(simpleType.isContainerType() == staticType.isContainerType());
        assertTrue(simpleType.getContentType() == staticType.getContentType());
        assertTrue(simpleType.getTypeHandler() == staticType.getTypeHandler());
        assertTrue(simpleType.getValueHandler() == staticType.getValueHandler());
        assertTrue(simpleType.getBindings() == staticType.getBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        String canonicalName = simpleType.buildCanonicalName();

        assertNotNull(canonicalName);
        assertFalse(canonicalName.isEmpty());
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        StringBuilder erasedSignature = simpleType.getErasedSignature(new StringBuilder());

        assertNotNull(erasedSignature);
        assertFalse(erasedSignature.toString().isEmpty());
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        StringBuilder genericSignature = simpleType.getGenericSignature(new StringBuilder());

        assertNotNull(genericSignature);
        assertFalse(genericSignature.toString().isEmpty());
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }

    @Test
    public void testGetTypeParameters() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);

        TypeBindings typeBindings = simpleType.getBindings();

        assertNotNull(typeBindings);
        assertTrue(typeBindings.size() == 0);
    }

}