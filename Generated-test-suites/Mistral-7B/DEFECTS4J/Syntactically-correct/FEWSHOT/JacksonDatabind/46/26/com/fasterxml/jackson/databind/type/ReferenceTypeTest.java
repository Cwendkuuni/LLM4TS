package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals(Object.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(false, referenceType.isStatic());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(contentValueHandler, newReferenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        String canonicalName = referenceType.buildCanonicalName();
        String expectedCanonicalName = "[reference type, class java.lang.Object<T> type 'class java.lang.String'>]";
        assertEquals(expectedCanonicalName.replaceAll("\\s", ""), canonicalName);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrow() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType narrowType = referenceType.narrow(Integer.class);
        // This should fail since Object is not a subclass of Integer
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        assertEquals(Object.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        StringBuilder expectedSignature = new StringBuilder("[Object]");
        StringBuilder signature = referenceType.getErasedSignature(expectedSignature);
        assertEquals(expectedSignature.toString(), signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        StringBuilder expectedSignature = new StringBuilder("[reference type, class java.lang.Object<T> type 'class java.lang.String'>]");
        StringBuilder signature = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), signature.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(Object.class, refType, null, null);
        String expectedString = "[reference type, class java.lang.Object<java.lang.String>]";
        assertEquals(expectedString, referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType1 = ReferenceType.construct(Object.class, refType1, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(Object.class, refType2, null, null);
        JavaType referenceType3 = ReferenceType.construct(Object.class, refType1, null, null);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
    }
}