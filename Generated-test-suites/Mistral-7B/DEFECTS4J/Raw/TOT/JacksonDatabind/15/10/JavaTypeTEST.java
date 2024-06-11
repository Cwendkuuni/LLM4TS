package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaTypeTest {

    @Test
    public void testJavaTypeInstance() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        // test basic properties
        assertEquals("java.lang.String", javaType.getRawClass().getName());
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(JavaType.class, javaType.getClass());
        assertEquals(true, javaType.isConcrete());
        assertEquals(false, javaType.isInterface());
        assertEquals(false, javaType.isArrayType());
        assertEquals(false, javaType.isEnumType());
        assertEquals(false, javaType.isPrimitive());
        assertEquals(false, javaType.isFinal());
        assertEquals(false, javaType.isContainerType());
        assertEquals(false, javaType.isCollectionLikeType());
        assertEquals(false, javaType.isMapLikeType());
        assertEquals(false, javaType.useStaticType());
        assertEquals(0, javaType.containedTypeCount());

        // test methods
        JavaType stringType = javaType.narrowBy(String.class);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(JavaType.class, stringType.getClass());
        assertEquals(true, stringType.isConcrete());
        assertEquals(false, stringType.isInterface());
        assertEquals(false, stringType.isArrayType());
        assertEquals(false, stringType.isEnumType());
        assertEquals(false, stringType.isPrimitive());
        assertEquals(false, stringType.isFinal());
        assertEquals(false, stringType.isContainerType());
        assertEquals(false, stringType.isCollectionLikeType());
        assertEquals(false, stringType.isMapLikeType());
        assertEquals(true, stringType.useStaticType());
        assertEquals(0, stringType.containedTypeCount());

        JavaType objectType = javaType.widenBy(Object.class);
        assertEquals(Object.class, objectType.getRawClass());
        assertEquals(JavaType.class, objectType.getClass());
        assertEquals(true, objectType.isConcrete());
        assertEquals(false, objectType.isInterface());
        assertEquals(false, objectType.isArrayType());
        assertEquals(false, objectType.isEnumType());
        assertEquals(true, objectType.isPrimitive());
        assertEquals(false, objectType.isFinal());
        assertEquals(false, objectType.isContainerType());
        assertEquals(false, objectType.isCollectionLikeType());
        assertEquals(false, objectType.isMapLikeType());
        assertEquals(false, objectType.useStaticType());
        assertEquals(0, objectType.containedTypeCount());

        JavaType listType = TypeFactory.defaultInstance().constructType(java.util.List.class);
        JavaType stringListType = listType.narrowBy(java.util.List.class);
        JavaType genericType = stringListType.narrowBy(java.util.ArrayList.class);
        JavaType stringType2 = genericType.narrowBy(String.class);

        assertEquals(String.class, stringType2.getRawClass());
        assertEquals(JavaType.class, stringType2.getClass());
        assertEquals(true, stringType2.isConcrete());
        assertEquals(false, stringType2.isInterface());
        assertEquals(false, stringType2.isArrayType());
        assertEquals(false, stringType2.isEnumType());
        assertEquals(false, stringType2.isPrimitive());
        assertEquals(false, stringType2.isFinal());
        assertEquals(false, stringType2.isContainerType());
        assertEquals(true, stringType2.isCollectionLikeType());
        assertEquals(true, stringType2.isMapLikeType());
        assertEquals(false, stringType2.useStaticType());
        assertEquals(0, stringType2.containedTypeCount());
    }
}