package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, refType, null, null);
        assertNotNull(referenceType);
        assertSame(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        JavaType refType = new SimpleType(String.class);
        Object typeHandler = new Object();
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false)
                .withTypeHandler(typeHandler);
        assertSame(typeHandler, referenceType.getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        JavaType refType = new SimpleType(String.class);
        Object contentTypeHandler = new Object();
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false)
                .withContentTypeHandler(contentTypeHandler);
        assertSame(contentTypeHandler, referenceType.getContentTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        JavaType referenceType = new ReferenceType(Integer.class, refType, valueHandler, null, false);
        assertSame(valueHandler, referenceType.getValueHandler());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType refType = new SimpleType(String.class);
        Object contentValueHandler = new Object();
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false)
                .withContentValueHandler(contentValueHandler);
        assertSame(contentValueHandler, referenceType.getContentValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false)
                .withStaticTyping();
        assertTrue(referenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals("class com.fasterxml.jackson.core.type.ReferenceType<T>[T=java.lang.Integer]", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        JavaType narrowedType = referenceType.narrow(Long.class);
        assertEquals(Long.class, narrowedType.getRawClass());
    }

    @Test
    public void testReferencedType() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(Integer.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("class com.fasterxml.jackson.core.type.ReferenceType<T>", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("class com.fasterxml.jackson.core.type.ReferenceType<T>[T=java.lang.Integer]", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(String.class);
        JavaType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals("[reference type, class com.fasterxml.jackson.core.type.ReferenceType<T>[T=java.lang.Integer]]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(String.class);
        JavaType referenceType1 = new ReferenceType(Integer.class, refType1, null, null, false);
        JavaType refType2 = new SimpleType(String.class);
        JavaType referenceType2 = new ReferenceType(Integer.class, refType2, null, null, false);
        JavaType referenceType3 = new ReferenceType(Integer.class, refType1, null, null, false);
        JavaType referenceType4 = new ReferenceType(Long.class, refType1, null, null, false);
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referenceType2));
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));
    }
}