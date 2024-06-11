package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(new Object() {});
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(refType, newReferenceType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaType(String.class);
        JavaType newContentType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaType(String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaType(String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
        assertTrue(newReferenceType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        referenceType.narrow(Double.class);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(referenceType.getContentType(), referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(Integer.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("I");
        assertEquals(expected.toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/lang/Integer;<T:Ljava/lang/String;>");
        assertEquals(expected.toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[reference type, class [Ljava.lang.Integer;<T:Ljava.lang.String;>]");
        assertEquals(expected.toString(), referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new JavaType(String.class);
        JavaType refType2 = new JavaType(String.class);
        ReferenceType referenceType1 = new ReferenceType(Integer.class, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, refType2, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(Integer.class, refType1, null, null, true);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2)); // Equals due to equal referenced types
        assertFalse(referenceType1.equals(referenceType3)); // Differences in static typing
    }
}