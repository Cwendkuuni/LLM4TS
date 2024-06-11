package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }
    }

    @Test
    public void test_narrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowedType = simpleType._narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test
    public void test_withTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withTypeHandlerType = simpleType.withTypeHandler("typeHandler");
        assertNotNull(withTypeHandlerType);
        assertEquals(String.class, withTypeHandlerType.getRawClass());
        assertEquals("typeHandler", withTypeHandlerType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_withContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentType(new SimpleType(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_withContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentTypeHandler("contentTypeHandler");
    }

    @Test
    public void test_withValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withValueHandlerType = simpleType.withValueHandler("valueHandler");
        assertNotNull(withValueHandlerType);
        assertEquals(String.class, withValueHandlerType.getRawClass());
        assertEquals("valueHandler", withValueHandlerType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_withContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        simpleType.withContentValueHandler("contentValueHandler");
    }

    @Test
    public void test_withStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType withStaticTypingType = simpleType.withStaticTyping();
        assertNotNull(withStaticTypingType);
        assertEquals(String.class, withStaticTypingType.getRawClass());
        assertTrue(withStaticTypingType.isStatic());
    }

    @Test
    public void test_refine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        String toString = simpleType.toString();
        assertNotNull(toString);
        assertEquals("[simple type, class java.lang.String]", toString);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}