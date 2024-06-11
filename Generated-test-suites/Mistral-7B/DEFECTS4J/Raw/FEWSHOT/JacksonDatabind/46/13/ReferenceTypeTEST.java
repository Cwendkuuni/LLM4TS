package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = ReferenceType.construct(String.class, referencedType, null);
        assertNotNull(referenceType.getReferencedType());
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotNull(newReferenceType.getTypeHandler());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertEquals(referenceType, newReferenceType); // Should be same instance since no new objects created
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newReferenceType.getReferencedType().getTypeHandler());
        assertEquals(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
        assertEquals(referenceType, newReferenceType); // Should be same instance since no new objects created
    }

    @Test
    public void testWithValueHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotNull(newReferenceType.getValueHandler());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertEquals(referenceType, newReferenceType); // Should be same instance since no new objects created
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotNull(newReferenceType.getReferencedType().getValueHandler());
        assertEquals(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
        assertEquals(referenceType, newReferenceType); // Should be same instance since no new objects created
    }

    @Test
    public void testWithStaticTyping() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isAsStatic());
        assertFalse(referenceType.isAsStatic());
        assertEquals(referenceType, newReferenceType); // Should be same instance since no new objects created
    }

    @Test
    public void testNarrow() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Class<?> subclass = Integer[].class;
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        JavaType narrowedType = referenceType._narrow(subclass);
        assertNotNull(narrowedType);
        assertEquals(new ReferenceType(subclass, referencedType, null), narrowedType);
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertEquals(referencedType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null);
        assertEquals("[reference type, class java.lang.String<T:Ljava/lang/Object;>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType referencedType1 = new SimpleType(Integer.TYPE);
        JavaType referencedType2 = new SimpleType(Integer.TYPE);
        ReferenceType referenceType1 = new ReferenceType(String.class, referencedType1, null);
        ReferenceType referenceType2 = new ReferenceType(String.class, referencedType2, null);
        JavaType referencedType3 = new SimpleType(String.class);
        ReferenceType referenceType3 = new ReferenceType(String.class, referencedType1, null);
        JavaType referenceType4 = new ReferenceType(Object.class, referencedType1, null);
        ReferenceType referenceType5 = new ReferenceType(String.class, referencedType1, null);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType5));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referencedType1));
        assertFalse(referenceType1.equals(referencedType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));
    }
}