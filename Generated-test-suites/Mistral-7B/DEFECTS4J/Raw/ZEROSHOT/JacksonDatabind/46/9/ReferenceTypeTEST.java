package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

/**
 * Test class for {@link ReferenceType}.
 */
public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
        assertEquals(ReferenceType.class, referenceType.getClass());
        assertEquals(refTypeClass, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testSetTypeHandler() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(refTypeClass, refType, null, null);
        Object typeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
        assertNull(newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testSetContentTypeHandler() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        JavaType refTypeWithHandler = JavaType.construct(String.class, Object.class);
        ReferenceType referenceType = ReferenceType.construct(refTypeClass, refType, null, null);
        Object contentTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getContentType());
        assertSame(refTypeWithHandler, newReferenceType.getContentType().getTypeHandler());
        assertEquals(contentTypeHandler, newReferenceType.getContentTypeHandler());
        assertNull(newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testSetValueHandler() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(refTypeClass, refType, null, null);
        Object valueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getValueHandler().getType());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
        assertNull(newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testSetContentValueHandler() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        JavaType refTypeWithHandler = JavaType.construct(String.class, Object.class);
        ReferenceType referenceType = ReferenceType.construct(refTypeClass, refType, null, null);
        Object contentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getContentType());
        assertSame(refTypeWithHandler, newReferenceType.getContentType().getValueHandler());
        assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
        assertNull(newReferenceType.getValueHandler());
        assertNull(newReferenceType.getTypeHandler());
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testSetStaticTyping() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(refTypeClass, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertSame(refType, newReferenceType.getContentType());
        assertNull(newReferenceType.getValueHandler());
        assertNull(newReferenceType.getTypeHandler());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testCanonicalName() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName.substring(0, canonicalName.lastIndexOf('[')));
        assertEquals("Ljava/lang/ref/ReferenceType;", canonicalName.substring(canonicalName.lastIndexOf('[')));
    }

    @Test
    public void testNarrow() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        JavaType stringListType = JavaType.construct(java.util.List.class, String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        JavaType narrowedType = referenceType._narrow(String[].class);
        assertEquals(String[].class, narrowedType.getRawClass());
        assertEquals(refType, narrowedType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        assertEquals(refTypeClass, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("Ljava/lang/ReferenceType<Ljava/lang/String;>;", sb.toString());
    }

    @Test
    public void testToString() {
        Class<String> refTypeClass = String.class;
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(refTypeClass, refType, null, null, false);
        String toString = referenceType.toString();
        String expected = "[reference type, class java.lang.String<T>]";
        assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        Class<String> refTypeClass1 = String.class;
        JavaType refType1 = JavaType.construct(String.class);
        ReferenceType referenceType1 = new ReferenceType(refTypeClass1, refType1, null, null, false);

        Class<String> refTypeClass2 = String.class;
        JavaType refType2 = JavaType.construct(String.class);
        ReferenceType referenceType2 = new ReferenceType(refTypeClass2, refType2, null, null, false);

        Class<String> refTypeClass3 = String[].class;
        JavaType refType3 = JavaType.construct(String[].class);
        ReferenceType referenceType3 = new ReferenceType(refTypeClass3, refType3, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1.withTypeHandler(null)));
        assertTrue(referenceType1.equals(referenceType1.withValueHandler(null)));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
    }
}