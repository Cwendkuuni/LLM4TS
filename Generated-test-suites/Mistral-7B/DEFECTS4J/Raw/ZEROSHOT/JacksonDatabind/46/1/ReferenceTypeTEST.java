package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(javaType.getClass(), referenceType.getContentType().getClass());
        assertEquals(javaType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(javaType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainsType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaType, containedType);
        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String<T>,");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        JavaType javaType1 = TypeReference.typeReference(String.class);
        Class<Integer> integerClass = Integer.class;
        JavaType javaType2 = TypeReference.typeReference(Integer.class);
        ReferenceType referenceType1 = ReferenceType.construct(stringClass1, javaType1, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(integerClass, javaType2, null, null);
        assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = ReferenceType.construct(stringClass1, javaType1, null, null);
        ReferenceType referenceType4 = referenceType3;
        assertTrue(referenceType3.equals(referenceType4));

        ReferenceType referenceType5 = ReferenceType.construct(stringClass1, javaType1, null, null);
        referenceType5.withTypeHandler(null);
        ReferenceType referenceType6 = ReferenceType.construct(stringClass1, javaType1, null, null);
        referenceType6.withTypeHandler(null);
        assertTrue(referenceType5.equals(referenceType6));

        ReferenceType referenceType7 = ReferenceType.construct(stringClass1, javaType1, null, null);
        ReferenceType referenceType8 = ReferenceType.construct(stringClass1, javaType1, null, null);
        assertTrue(referenceType7.equals(referenceType8));
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        String expected = "[reference type, class java.lang.String<T>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ObjectNode objectNode = new ObjectNode(new StandardJsonParser().getFactory());
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType newTypeHandler = objectNode.getType();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        JavaType newContentTypeHandler = newReferenceType.getContentType();
        assertEquals(javaType, newContentTypeHandler);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ObjectNode objectNode = new ObjectNode(new StandardJsonParser().getFactory());
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        JavaType oldContentTypeHandler = javaType.<Object>getTypeHandler();
        ObjectNode newObjectNode = objectNode.put("newHandler", new Object());
        JavaType newContentTypeHandler = referenceType.getContentType().withTypeHandler(newObjectNode);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        JavaType newReferencedType = newReferenceType.getReferencedType();
        assertEquals(javaType, newReferencedType);
        assertEquals(newObjectNode.getType(), newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ObjectNode objectNode = new ObjectNode(new StandardJsonParser().getFactory());
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ObjectNode objectNode = new ObjectNode(new StandardJsonParser().getFactory());
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        Object oldContentValueHandler = javaType.<Object>getValueHandler();
        Object newContentValueHandler = new Object();
        JavaType newContentTypeHandler = referenceType.getContentType().withValueHandler(newContentValueHandler);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentTypeHandler);
        assertEquals(newContentValueHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeReference.typeReference(String.class);
        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(true, newReferenceType.isStatic());
    }
}