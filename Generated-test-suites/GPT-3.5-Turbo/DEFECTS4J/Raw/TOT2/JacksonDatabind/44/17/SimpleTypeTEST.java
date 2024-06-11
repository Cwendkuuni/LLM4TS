package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Long.class);
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Double.class);
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, newType);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Float.class);
        JavaType refinedType = simpleType.refine(Long.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Short.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertTrue(sb.toString().contains("java.lang.Short"));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Byte.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertTrue(sb.toString().contains("java/lang/Byte"));
    }

}