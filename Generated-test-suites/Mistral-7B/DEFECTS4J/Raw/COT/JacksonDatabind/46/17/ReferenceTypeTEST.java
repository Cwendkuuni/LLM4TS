package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType newContentType = JavaType.construct(long.class);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<Object> subclass = Object.class;
        ReferenceType narrowedType = referenceType._narrow(subclass);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testNarrowWithValidSubclass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<StringBuilder> subclass = StringBuffer.class;
        ReferenceType narrowedType = referenceType._narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(refType, narrowedType.getContentType());
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int[].class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);

        Class<String> cls2 = String.class;
        JavaType refType2 = JavaType.construct(long[].class);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);

        ReferenceType referenceType3 = referenceType1;

        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType3);
        assertEquals(referenceType1.hashCode(), referenceType1.hashCode());
        assertEquals(referenceType1, referenceType1);

        assertNotEquals(referenceType1, referenceType2);
        assertNotEquals(referenceType1, null);
        assertNotEquals(referenceType1, new Object());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expected = "[reference type, class com.example.junit.String, class [I]";
        String result = referenceType.toString();
        assertEquals(expected, result);
    }
}