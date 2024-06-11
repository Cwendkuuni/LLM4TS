package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals(Object.class, referenceType.getRawClass());
        assertNull(referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null)
            .withStaticTyping();
        assertTrue(referenceType.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null)
            .withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, referenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null)
            .withContentValueHandler(valueHandler);
        assertEquals(valueHandler, referenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithReferencedType() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null)
            .withReferencedType(referencedType);
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals(null, referenceType.containedType(1));
        assertEquals(null, referenceType.containedType(-1));
        assertEquals(null, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals(null, referenceType.containedTypeName(1));
        assertEquals(null, referenceType.containedTypeName(-1));
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals(Object.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals("Ljava/lang/Object;", referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals("Ljava/lang/Object;T;", referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null, null, null);
        assertEquals("[reference type, class java.lang.Object<T>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = ReferenceType.construct(Object.class, null, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(Object.class, null, null, null);
        ReferenceType referenceType3 = ReferenceType.construct(String.class, null, null, null);
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }
}