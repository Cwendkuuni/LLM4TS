package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static junit.framework.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType type = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SimpleType type = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            SimpleType type = SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType narrowedType = (SimpleType) type.narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        type.withContentType(SimpleType.constructUnsafe(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        type.withContentTypeHandler("handler");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        type.withContentValueHandler("handler");
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTyping = (SimpleType) type.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = type.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = type.getErasedSignature(sb);
        assertNotNull(erasedSignature);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = type.getGenericSignature(sb);
        assertNotNull(genericSignature);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        String string = type.toString();
        assertNotNull(string);
        assertEquals("[simple type, class java.lang.String]", string);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        SimpleType type3 = SimpleType.constructUnsafe(Integer.class);

        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
        assertNotEquals(type1, "string");
        assertNotEquals(type1, null);
    }
}