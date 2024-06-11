package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getType());
        assertEquals(int.class, referenceType.getContentType().getType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType newTypeHandler = new SimpleType(long.class);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(newTypeHandler, newReferenceType.getContentTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerIllegalArgumentException() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType newTypeHandler = null;
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType newContentTypeHandler = new SimpleType(long.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(referenceType.getContentTypeHandler(), newReferenceType.getContentTypeHandler());
        assertEquals(newContentTypeHandler, newReferenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerIllegalArgumentException() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType newContentTypeHandler = null;
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerIllegalArgumentException() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        Object newValueHandler = null;
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        Object newContentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(referenceType.getContentValueHandler(), newReferenceType.getContentValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerIllegalArgumentException() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        Object newContentValueHandler = null;
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(true, newReferenceType.isStatic());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(refType, referenceType.containedType(0));
        assertEquals(null, referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
        assertEquals(null, referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = referenceType.getErasedSignature(expected);
        assertEquals("Ljava/lang/String;", actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = referenceType.getGenericSignature(expected);
        assertEquals("Ljava/lang/String;<T:Ljava/lang/Object;>", actual.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String expected = "[reference type, class [Ljava/lang/String;<T:Ljava/lang/Object;>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new SimpleType(int.class);
        JavaType refType2 = new SimpleType(int.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null, null, false);
        JavaType referenceType3 = new ReferenceType(String.class, refType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(referenceType2));
    }

    // Add more test cases for specific edge cases and exception handling scenarios here

}