package com.example.referenceType; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType actual = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType expected = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType actual = referenceType.withTypeHandler(newTypeHandler);
        ReferenceType expected = new ReferenceType(cls, refType, valueHandler, newTypeHandler, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType actual = referenceType.getReferencedType();
        JavaType expected = refType;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        boolean actual = referenceType.isReferenceType();
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<Double> subclass = Double.class;
        JavaType actual = referenceType._narrow(subclass);
        fail("Expected AssertionError but got: " + actual);
    }

    @Test
    public void testNarrowWithValidSubclass() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Class<StringBuffer> subclass = StringBuffer.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType actual = referenceType._narrow(subclass);
        JavaType expected = new ReferenceType(subclass, refType, null, null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType actual = referenceType.withContentTypeHandler(newTypeHandler);
        ReferenceType expected = new ReferenceType(cls, refType, valueHandler, newTypeHandler, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object newValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType actual = referenceType.withValueHandler(newValueHandler);
        ReferenceType expected = new ReferenceType(cls, refType, newValueHandler, null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object newValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType actual = referenceType.withContentValueHandler(newValueHandler);
        ReferenceType expected = new ReferenceType(cls, refType, null, newValueHandler, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType actual = referenceType.withStaticTyping();
        ReferenceType expected = new ReferenceType(cls, refType, null, null, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        String actual = new ReferenceType(cls, refType, null, null, false).buildCanonicalName();
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expected, actual);
    }

    @Test
    public void testContainsType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        int actual = referenceType.containedTypeCount();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType actual = referenceType.containedType(0);
        JavaType expected = refType;
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String actual = referenceType.containedTypeName(0);
        String expected = "T";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<?> actual = referenceType.getParameterSource();
        Class<?> expected = cls;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = new ReferenceType(cls, refType, null, null, false).getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;<T>Lcom/fasterxml/jackson/databind/JavaType;>");
        StringBuilder actual = new ReferenceType(cls, refType, null, null, false).getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        String actual = new ReferenceType(cls, refType, null, null, false).toString();
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = JavaType.construct(int.class);
        Class<String> cls2 = String.class;
        JavaType refType2 = JavaType.construct(double.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);
        boolean actual = referenceType1.equals(referenceType2);
        boolean expected = false;
        assertEquals(expected, actual);

        ReferenceType referenceType3 = new ReferenceType(cls1, refType1, null, null, false);
        boolean actual2 = referenceType1.equals(referenceType3);
        boolean expected2 = true;
        assertEquals(expected2, actual2);
    }
}