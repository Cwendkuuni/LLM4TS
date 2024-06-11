
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
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
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(Object.class);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertTrue(simpleType.isStaticTyping());
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStaticTyping());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.getErasedSignature(sb);
        String expectedSignature = "Ljava/lang/String;";
        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.getGenericSignature(sb);
        String expectedSignature = "Ljava/lang/String;";
        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}

