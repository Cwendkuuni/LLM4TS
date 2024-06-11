package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
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
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType base = SimpleType.constructUnsafe(CharSequence.class);
        SimpleType type = base.narrow(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        try {
            base.withContentType(String.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; cannot call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType type = base.withTypeHandler(handler);
        assertEquals(handler, type.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        try {
            base.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; cannot call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        Object handler = new Object();
        SimpleType type = base.withValueHandler(handler);
        assertEquals(handler, type.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        try {
            base.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; cannot call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        SimpleType type = base.withStaticTyping();
        assertEquals(true, type.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        SimpleType type = base.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(type);
    }

    @Test
    public void testIsContainerType() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        assertEquals(false, base.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        base.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        base.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType base = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", base.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertEquals(true, type1.equals(type2));
    }
}