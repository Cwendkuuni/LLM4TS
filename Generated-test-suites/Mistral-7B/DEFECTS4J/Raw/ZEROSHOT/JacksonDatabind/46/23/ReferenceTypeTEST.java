package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.types.ClassType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(javaType.getClass(), referenceType.getContentType().getClass());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testTypeHandlers() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        JavaType originalTypeHandler = referenceType.getTypeHandler();
        JavaType newTypeHandler = TypeFactory.defaultInstance().constructType(Integer[].class);

        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newTypeHandler);
        assertNotEquals(originalTypeHandler, newReferenceType.getTypeHandler());
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(javaType, newReferenceType.getContentType());

        JavaType originalValueHandler = referenceType.getValueHandler();
        JavaType newValueHandler = TypeFactory.defaultInstance().constructType(Long.class);

        ReferenceType newReferenceTypeWithValueHandler = newReferenceType.withValueHandler(newValueHandler);
        assertNotEquals(originalValueHandler, newReferenceTypeWithValueHandler.getValueHandler());
        assertEquals(newValueHandler, newReferenceTypeWithValueHandler.getValueHandler());
        assertEquals(newReferenceType.getRawClass(), newReferenceTypeWithValueHandler.getRawClass());
        assertEquals(javaType, newReferenceTypeWithValueHandler.getContentType());
    }

    @Test
    public void testStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        ReferenceType staticReferenceType = referenceType.withStaticTyping();

        assertFalse(referenceType.isStatic());
        assertTrue(staticReferenceType.isStatic());
        assertEquals(referenceType.getRawClass(), staticReferenceType.getRawClass());
        assertEquals(javaType, staticReferenceType.getContentType());
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        Class<Integer> intClass = Integer.class;

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
        JavaType narrowedType = referenceType.narrow(intClass);

        assertEquals(intClass, narrowedType.getRawClass());
        assertEquals(javaType, narrowedType.getContentType());
        assertTrue(narrowedType instanceof ReferenceType);
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(javaType, referencedType);
    }

    @Test
    public void testGetRawClass() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        Class<String> rawClass = referenceType.getRawClass();
        assertEquals(stringClass, rawClass);
    }

    @Test
    public void testGetContentType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        JavaType contentType = referenceType.getContentType();
        assertEquals(javaType, contentType);
    }

    @Test
    public void testContainsType() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        JavaType containedType = referenceType.containedType(0);
        assertEquals(javaType, containedType);
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainsTypeName() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        String containedTypeName = referenceType.containedTypeName(0);
        assertEquals("T", containedTypeName);
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        assertEquals(stringClass, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);

        String result = sb.toString();
        assertEquals("[Ljava.lang.String;", result);
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);

        String result = sb.toString();
        assertEquals("[Ljava.lang.String;<T extends java.lang.Object>]", result);
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);

        String result = referenceType.toString();

        String expected = "[reference type, class " + stringClass.getName() + "<T>" +
                "{'type': '" + javaType.toString() + "', 'referencedType': '" + javaType + "'}]";

        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        Class<String> stringClass2 = String.class;
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType1 = new ReferenceType(stringClass1, javaType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(stringClass2, javaType2, null, null, false);
        ReferenceType identicalReferenceType = new ReferenceType(stringClass1, javaType1, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(identicalReferenceType));
        assertTrue(identicalReferenceType.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));

        JavaType differentContentType = TypeFactory.defaultInstance().constructType(Integer[].class);
        ReferenceType differentContentTypeReferenceType = new ReferenceType(stringClass1, differentContentType, null, null, false);

        assertFalse(referenceType1.equals(differentContentTypeReferenceType));
        assertFalse(differentContentTypeReferenceType.equals(referenceType1));

        ReferenceType differentRawClassReferenceType = new ReferenceType(Integer.class, javaType1, null, null, false);

        assertFalse(referenceType1.equals(differentRawClassReferenceType));
        assertFalse(differentRawClassReferenceType.equals(referenceType1));

        SimpleType simpleType = new SimpleType(stringClass1, javaType1.getClass(), null, null, false);

        assertFalse(referenceType1.equals(simpleType));
        assertFalse(simpleType.equals(referenceType1));
    }
}