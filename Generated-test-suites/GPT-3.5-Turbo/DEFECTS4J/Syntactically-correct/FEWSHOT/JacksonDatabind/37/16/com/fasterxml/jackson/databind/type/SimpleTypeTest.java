
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType type = new SimpleType(String.class);
        SimpleType narrowType = type._narrow(Object.class);
        assertEquals(Object.class, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(java.util.Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(java.util.Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(java.util.ArrayList[].class);
    }

    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(String.class);
        assertNull(type.refine(String.class, new TypeBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType nonContainerType = new SimpleType(String.class);
        assertFalse(nonContainerType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        TypeBindings bindings = new TypeBindings();
        SimpleType type = new SimpleType(String.class, bindings, null, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));
    }
}
