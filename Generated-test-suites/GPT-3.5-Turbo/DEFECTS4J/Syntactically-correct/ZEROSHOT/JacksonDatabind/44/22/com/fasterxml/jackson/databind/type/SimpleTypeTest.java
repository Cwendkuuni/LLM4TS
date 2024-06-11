package com.fasterxml.jackson.databind.type;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        SimpleType narrowedType = (SimpleType) simpleType.narrow(Integer.class);
        assertNotNull(narrowedType);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        simpleType.narrow(String.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(List.class);
        try {
            simpleType.withContentType(SimpleType.construct(String.class));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Set.class);
        try {
            simpleType.withContentTypeHandler("handler");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(Float.class);
        SimpleType typeWithNewValueHandler = (SimpleType) simpleType.withValueHandler("handler");
        assertNotNull(typeWithNewValueHandler);
        assertEquals(Float.class, typeWithNewValueHandler.getRawClass());
        assertEquals("handler", typeWithNewValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(Double.class);
        try {
            simpleType.withContentValueHandler("handler");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Long.class);
        SimpleType typeWithStaticTyping = simpleType.withStaticTyping();
        assertNotNull(typeWithStaticTyping);
        assertEquals(Long.class, typeWithStaticTyping.getRawClass());
        assertTrue(typeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        assertNull(simpleType.refine(Integer.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Double.class);
        StringBuilder signature = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(signature);
        assertEquals("Ljava/lang/Double;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Float.class);
        StringBuilder signature = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(signature);
        assertEquals("Ljava/lang/Float;", signature.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(Boolean.class);
        assertEquals("java.lang.Boolean", simpleType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Double.class);
        SimpleType simpleType2 = SimpleType.construct(Double.class);
        SimpleType simpleType3 = SimpleType.construct(Float.class);
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

}