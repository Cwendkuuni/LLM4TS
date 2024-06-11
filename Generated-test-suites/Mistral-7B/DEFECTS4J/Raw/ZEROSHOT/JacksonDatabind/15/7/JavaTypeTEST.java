package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @org.junit.Test
    public void testJavaTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getErasedSignature());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.useStaticType());
    }

    @org.junit.Test
    public void testJavaTypeNarrowing() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType substringJavaType = stringJavaType.narrowBy(String.class.getSuperclass());
        assertEquals(String.class, substringJavaType.getRawClass());
        assertEquals(String.class.getName(), substringJavaType.getErasedSignature());
        assertEquals(0, substringJavaType.containedTypeCount());
        assertNull(substringJavaType.getKeyType());
        assertNull(substringJavaType.getContentType());
        assertFalse(substringJavaType.isContainerType());
        assertFalse(substringJavaType.isCollectionLikeType());
        assertFalse(substringJavaType.isMapLikeType());
        assertFalse(substringJavaType.isAbstract());
        assertFalse(substringJavaType.isInterface());
        assertFalse(substringJavaType.isPrimitive());
        assertFalse(substringJavaType.isFinal());
        assertFalse(substringJavaType.isThrowable());
        assertTrue(substringJavaType.isConcrete());
        assertFalse(substringJavaType.useStaticType());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType integerJavaType = stringJavaType.narrowBy(int.class);
        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test
    public void testJavaTypeWidening() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType objectJavaType = stringJavaType.widenBy(Object.class);
        assertEquals(Object.class, objectJavaType.getRawClass());
        assertEquals("Ljava/lang/Object;", objectJavaType.getErasedSignature());
        assertEquals(0, objectJavaType.containedTypeCount());
        assertNull(objectJavaType.getKeyType());
        assertNull(objectJavaType.getContentType());
        assertFalse(objectJavaType.isContainerType());
        assertFalse(objectJavaType.isCollectionLikeType());
        assertFalse(objectJavaType.isMapLikeType());
        assertFalse(objectJavaType.isAbstract());
        assertFalse(objectJavaType.isInterface());
        assertFalse(objectJavaType.isPrimitive());
        assertFalse(objectJavaType.isFinal());
        assertFalse(objectJavaType.isThrowable());
        assertTrue(objectJavaType.isConcrete());
        assertFalse(objectJavaType.useStaticType());
    }

    @org.junit.Test
    public void testJavaTypeStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType staticStringJavaType = stringJavaType.withStaticTyping();
        assertEquals(String.class, staticStringJavaType.getRawClass());
        assertEquals("Ljava/lang/String;", staticStringJavaType.getErasedSignature());
        assertEquals(0, staticStringJavaType.containedTypeCount());
        assertNull(staticStringJavaType.getKeyType());
        assertNull(staticStringJavaType.getContentType());
        assertFalse(staticStringJavaType.isContainerType());
        assertFalse(staticStringJavaType.isCollectionLikeType());
        assertFalse(staticStringJavaType.isMapLikeType());
        assertFalse(staticStringJavaType.isAbstract());
        assertFalse(staticStringJavaType.isInterface());
        assertFalse(staticStringJavaType.isPrimitive());
        assertFalse(staticStringJavaType.isFinal());
        assertFalse(staticStringJavaType.isThrowable());
        assertTrue(staticStringJavaType.isConcrete());
        assertTrue(staticStringJavaType.useStaticType());
    }

    @org.junit.Test
    public void testJavaTypeTypeHandlers() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        JavaType stringJavaTypeWithValueHandler = stringJavaType.withValueHandler(valueHandler);
        JavaType stringJavaTypeWithTypeHandler = stringJavaType.withTypeHandler(typeHandler);
        assertEquals(valueHandler, stringJavaTypeWithValueHandler.getValueHandler());
        assertEquals(typeHandler, stringJavaTypeWithTypeHandler.getTypeHandler());
        assertNull(stringJavaTypeWithValueHandler.getTypeHandler());
        assertNull(stringJavaTypeWithTypeHandler.getValueHandler());
    }

    @org.junit.Test
    public void testJavaTypeIsContainerType() {
        Class<List<String>> listClass = new ArrayList<String>().getClass();
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(listClass);
        assertTrue(listJavaType.isContainerType());
        assertFalse(listJavaType.isCollectionLikeType());
        assertFalse(listJavaType.isMapLikeType());
    }

    @org.junit.Test
    public void testJavaTypeIsCollectionLikeType() {
        Class<List<String>> listClass = new ArrayList<String>().getClass();
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(listClass);
        assertFalse(listJavaType.isContainerType());
        assertTrue(listJavaType.isCollectionLikeType());
        assertFalse(listJavaType.isMapLikeType());
    }

    @org.junit.Test
    public void testJavaTypeIsMapLikeType() {
        Class<Map<String, String>> mapClass = new HashMap<String, String>().getClass();
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(mapClass);
        assertFalse(mapJavaType.isContainerType());
        assertFalse(mapJavaType.isCollectionLikeType());
        assertTrue(mapJavaType.isMapLikeType());
    }

}