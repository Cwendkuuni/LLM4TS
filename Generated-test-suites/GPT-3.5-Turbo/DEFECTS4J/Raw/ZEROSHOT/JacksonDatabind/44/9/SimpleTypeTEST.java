package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Boolean.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Float.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Integer.class);
        JavaType narrowedType = simpleType.narrow(Number.class);
        assertEquals(Number.class, narrowedType.getRawClass());
        assertNotEquals(simpleType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(String.class);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType staticTypedType = simpleType.withStaticTyping();
        assertTrue(staticTypedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Double.class);
        JavaType refinedType = simpleType.refine(Number.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Long.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Byte.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        simpleType.getErasedSignature(sb);
        expected.append("Ljava/lang/Byte;");
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Boolean.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        simpleType.getGenericSignature(sb);
        expected.append("Ljava/lang/Boolean;");
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Character.class);
        String expected = "[simple type, class java.lang.Character]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}