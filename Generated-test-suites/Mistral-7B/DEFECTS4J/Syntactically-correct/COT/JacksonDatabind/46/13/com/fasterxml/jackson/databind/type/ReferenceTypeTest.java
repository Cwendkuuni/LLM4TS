package com.example.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(String[].class, referenceType.getRawClass());
        assertEquals(refType.hashCode(), referenceType.getContentType().hashCode());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaType(String.class);
        JavaType newRefType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(handler, newReferenceType.getReferencedType().getTypeHandler());
        assertEquals(newRefType, newReferenceType.getReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaType(String.class);
        JavaType newRefType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(handler, newReferenceType.getReferencedType().getValueHandler());
        assertEquals(newRefType, newReferenceType.getReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals("[reference type, class [Ljava.lang.String;<T>]"
                + "[reference type, class java.lang.String]", referenceType.buildCanonicalName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        referenceType.narrow(Integer.class);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(refType, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals(String[].class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.lang.String;");
        String actual = referenceType.getErasedSignature(expected).toString();
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[reference type, class [Ljava.lang.String;<T>]");
        expected.append("<T>]");
        String actual = referenceType.getGenericSignature(expected).toString();
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testToString() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String[].class, refType, null, null, false);
        assertEquals("[reference type, class [Ljava.lang.String;<java.lang.String>]"
                + "[reference type, class java.lang.String]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new JavaType(String.class);
        JavaType refType2 = new JavaType(String.class);
        ReferenceType referenceType1 = new ReferenceType(String[].class, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String[].class, refType2, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        ReferenceType referenceType3 = new ReferenceType(String[].class, refType1, null, null, true);
        assertFalse(referenceType1.equals(referenceType3));
    }
}