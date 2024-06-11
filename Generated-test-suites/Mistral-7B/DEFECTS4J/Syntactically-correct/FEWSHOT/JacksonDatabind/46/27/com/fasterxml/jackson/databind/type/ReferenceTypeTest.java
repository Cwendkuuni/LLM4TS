package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(String[].class, referenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertNotSame(referenceType._typeHandler, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaTypeRef = JavaType.construct(String.class);
        JavaType javaType = JavaType.construct(String[].class);
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaTypeRef, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(javaTypeRef, newReferenceType.getReferencedType());
        assertNotSame(referenceType._referencedType.<Object>getTypeHandler(), newReferenceType._referencedType.<Object>getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotSame(referenceType._valueHandler, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaTypeRef = JavaType.construct(String.class);
        JavaType javaType = JavaType.construct(String[].class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaTypeRef, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotSame(referenceType._referencedType.<Object>getValueHandler(), newReferenceType._referencedType.<Object>getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType._asStatic);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals("[reference type, class java.lang.String<T>]", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaTypeString = JavaType.construct(String.class);
        JavaType javaTypeArrayString = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(String.class, javaTypeString, null, null, false);
        ReferenceType narrowedType = referenceType._narrow(String[].class);
        assertEquals(javaTypeArrayString, narrowedType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(javaType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(javaType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaTypeRef = JavaType.construct(String.class);
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaTypeRef, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;<T>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaTypeRef = JavaType.construct(String.class);
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaTypeRef, null, null, false);
        assertEquals("[reference type, class java.lang.String<T>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        JavaType javaTypeRef1 = JavaType.construct(String.class);
        JavaType javaTypeRef2 = JavaType.construct(String.class);
        JavaType javaType = JavaType.construct(String[].class);
        ReferenceType referenceType1 = new ReferenceType(stringClass, javaTypeRef1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass, javaTypeRef2, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(stringClass, javaTypeRef1, null, null, true);
        ReferenceType referenceType4 = new ReferenceType(Object.class, javaTypeRef1, null, null, false);
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType4));
    }
}