package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertNotNull(referenceType);
        assertNotNull(referenceType.getReferencedType());
        assertEquals(int.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetContentType() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        String expectedCanonicalName = "[reference type, class java.lang.String<T> type 'class java.lang.Integer'>]";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<?> subclass = String[].class;
        JavaType narrowType = referenceType.narrow(subclass);
        assertEquals(new SimpleType(int[].class), narrowType);
    }

    @Test
    public void testGetReferencedTypeClass() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(int.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testEquals() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        JavaType refType2 = new SimpleType(int.class);
        ReferenceType referenceType1 = new ReferenceType(cls, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls, refType2, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(cls, refType, null, null, true);
        ReferenceType referenceType4 = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType4));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(null));
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expectedToString = "[reference type, class java.lang.String<T> type 'class java.lang.Integer'>]";
        assertEquals(expectedToString, referenceType.toString());
    }
}