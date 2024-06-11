
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow_SameClass() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = simpleType1.narrow(String.class);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testWithContentType_ThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler_ThrowsException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler(new MyTypeHandler());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping_ReturnsNewInstance() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertNotSame(simpleType, newSimpleType);
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine_ReturnsNull() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    private static class MyTypeHandler {
    }
}
