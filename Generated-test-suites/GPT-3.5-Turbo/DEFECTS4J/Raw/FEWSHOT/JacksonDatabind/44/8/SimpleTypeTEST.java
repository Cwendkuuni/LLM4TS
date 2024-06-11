package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Collection;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertEquals("java.lang.String", type.toCanonical());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("IllegalArgumentException should be thrown for Map class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not construct SimpleType for a Map"));
        }
        try {
            SimpleType.construct(Collection.class);
            fail("IllegalArgumentException should be thrown for Collection class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not construct SimpleType for a Collection"));
        }
        try {
            SimpleType.construct(Object[].class);
            fail("IllegalArgumentException should be thrown for array class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not construct SimpleType for an array"));
        }
        
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
        assertEquals("java.lang.String", type.toCanonical());
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType narrowedType = (SimpleType) type.narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
        assertNull(narrowedType.getContentType());
        assertEquals("java.lang.Object", narrowedType.toCanonical());
        
        SimpleType sameType = (SimpleType) type.narrow(String.class);
        assertEquals(type, sameType);
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(List.class);
        try {
            type.withContentType(String.class);
            fail("IllegalArgumentException should be thrown for SimpleType with no content types");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Simple types have no content types"));
        }
    }

    @Test
    public void testWithGenericTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        try {
            type.withTypeHandler(new TypeHandler());
            fail("IllegalArgumentException should be thrown for SimpleType with no generic type handler");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Simple types have no generic type handler"));
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(List.class);
        try {
            type.withContentValueHandler(new ValueHandler());
            fail("IllegalArgumentException should be thrown for SimpleType with no content value handler");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Simple types have no content value handler"));
        }
    }

    @Test
    public void testwithStaticTyping() {
        SimpleType type = SimpleType.construct(String.class);
        SimpleType staticType = type.withStaticTyping();
        assertTrue(staticType.isStatic());
        assertEquals(type.getRawClass(), staticType.getRawClass());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(String.class);
        JavaType refinedType = type.refine(Map.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        SimpleType type3 = SimpleType.constructUnsafe(Integer.class);
        
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        assertFalse(type1.equals(type3));
    }
    
    private static class TypeHandler {}
    
    private static class ValueHandler {}

    private static interface List {}
}