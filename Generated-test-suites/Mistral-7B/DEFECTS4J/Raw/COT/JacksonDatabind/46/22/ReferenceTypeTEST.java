package com.example.junit; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.getClass(), referenceType.getContentType().getClass());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        referenceType.withTypeHandler(null);
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(refType, referencedType);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
        assertEquals(cls, withStaticTyping.getRawClass());
        assertEquals(refType, withStaticTyping.getContentType());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType originalRefType = JavaType.construct(String[].class);
        JavaType newRefType = JavaType.construct(Object[].class);
        ReferenceType referenceType = new ReferenceType(cls, originalRefType, null, null, false);
        JavaType narrowed = referenceType._narrow(Object[].class);
        assertEquals(newRefType, narrowed);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidNarrow() {
        Class<?> cls = String.class;
        JavaType originalRefType = JavaType.construct(String[].class);
        JavaType newRefType = JavaType.construct(int[].class);
        ReferenceType referenceType = new ReferenceType(cls, originalRefType, null, null, false);
        referenceType._narrow(int[].class); // should throw an exception
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder("class [Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder("class com.fasterxml.jackson.databind.type.ReferenceType<T>[]");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);

        Class<?> cls2 = String.class;
        JavaType refType2 = JavaType.construct(long.class);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);

        Class<?> cls3 = String.class;
        JavaType refType3 = JavaType.construct(int.class);
        ReferenceType referenceType3 = new ReferenceType(cls3, refType3, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1.withTypeHandler(null)));
        assertTrue(referenceType1.equals(referenceType1.withContentTypeHandler(null)));
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }

}