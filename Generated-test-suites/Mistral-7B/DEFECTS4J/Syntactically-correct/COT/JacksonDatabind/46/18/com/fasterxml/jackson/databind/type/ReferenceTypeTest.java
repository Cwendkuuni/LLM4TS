package com.example.junit; // replace with your testing package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, typeHandler, false);

        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object invalidTypeHandler = new String();

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, invalidTypeHandler, false);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object newTypeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType.getReferencedType(), newReferenceType.getReferencedType());
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertNotSame(valueHandler, newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object newTypeHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newTypeHandler);

        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType.getReferencedType(), newReferenceType.getReferencedType());
        assertEquals(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertNotSame(valueHandler, newReferenceType.getValueHandler());
        assertEquals(newTypeHandler, newReferenceType.<Object>getTypeHandler());
        assertFalse(newReferenceType.isStaticTyping());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object oldValueHandler = new Object();
        Object newValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, oldValueHandler, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType.getReferencedType(), newReferenceType.getReferencedType());
        assertNotSame(oldValueHandler, newReferenceType.getValueHandler());
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
        assertFalse(newReferenceType.isStaticTyping());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object oldContentValueHandler = new Object();
        Object newContentValueHandler = new Object();

        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);

        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType.getReferencedType(), newReferenceType.getReferencedType());
        assertEquals(referenceType.getValueHandler(), newReferenceType.getValueHandler());
        assertNotSame(oldContentValueHandler, newReferenceType.<Object>getValueHandler());
        assertEquals(newContentValueHandler, newReferenceType.<Object>getValueHandler());
        assertFalse(newReferenceType.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertTrue(newReferenceType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String canonicalName = referenceType.buildCanonicalName();

        String expectedCanonicalName = "[reference type, class java.lang.String<T>]";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType intArrayType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, intArrayType, null, null, false);

        JavaType narrowedType = referenceType.narrow(Object.class);

        assertEquals(Object.class, narrowedType.getRawClass());
        assertInstanceOf(ReferenceType.class, narrowedType);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        JavaType intArrayType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(stringClass, intArrayType, null, null, false);

        JavaType narrowedType = referenceType.narrow(Boolean.class);
    }

    @Test
    public void testContainedTypeCount() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        int containedTypeCount = referenceType.containedTypeCount();

        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testContainedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        JavaType containedType = referenceType.containedType(0);

        assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String containedTypeName = referenceType.containedTypeName(0);

        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        Class<?> getParameterSource = referenceType.getParameterSource();

        assertEquals(cls, getParameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        StringBuilder erasedSignature = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(erasedSignature);

        String expectedErasedSignature = "[Ljava.lang.String;";
        assertEquals(expectedErasedSignature, result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        StringBuilder genericSignature = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(genericSignature);

        String expectedGenericSignature = "[Ljava.lang.String;<T extends java.lang.Object>]";
        assertEquals(expectedGenericSignature, result.toString());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String toString = referenceType.toString();

        String expectedToString = "[reference type, class java.lang.String<T>, T extends java.lang.Object>]";
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int[].class);
        JavaType refType2 = JavaType.construct(long[].class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls1, refType2, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType2));
    }
}