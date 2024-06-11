
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeForArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentTypeHandler("Handler");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        Object handler = new Object();
        SimpleType modifiedType = simpleType.withValueHandler(handler);
        assertEquals(handler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContenValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentValueHandler("Handler");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertTrue(modifiedType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertNull(simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertEquals("java.util.List", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = new StringBuilder();
        sb = simpleType.getErasedSignature(sb);
        assertEquals("Ljava/util/List;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = new StringBuilder();
        sb = simpleType.getGenericSignature(sb);
        assertEquals("Ljava/util/List<*>;", sb.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(List.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(List.class);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        assertEquals("[simple type, class java.util.List]", simpleType.toString());
    }
}

