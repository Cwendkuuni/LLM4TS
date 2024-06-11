package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertEquals(cls, referenceType.getParameterSource());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertEquals(referenceType, result);
        assertEquals(h, referenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertEquals(referenceType, result);
        assertEquals(h, referenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        assertEquals(referenceType, result);
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        assertEquals(null, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        assertEquals(null, referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testToString() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        String expected = "[reference type, class " + referenceType.buildCanonicalName() + "<" + refType + ">]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertEquals(referenceType1, referenceType2);
    }
}