package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertEquals(1, type.containedTypeCount());
        assertEquals("java.lang.String", type.toCanonical());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
        assertNull(type.getContentType());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isContainerType());
        assertEquals(1, type.containedTypeCount());
        assertEquals("java.lang.Integer", type.toCanonical());
        assertTrue(type.equals(type));
        assertFalse(type.equals(null));
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = SimpleType.construct(Object.class);
        SimpleType subtype = (SimpleType) baseType.narrow(SimpleType.class);
        assertEquals(SimpleType.class, subtype.getRawClass());
        assertEquals(baseType.containedType(0), subtype.containedType(0));
        assertEquals(baseType.getBindings(), subtype.getBindings());
        assertEquals(baseType.getValueHandler(), subtype.getValueHandler());
        assertEquals(baseType.getTypeHandler(), subtype.getTypeHandler());
        assertEquals(baseType.isContainerType(), subtype.isContainerType());
        assertFalse(subtype.isReferenceType());
        assertEquals("com.fasterxml.jackson.databind.type.SimpleType", subtype.toCanonical());
        assertTrue(subtype.equals(subtype));
        assertFalse(subtype.equals(null));
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(Collection.class);
        try {
            type.withContentType(SimpleType.construct(String.class));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(Map.class);
        Object handler = new Object();
        SimpleType withHandler = type.withTypeHandler(handler);
        assertSame(handler, withHandler.getTypeHandler());
        assertEquals(withHandler, withHandler);
        assertFalse(withHandler.equals(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(List.class);
        try {
            type.withContentTypeHandler(new Object());
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(Set.class);
        SimpleType staticType = type.withStaticTyping();
        assertTrue(staticType.isStatic());
        assertEquals(staticType, staticType);
        assertFalse(staticType.equals(null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType type = SimpleType.construct(Object.class);
        assertEquals("java.lang.Object", type.buildCanonicalName());
        SimpleType parameterizedType = SimpleType.construct(Collection.class)
                .withContentType(SimpleType.construct(String.class));
        assertEquals("java.util.Collection<java.lang.String>", parameterizedType.buildCanonicalName());
        SimpleType multiParameterizedType = SimpleType.construct(Map.class)
                .withContentType(SimpleType.construct(String.class))
                .withContentType(SimpleType.construct(Integer.class));
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", multiParameterizedType.buildCanonicalName());
    }
}