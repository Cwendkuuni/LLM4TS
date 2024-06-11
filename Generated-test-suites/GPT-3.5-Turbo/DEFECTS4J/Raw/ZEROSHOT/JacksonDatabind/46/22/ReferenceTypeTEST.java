package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = null;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertEquals(false, referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(h, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType.withTypeHandler(h), result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType result = referenceType.withValueHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(h, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType.withValueHandler(h), result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        assertNotNull(result);
        assertEquals(referenceType._class, result._class);
        assertEquals(referenceType._referencedType.withStaticTyping(), result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertEquals("java.lang.String<null>", canonicalName);
    }

    @Test
    public void testNarrow() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        Class<?> subclass = Object.class;
        JavaType result = referenceType._narrow(subclass);
        assertNotNull(result);
        assertEquals(subclass, result._class);
        assertEquals(referenceType._referencedType, result._referencedType);
        assertEquals(referenceType._valueHandler, result._valueHandler);
        assertEquals(referenceType._typeHandler, result._typeHandler);
        assertEquals(referenceType._asStatic, result._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        JavaType result = referenceType.getReferencedType();
        assertNotNull(result);
        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        int count = referenceType.containedTypeCount();
        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        JavaType result = referenceType.containedType(0);
        assertNotNull(result);
        assertEquals(referenceType._referencedType, result);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        String name = referenceType.containedTypeName(0);
        assertNotNull(name);
        assertEquals("T", name);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        Class<?> result = referenceType.getParameterSource();
        assertEquals(referenceType._class, result);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals("java.lang.String", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals("java.lang.String<null;>", result.toString());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        String string = referenceType.toString();
        assertNotNull(string);
        assertEquals("[reference type, class java.lang.String<null>]", string);
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(String.class, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, null, null, null, false);
        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1.hashCode(), referenceType1.hashCode());
        assertEquals(referenceType1, referenceType2);
        assertEquals(referenceType1.hashCode(), referenceType2.hashCode());
        assertNotEquals(referenceType1, null);
        assertNotEquals(referenceType1, new Object());
    }
}