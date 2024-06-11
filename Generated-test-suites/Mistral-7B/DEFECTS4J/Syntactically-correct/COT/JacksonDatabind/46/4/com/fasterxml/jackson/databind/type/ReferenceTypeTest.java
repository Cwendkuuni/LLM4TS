package com.example.referenceType.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(javaType.getClass(), referenceType.getContentType().getClass());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(javaType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType newReferencedType = referenceType.withContentTypeHandler(handler);
        assertNotSame(javaType, newReferencedType);
        assertEquals(handler, newReferencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(handler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType newReferencedType = referenceType.withContentValueHandler(handler);
        assertNotSame(javaType, newReferencedType);
        assertEquals(handler, newReferencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStaticTyping());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTypingWhenAlreadyStatic() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, true);
        referenceType.withStaticTyping();
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        String canonicalName = referenceType.buildCanonicalName();
        assertTrue(canonicalName.startsWith("[reference type, class "));
        assertTrue(canonicalName.endsWith("]"));
        assertTrue(canonicalName.contains(stringClass.getName()));
        assertTrue(canonicalName.contains("<" + javaType.toCanonical() + ">"));
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        JavaType narrowedType = new ReferenceType(String[].class, javaType, null, null, false).narrow(String[].class);
        assertNotSame(javaType, narrowedType);
        assertEquals(narrowedType.getClass(), ReferenceType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithIncompatibleType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        new ReferenceType(String[].class, javaType, null, null, false).narrow(int.class);
    }

    @Test
    public void testGetRawClass() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(stringClass, referenceType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        assertEquals(javaType, referenceType.getContentType());
    }

    @Test
    public void testGetClassSignature() {
        Class<String> stringClass = String.class;
        StringBuilder expected = new StringBuilder("[reference type, class ");
        StringBuilder actual = new ReferenceType(stringClass, null, null, null, false).getClassSignature(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        StringBuilder expected = new StringBuilder("[reference type, class ");
        StringBuilder actual = new ReferenceType(stringClass, null, null, null, false).getGenericSignature(expected);
        assertEquals(expected.append('<').append(stringClass.getName()).append('>').append(']'), actual);
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = JavaType.construct(String.class, String.class);
        String expected = "[reference type, class " + stringClass.getName() + "<T>" + " of class " + javaType.getClass().getName() + "]";
        String actual = new ReferenceType(stringClass, javaType, null, null, false).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        JavaType javaType1 = JavaType.construct(String.class, String.class);
        JavaType javaType2 = JavaType.construct(String.class, String.class);
        ReferenceType referenceType1 = new ReferenceType(stringClass1, javaType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass2, javaType2, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(stringClass1, javaType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
    }
}