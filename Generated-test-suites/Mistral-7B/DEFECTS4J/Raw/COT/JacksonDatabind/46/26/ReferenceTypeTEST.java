package com.example.junit; // replace with your test package if needed

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = JavaType.construct(int.class, String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
        assertNotNull(referenceType);
        assertTrue(referenceType instanceof ReferenceType);
        assertTrue(referenceType.getReferencedType().equals(refType));
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.construct(int.class, String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
        assertTrue(referenceType.getReferencedType().equals(refType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerNull() {
        JavaType refType = JavaType.construct(int.class, String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        referenceType.withTypeHandler(null);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = JavaType.construct(int.class, String.class);
        JavaType newContentTypeHandler = JavaType.construct(String.class, Integer.TYPE);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertNotNull(newReferenceType);
        assertFalse(newReferenceType.equals(referenceType));
        assertTrue(newReferenceType.getReferencedType().equals(refType));
        assertTrue(newReferenceType.getContentTypeHandler().equals(newContentTypeHandler));
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = JavaType.construct(int.class, String.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotNull(newReferenceType);
        assertFalse(newReferenceType.equals(referenceType));
        assertTrue(newReferenceType.getValueHandler().equals(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = JavaType.construct(int.class, String.class);
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotNull(newReferenceType);
        assertFalse(newReferenceType.equals(referenceType));
        assertTrue(newReferenceType.getContentValueHandler().equals(contentValueHandler));
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = JavaType.construct(int.class, String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotNull(newReferenceType);
        assertFalse(newReferenceType.equals(referenceType));
        assertTrue(newReferenceType.isStatic());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithStaticTypingAlreadyStatic() {
        JavaType refType = JavaType.construct(int.class, String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, true);
        referenceType.withStaticTyping();
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = JavaType.construct(int.class, String.class);
        String expected = "[reference type, class java.lang.String<com.fasterxml.jackson.databind.type.TypeReference>]";
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String actual = referenceType.buildCanonicalName();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testNarrow() {
        JavaType refType = JavaType.construct(int.class, String.class);
        JavaType subclass = JavaType.construct(int[].class, String.class);
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        JavaType narrowedType = referenceType._narrow(subclass.getRawClass());
        assertTrue(narrowedType.getClass().equals(ReferenceType.class));
        assertTrue(narrowedType.getReferencedType().equals(subclass));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = JavaType.construct(int.class, String.class);
        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava.lang.String;");
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertTrue(actual.toString().equals(expected.toString()));
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = JavaType.construct(int.class, String.class);
        StringBuilder expected = new StringBuilder();
        expected.append("[reference type, class java.lang.String<");
        expected.append(refType.getRawClass().getName());
        expected.append(">]");
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertTrue(actual.toString().equals(expected.toString()));
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.construct(int.class, String.class);
        String expected = "[reference type, class java.lang.String<com.fasterxml.jackson.databind.type.TypeReference>]";
        ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
        String actual = referenceType.toString();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testEquals() {
        JavaType refType1 = JavaType.construct(int.class, String.class);
        JavaType refType2 = JavaType.construct(int.class, String.class);
        ReferenceType referenceType1 = new ReferenceType(String.class, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType2, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(String[].class, refType1, null, null, false);
        assertFalse(referenceType1.equals(referenceType3));

        ReferenceType referenceType4 = new ReferenceType(String.class, refType2, null, null, false);
        assertFalse(referenceType1.equals(referenceType4));

        ReferenceType referenceType5 = new ReferenceType(String.class, refType1, null, null, true);
        assertFalse(referenceType1.equals(referenceType5));
    }
}