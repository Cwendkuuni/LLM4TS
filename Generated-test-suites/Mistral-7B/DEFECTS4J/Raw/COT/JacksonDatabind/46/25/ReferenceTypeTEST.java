package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        Assertions.assertEquals(cls, referenceType.getRawClass());
        Assertions.assertEquals(refType.getClass(), referenceType._referencedType.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        Object typeHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        Assertions.assertNotEquals(referenceType, newReferenceType);
        Assertions.assertEquals(typeHandler, newReferenceType._typeHandler);
    }

    @Test
    public void testGetReferencedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType actualType = referenceType.getReferencedType();
        Assertions.assertEquals(refType, actualType);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        boolean actualResult = referenceType.isReferenceType();
        Assertions.assertTrue(actualResult);
    }

    @Test
    public void testContainsTypeCount() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        int actualResult = referenceType.containedTypeCount();
        Assertions.assertEquals(1, actualResult);
    }

    @Test
    public void testContainedType() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType actualType = referenceType.containedType(0);
        Assertions.assertEquals(refType, actualType);
    }

    @Test
    public void testContainedTypeName() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String actualResult = referenceType.containedTypeName(0);
        Assertions.assertEquals("T", actualResult);
    }

    @Test
    public void testGetParameterSource() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        Class<?> actualResult = referenceType.getParameterSource();
        Assertions.assertEquals(cls, actualResult);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expectedResult = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actualResult = referenceType.getErasedSignature(new StringBuilder());
        Assertions.assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expectedResult = new StringBuilder("[Ljava/lang/String;<T>Ljava/lang/Object;");
        StringBuilder actualResult = referenceType.getGenericSignature(new StringBuilder());
        Assertions.assertEquals(expectedResult.toString(), actualResult.toString());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expectedResult = "[reference type, class [Ljava.lang.String;<T>Ljava/lang/Object;]";
        String actualResult = referenceType.toString();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEquals() {
        Class<String> cls1 = String.class;
        JavaType refType1 = JavaType.construct(Integer.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);

        Class<String> cls2 = String.class;
        JavaType refType2 = JavaType.construct(Long.class);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);

        Class<String> cls3 = String.class;
        JavaType refType3 = JavaType.construct(Integer.class);
        ReferenceType referenceType3 = new ReferenceType(cls3, refType3, null, null, false);

        Object referenceType4 = new Object() {};

        Assertions.assertTrue(referenceType1.equals(referenceType1));
        Assertions.assertFalse(referenceType1.equals(referenceType2));
        Assertions.assertFalse(referenceType1.equals(referenceType3));
        Assertions.assertFalse(referenceType1.equals(referenceType4));

        ReferenceType newReferenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        Assertions.assertTrue(referenceType1.equals(newReferenceType1));
    }

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        JavaType expectedResult = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType actualResult = referenceType.narrow(String.class);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        Assertions.assertTrue(newReferenceType._asStatic);
    }
}