package com.fasterxml.jackson.databind.type;

import java.util.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentType(SimpleType.construct(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNull(simpleType.refine(List.class, TypeBindings.emptyBindings(), null, new JavaType[] {}));
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        assertEquals(type1, type2);
        assertEquals(type1.hashCode(), type2.hashCode());
    }
}