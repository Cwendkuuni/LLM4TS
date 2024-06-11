package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType staticType = (SimpleType) simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertEquals("java.lang.Object", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        String str = simpleType.toString();
        assertNotNull(str);
        assertTrue(str.contains("[simple type, class java.lang.Object]"));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}