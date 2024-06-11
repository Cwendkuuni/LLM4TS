package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testConstructUnsafe() {
        JavaType javaType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, javaType.getRawClass());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperTypes());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Map() {
        SimpleType.construct(Map.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Collection() {
        SimpleType.construct(Collection.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Array() {
        SimpleType.construct(int[].class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNarrow() {
        JavaType javaType = SimpleType.constructUnsafe(String.class);
        JavaType narrowed = javaType.narrow(StringBuilder.class);
        assertEquals(StringBuilder.class, narrowed.getRawClass());
        assertNotNull(narrowed.getSuperClass());
        assertNotNull(narrowed.getSuperTypes());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(SimpleType.constructUnsafe(String.class).isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = SimpleType.constructUnsafe(List.class);
        assertEquals("java.util.List", javaType.getCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = SimpleType.constructUnsafe(List.class);
        assertEquals("java.util.List<E>", javaType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = SimpleType.constructUnsafe(List.class);
        assertEquals("java.util.List<E>", javaType.getGenericSignature().toString());
    }
}