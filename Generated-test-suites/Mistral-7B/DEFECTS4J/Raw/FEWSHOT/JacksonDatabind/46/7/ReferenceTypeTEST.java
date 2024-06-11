package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = ReferenceType.construct(intType, javaIntType, null);
        assertEquals(intType, referenceType.getRawClass());
        assertEquals(javaIntType, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertNull(referenceType.getContentValueHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testGetReferencedType() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        assertEquals(javaIntType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetContentTypeHandler() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType).withTypeHandler(new Object() {});
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        assertNull(referenceType.getContentTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType).withTypeHandler(new Object() {});
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(new Object() {});
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferenceType.getContentTypeHandler(), new Object());
    }

    @Test
    public void testNarrow() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        JavaType javaLongType = new SimpleType(Long.TYPE);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        ReferenceType narrowedType = referenceType.narrow(Long.TYPE);
        assertEquals(Long.TYPE, narrowedType.getRawClass());
        assertEquals(javaIntType, narrowedType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaIntType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        assertEquals(intType, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        StringBuilder erasedSignature = new StringBuilder();
        referenceType.getErasedSignature(erasedSignature);
        assertEquals("[int]", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        StringBuilder genericSignature = new StringBuilder();
        referenceType.getGenericSignature(genericSignature);
        assertEquals("[reference type, class java.lang.Integer<T>]", genericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType = new SimpleType(intType);
        ReferenceType referenceType = new ReferenceType(intType, javaIntType, null, null, false);
        assertEquals("[reference type, class java.lang.Integer]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<Integer> intType = Integer.TYPE;
        JavaType javaIntType1 = new SimpleType(intType);
        JavaType javaIntType2 = new SimpleType(intType);
        ReferenceType referenceType1 = new ReferenceType(intType, javaIntType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(intType, javaIntType2, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
        assertTrue(referenceType1.equals(new ReferenceType(intType, javaIntType1, null, null, false)));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(null));
    }
}