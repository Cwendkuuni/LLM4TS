package com.example;

import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testConstructor() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(0, javaType.hashCode());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isConcrete());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubclass() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        javaType.narrowBy(Integer.class);
    }

    @Test
    public void testNarrowBy() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType stringType = javaType.narrowBy(String.class);
        JavaType substringType = javaType.narrowBy(Substring.class);
        assertSame(javaType, stringType);
        assertSame(javaType, substringType);

        Class<Substring> substringClass = Substring.class;
        JavaType substringType2 = new JavaType(substringClass, 0, null, null, false).narrowBy(String.class);
        assertNotNull(substringType2.getValueHandler());
        assertSame(javaType, substringType2);
    }

    @Test
    public void testWidenBy() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType objectType = javaType.widenBy(Object.class);
        assertSame(Object.class, objectType.getRawClass());
        assertSame(javaType, objectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSuperclass() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        javaType.widenBy(String.class);
    }

    @Test
    public void testNarrowContentsBy() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType stringListType = javaType.narrowContentsBy(String.class);
        assertSame(List.class, stringListType.getRawClass());
        assertTrue(stringListType.isCollectionLikeType());
        assertFalse(stringListType.isMapLikeType());
        assertFalse(stringListType.useStaticType());
    }

    @Test
    public void testWidenContentsBy() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType objectListType = javaType.widenContentsBy(Object.class);
        assertSame(List.class, objectListType.getRawClass());
        assertTrue(objectListType.isCollectionLikeType());
        assertFalse(objectListType.isMapLikeType());
        assertFalse(objectListType.useStaticType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        assertEquals("<java.lang.String>", sb.toString());
    }

    @Test
    public void testToString() {
        Class<List<String>> rawType = List.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        String string = javaType.toString();
        assertNotNull(string);
        assertTrue(string.contains("java.util.List<java.lang.String>"));
    }

    @Test
    public void testEquals() {
        Class<List<String>> rawType = List.class;
        JavaType javaType1 = new JavaType(rawType, 0, null, null, false);
        JavaType javaType2 = new JavaType(rawType, 0, null, null, false);
        assertTrue(javaType1.equals(javaType2));

        JavaType javaType3 = new JavaType(rawType, 1, null, null, false);
        assertFalse(javaType1.equals(javaType3));

        JavaType javaType4 = new TypeFactory().unresolvedType(rawType);
        assertFalse(javaType1.equals(javaType4));
    }

    public static class Substring {
    }
}