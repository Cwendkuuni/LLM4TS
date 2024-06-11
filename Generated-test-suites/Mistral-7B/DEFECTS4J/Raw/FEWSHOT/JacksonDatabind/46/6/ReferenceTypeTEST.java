package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType.toString(), referenceType.getContentType().toString());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertEquals(handler, newReferenceType.getTypeHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType newContentType = JavaType.constructCollectionType(ArrayList.class, String.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        ReferenceType newContentTypeReferenceType = newReferenceType.withContentTypeHandler(newContentType);
        assertEquals(newContentType, newContentTypeReferenceType.getContentType());
        assertEquals(referenceType.getRawClass(), newContentTypeReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newContentTypeReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertEquals(handler, newReferenceType.getValueHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType newContentType = JavaType.constructCollectionType(ArrayList.class, String.class);
        Object newContentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
        ReferenceType newContentValueHandlerReferenceType = newReferenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newContentValueHandlerReferenceType.getContentValueHandler());
        assertEquals(referenceType.getRawClass(), newContentValueHandlerReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newContentValueHandlerReferenceType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType expected = refType;
        JavaType actual = referenceType.containedType(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String expected = "T";
        String actual = referenceType.containedTypeName(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = referenceType.getErasedSignature(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = referenceType.getGenericSignature(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String expected = "[reference type, class " + referenceType.getRawClass().getName() + " <" + refType.toString() + ">]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType refType2 = JavaType.constructCollectionType(List.class, String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(String.class, refType, null, null, true);
        assertTrue(referenceType.equals(referenceType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(referenceType2));
        assertFalse(referenceType.equals(referenceType3));
    }
}