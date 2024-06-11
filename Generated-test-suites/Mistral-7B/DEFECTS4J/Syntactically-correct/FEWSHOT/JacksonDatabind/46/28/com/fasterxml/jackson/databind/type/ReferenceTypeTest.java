package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = ReferenceType.construct(String.class, referencedType, null);
        assertNotNull(refType);
        assertEquals(String.class, refType.getRawClass());
        assertEquals(referencedType.getClass(), refType._referencedType.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object typeHandler = new Object();
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType refTypeWithHandler = refType.withTypeHandler(typeHandler);
        assertEquals(refType.getClass(), refTypeWithHandler.getClass());
        assertNotNull(refTypeWithHandler._typeHandler);
        assertNull(refType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object contentTypeHandler = new Object();
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType refTypeWithContentTypeHandler = refType.withContentTypeHandler(contentTypeHandler);
        assertEquals(refType.getClass(), refTypeWithContentTypeHandler.getClass());
        assertNotNull(refTypeWithContentTypeHandler._referencedType._typeHandler);
        assertNull(refType._referencedType._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object valueHandler = new Object();
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType refTypeWithValueHandler = refType.withValueHandler(valueHandler);
        assertEquals(refType.getClass(), refTypeWithValueHandler.getClass());
        assertNotNull(refTypeWithValueHandler._valueHandler);
        assertNull(refType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        Object contentValueHandler = new Object();
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType refTypeWithContentValueHandler = refType.withContentValueHandler(contentValueHandler);
        assertEquals(refType.getClass(), refTypeWithContentValueHandler.getClass());
        assertNotNull(refTypeWithContentValueHandler._referencedType._valueHandler);
        assertNull(refType._referencedType._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType refTypeWithStaticTyping = refType.withStaticTyping();
        assertEquals(refType.getClass(), refTypeWithStaticTyping.getClass());
        assertTrue(refTypeWithStaticTyping._asStatic);
        assertFalse(refType._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        assertEquals(referencedType, refType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        assertEquals(1, refType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        JavaType expected = referencedType;
        JavaType actual = refType.containedType(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        String expected = "T";
        String actual = refType.containedTypeName(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterSource() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        assertEquals(String.class, refType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = refType.getErasedSignature(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = refType.getGenericSignature(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType = new ReferenceType(String.class, referencedType, null, null, false);
        String expected = "[reference type, class " + String.class.getName() + "<T>" +
                "[" + referencedType.toString() + "]>";
        String actual = refType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType referencedType = new SimpleType(Integer.TYPE);
        ReferenceType refType1 = new ReferenceType(String.class, referencedType, null, null, false);
        ReferenceType refType2 = new ReferenceType(String.class, referencedType, null, null, false);
        Object referenceObject = new Object();
        JavaType referencedType2 = new SimpleType(Long.TYPE);
        ReferenceType refType3 = new ReferenceType(String.class, referencedType2, null, null, false);
        assertEquals(refType1, refType1);
        assertEquals(refType1, refType2);
        assertNotEquals(refType1, referenceObject);
        assertNotEquals(refType1, refType3);
    }
}