package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    // List all the public methods and their signatures

    // Constructor
    @Test
    public void testConstructor() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        ReferenceType expected = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        assertEquals(expected, referenceType);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null);
        assertEquals(refType, referenceType.getContentType());
    }

    // withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler
    @Test
    public void testWithTypeHandler() {
        JavaType typeHandler = new SimpleType(long.class);
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType contentTypeHandler = new SimpleType(float.class);
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object() {};
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
    }

    // withStaticTyping
    @Test
    public void testWithStaticTyping() {
        boolean asStatic = false;
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStatic());
        asStatic = true;
        ReferenceType newReferenceType2 = referenceType.withStaticTyping();
        assertFalse(newReferenceType2.isStatic());
    }

    // getReferencedType, isReferenceType
    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new SimpleType(double.class);
        ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
        JavaType expected = referencedType;
        JavaType actual = referenceType.getReferencedType();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    // containedTypeCount, containedType, containedTypeName, getParameterSource
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        int expected = 1;
        int actual = referenceType.containedTypeCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        JavaType expected = new SimpleType(int.class);
        JavaType actual = referenceType.containedType(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        String expected = "T";
        String actual = referenceType.containedTypeName(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, new SimpleType(int.class), null, null, false);
        Class<?> expected = String.class;
        Class<?> actual = referenceType.getParameterSource();
        assertEquals(expected, actual);
    }

    // toString
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String expected = "[reference type, class [" + cls.getName() + "], class [" + refType.getTypeName() + "]]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    // equals
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType1 = new ReferenceType(cls, refType, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType1.equals(referenceType2));
        Object obj = new Object();
        assertFalse(referenceType1.equals(obj));
        ReferenceType referenceType3 = new ReferenceType(int.class, refType, null, null, false);
        assertFalse(referenceType1.equals(referenceType3));
    }

    // Narrow
    @Test(expected = AssertionError.class)
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        referenceType.narrow(Object.class);
    }

    // Extended API
    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T:Ljava/lang/Object;>Lcom/fasterxml/jackson/databind/type/ReferenceType;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType(int.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T:Ljava/lang/Object;>L");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }
}