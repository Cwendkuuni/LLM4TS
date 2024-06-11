package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.Class;
import java.lang.reflect.Type;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(cls, referenceType.getType());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(Integer.class);
        JavaType narrowType = ReferenceType.construct(cls, refType, null, null).narrow(String[].class);
        assertEquals(refType, narrowType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        Object handler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        JavaType refType2 = ClassType.construct(long.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType2, null, null);
        assertFalse(referenceType.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls, refType, null, null);
        assertTrue(referenceType.equals(referenceType3));
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        StringBuilder erasedSignature = new StringBuilder();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        referenceType.getErasedSignature(erasedSignature);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        StringBuilder genericSignature = new StringBuilder();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        referenceType.getGenericSignature(genericSignature);
        assertEquals("Lcom/fasterxml/jackson/databind/type/ReferenceType<Ljava/lang/String;Ljava/lang/Integer;>;", genericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = ClassType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals("[reference type, class java.lang.String, class java.lang.Integer]", referenceType.toString());
    }
}