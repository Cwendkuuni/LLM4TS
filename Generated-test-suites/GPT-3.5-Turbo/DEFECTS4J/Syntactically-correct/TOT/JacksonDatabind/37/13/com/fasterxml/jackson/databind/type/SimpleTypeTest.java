
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
    public void testConstruct_withArray_shouldThrowIllegalArgumentException() {
        try {
            SimpleType simpleType = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withMap_shouldThrowIllegalArgumentException() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
    }

    @Test
    public void testConstruct_withCollection_shouldThrowIllegalArgumentException() {
        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
    }

    @Test
    public void testRefine_shouldReturnNull() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }

    @Test
    public void testIsContainerType_shouldReturnFalse() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature_withClass_shouldReturnErasedSignatureString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        sb = simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature_withClass_shouldReturnGenericSignatureString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        sb = simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testWithContentType_shouldThrowIllegalArgumentException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler_shouldThrowIllegalArgumentException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertTrue(modifiedType.isStatic());
    }

    @Test
    public void testWithStaticTyping_sameAsCurrent_returnSameInstance() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType modifiedType = simpleType.withStaticTyping();
        assertSame(simpleType, modifiedType);
    }

    @Test
    public void testToString_shouldReturnStringRepresentation() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals_withSameObject_shouldReturnTrue() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_withEqualObjects_shouldReturnTrue() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEquals_withDifferentClass_shouldReturnFalse() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Integer.class);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEquals_withNullObject_shouldReturnFalse() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType.equals(null));
    }

}

