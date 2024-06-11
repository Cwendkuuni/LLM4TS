package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    // List all the public methods and their signatures

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.construct(int.class), null, null);
        assertNotNull(referenceType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        assertNotSame(referenceType, referenceType.withTypeHandler(null));
        assertSame(referenceType, referenceType.withTypeHandler(javaType.getTypeHandler()));
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType1 = JavaType.construct(String.class);
        JavaType javaType2 = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType1, null, null, false);
        assertNotSame(referenceType, referenceType.withContentTypeHandler(null));
        assertSame(referenceType, referenceType.withContentTypeHandler(javaType2.getTypeHandler()));
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        assertNotSame(referenceType, referenceType.withValueHandler(null));
        assertSame(referenceType, referenceType.withValueHandler(javaType.getValueHandler()));
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType1 = JavaType.construct(String.class);
        JavaType javaType2 = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType1, null, null, false);
        assertNotSame(referenceType, referenceType.withContentValueHandler(null));
        assertSame(referenceType, referenceType.withContentValueHandler(javaType2.getValueHandler()));
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        assertFalse(referenceType.isStaticTyping());
        assertTrue(referenceType.withStaticTyping().isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> clazz = String.class;
        JavaType javaType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, javaType, null, null, false);
        String result = referenceType.buildCanonicalName();
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expected, result);
    }

    // Edge cases and exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        ReferenceType.construct(null, JavaType.construct(int.class), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullReferencedType() {
        ReferenceType.construct(String.class, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithTypeHandlerWithNullHandler() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        referenceType.withTypeHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithContentTypeHandlerWithNullHandler() {
        JavaType javaType1 = JavaType.construct(String.class);
        JavaType javaType2 = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType1, null, null, false);
        referenceType.withContentTypeHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithValueHandlerWithNullHandler() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        referenceType.withValueHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithContentValueHandlerWithNullHandler() {
        JavaType javaType1 = JavaType.construct(String.class);
        JavaType javaType2 = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType1, null, null, false);
        referenceType.withContentValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithDuplicateTypes() {
        ReferenceType.construct(String.class, JavaType.construct(String.class), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithIncompatibleTypes() {
        ReferenceType.construct(String.class, JavaType.construct(int.class), null, null);
    }

    @Test
    public void testGetReferencedType() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        JavaType result = referenceType.getReferencedType();
        assertSame(javaType, result);
    }

    @Test
    public void testIsReferenceType() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    // Test other methods

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        int result = referenceType.containedTypeCount();
        assertEquals(1, result);
    }

    @Test
    public void testContainedType() {
        JavaType javaType1 = JavaType.construct(String.class);
        JavaType javaType2 = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType1, null, null, false);
        JavaType result = referenceType.containedType(0);
        assertSame(javaType1, result);
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        String result = referenceType.containedTypeName(0);
        assertEquals("T", result);
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, javaType, null, null, false);
        Class<?> result = referenceType.getParameterSource();
        assertEquals(String.class, result);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> clazz = String.class;
        ReferenceType referenceType = new ReferenceType(clazz, JavaType.construct(Integer.class), null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        String result = sb.toString();
        String expected = "Ljava/lang/String;";
        assertEquals(expected, result);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> clazz = String.class;
        JavaType javaType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, javaType, null, null, false);
        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        String result = sb.toString();
        String expected = "Lcom.fasterxml.jackson.databind.type.ReferenceType<Ljava/lang/String;Ljava/lang/Integer;>;";
        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        Class<?> clazz = String.class;
        JavaType javaType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(clazz, javaType, null, null, false);
        String result = referenceType.toString();
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> clazz1 = String.class;
        JavaType javaType1 = JavaType.construct(Integer.class);
        Class<?> clazz2 = Integer.class;
        JavaType javaType2 = JavaType.construct(Double.class);
        ReferenceType referenceType1 = new ReferenceType(clazz1, javaType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(clazz2, javaType2, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(clazz1, javaType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType3));
    }
}