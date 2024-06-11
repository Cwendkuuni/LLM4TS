package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(Integer.class, referenceType.getClass());
        assertEquals(refType.getTypeId(), referenceType.getHashCode());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testNarrow() {
        JavaType refType = new SimpleType(String.class);
        JavaType narrowType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        JavaType narrowResult = referenceType.narrow(narrowType.getClass());
        assertNotNull(narrowResult);
        assertEquals(narrowType, narrowResult);
        assertEquals(Integer.class, narrowResult.getClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertNotSame(referenceType._typeHandler, newReferenceType._typeHandler);
        assertEquals(handler, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new SimpleType(String.class);
        Object contentHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        JavaType newReferencedType = referenceType.withContentTypeHandler(contentHandler);
        assertEquals(referenceType.getReferencedType(), newReferencedType);
        assertNotSame(referenceType._referencedType.<Object>getTypeHandler(), newReferencedType.<Object>getTypeHandler());
        assertEquals(contentHandler, newReferencedType.<Object>getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new SimpleType(String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertNotSame(referenceType._valueHandler, newReferenceType._valueHandler);
        assertEquals(valueHandler, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new SimpleType(String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        JavaType newReferencedType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(referenceType.getReferencedType(), newReferencedType);
        assertNotSame(referenceType._referencedType.<Object>getValueHandler(), newReferencedType.<Object>getValueHandler());
        assertEquals(contentValueHandler, newReferencedType.<Object>getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(false, newReferenceType._asStatic);
        assertEquals(Integer.class, newReferenceType.getClass());
        assertEquals(Integer.class, newReferenceType.getReferencedType().getClass());

        ReferenceType staticReferenceType = new ReferenceType(Integer.class, refType, null, null, true);
        assertEquals(true, staticReferenceType._asStatic);
        assertEquals(Integer.class, staticReferenceType.getClass());
        assertEquals(Integer.class, staticReferenceType.getReferencedType().getClass());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals("class com.fasterxml.jackson.core.type.ReferenceType<java.lang.String>", referenceType.buildCanonicalName());
    }

    @Test
    public void testGetHashCode() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(refType.getTypeId(), referenceType.getHashCode());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(String.class);
        JavaType refType2 = new SimpleType(String.class);
        ReferenceType referenceType1 = new ReferenceType(Integer.class, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, refType2, null, null, false);
        JavaType refType3 = new SimpleType(String.class);
        ReferenceType referenceType3 = new ReferenceType(Integer.class, refType3, null, null, false);
        JavaType refType4 = new SimpleType(String.class);
        ReferenceType referenceType4 = new ReferenceType(Integer.class, refType4, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));

        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
        assertFalse(referenceType1.equals(""));

        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));

        assertFalse(referenceType1.equals(refType1));
        assertFalse(referenceType1.equals(refType2));
        assertFalse(referenceType1.equals(refType3));
        assertFalse(referenceType1.equals(refType4));

        ReferenceType referenceType5 = new ReferenceType(Integer.class, refType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType5));
        assertTrue(referenceType5.equals(referenceType1));
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(referenceType.getReferencedType(), referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals(Integer.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder erasedSignature = new StringBuilder();
        referenceType.getErasedSignature(erasedSignature);
        assertEquals("Integer", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        StringBuilder genericSignature = new StringBuilder();
        referenceType.getGenericSignature(genericSignature);
        assertEquals("[reference type, class java.lang.Integer<java.lang.String>]", genericSignature.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, refType, null, null, false);
        assertEquals("[reference type, class java.lang.Integer<java.lang.String>]", referenceType.toString());
    }
}