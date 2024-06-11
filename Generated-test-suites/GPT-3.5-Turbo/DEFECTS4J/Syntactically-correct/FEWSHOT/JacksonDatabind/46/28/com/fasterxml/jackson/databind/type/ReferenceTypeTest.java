package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertSame(cls, referenceType._class);
        assertSame(refType, referenceType._referencedType);
        assertNull(referenceType._valueHandler);
        assertNull(referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandlerOrig = null;
        Object typeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandlerOrig, false);

        ReferenceType result = referenceType.withTypeHandler(typeHandler);

        assertSame(cls, result._class);
        assertSame(refType, result._referencedType);
        assertSame(valueHandler, result._valueHandler);
        assertSame(typeHandler, result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refTypeOrig = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;
        Object typeHandlerContentOrig = null;
        Object typeHandlerContent = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refTypeOrig, valueHandler, typeHandler, false);

        ReferenceType result = referenceType.withContentTypeHandler(typeHandlerContent);

        assertSame(cls, result._class);
        assertNotSame(refTypeOrig, result._referencedType);
        assertSame(valueHandler, result._valueHandler);
        assertSame(typeHandler, result._typeHandler);
        assertSame(typeHandlerContent, result._referencedType.getTypeHandler());
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandlerOrig = null;
        Object valueHandler = new Object();
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandlerOrig, typeHandler, false);

        ReferenceType result = referenceType.withValueHandler(valueHandler);

        assertSame(cls, result._class);
        assertSame(refType, result._referencedType);
        assertSame(valueHandler, result._valueHandler);
        assertSame(typeHandler, result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = Object.class;
        JavaType refTypeOrig = new SimpleType(cls, 0, null, null, false);
        Object valueHandlerOrig = null;
        Object typeHandler = null;
        Object valueHandlerContentOrig = null;
        Object valueHandlerContent = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refTypeOrig, valueHandlerOrig, typeHandler, false);

        ReferenceType result = referenceType.withContentValueHandler(valueHandlerContent);

        assertSame(cls, result._class);
        assertNotSame(refTypeOrig, result._referencedType);
        assertSame(valueHandlerOrig, result._valueHandler);
        assertSame(typeHandler, result._typeHandler);
        assertSame(valueHandlerContent, result._referencedType.getValueHandler());
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Object.class;
        JavaType refTypeOrig = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refTypeOrig, valueHandler, typeHandler, false);

        ReferenceType result = referenceType.withStaticTyping();

        assertSame(cls, result._class);
        assertNotSame(refTypeOrig, result._referencedType);
        assertSame(valueHandler, result._valueHandler);
        assertSame(typeHandler, result._typeHandler);
        assertTrue(result._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String canonicalName = cls.getName() + "<" + refType.toCanonical() + ">";

        assertEquals(canonicalName, referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<?> clsOrig = Object.class;
        JavaType refTypeOrig = new SimpleType(clsOrig, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;
        Class<?> cls = String.class;

        ReferenceType referenceType = new ReferenceType(clsOrig, refTypeOrig, valueHandler, typeHandler, false);

        JavaType result = referenceType._narrow(cls);

        assertNotSame(referenceType, result);
        assertSame(cls, result._class);
        assertSame(refTypeOrig, result._referencedType);
        assertSame(valueHandler, result._valueHandler);
        assertSame(typeHandler, result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertSame(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertSame(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertSame(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sbOrig = new StringBuilder();
        StringBuilder sb = referenceType.getErasedSignature(sbOrig);

        assertEquals(sbOrig, sb);
        assertEquals(cls.toString(), sbOrig.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sbOrig = new StringBuilder();
        referenceType.getGenericSignature(sbOrig);
        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(sbOrig, sb);
    }

    @Test
    public void testToString() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType(cls, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String expected = "[reference type, class " + referenceType.buildCanonicalName() + "]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> clsOrig = Object.class;
        JavaType refTypeOrig = new SimpleType(clsOrig, 0, null, null, false);
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType referenceType = new ReferenceType(clsOrig, refTypeOrig, valueHandler, typeHandler, false);
        ReferenceType sameReferenceType = new ReferenceType(clsOrig, refTypeOrig, valueHandler, typeHandler, false);
        ReferenceType differentReferenceType = new ReferenceType(clsOrig, refTypeOrig, valueHandler, typeHandler, true);

        assertTrue(referenceType.equals(referenceType));
        assertTrue(referenceType.equals(sameReferenceType));
        assertFalse(referenceType.equals(differentReferenceType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(new Object()));
    }
}