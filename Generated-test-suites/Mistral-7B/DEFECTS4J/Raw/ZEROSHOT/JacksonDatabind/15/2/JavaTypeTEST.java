package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testJavaTypeInstance() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        assertEquals(String.class, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertNull(javaType.containedType(0));
        assertNull(javaType.containedTypeName(0));
        assertNull(javaType.getParameterSource());

        assertEquals("java.lang.String", javaType.getGenericSignature());
        assertEquals("Ljava/lang/String;", javaType.getErasedSignature());
    }

    @Test
    public void testJavaTypeModule() {
        Class<Integer> integerClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(integerClass);

        assertEquals(Integer.class, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertNull(javaType.containedTypeName(0));
        assertNull(javaType.getParameterSource());

        assertEquals("Ljava/lang/Integer;", javaType.getErasedSignature());
        assertEquals("Ljava/lang/Integer;<TT;>K", javaType.getGenericSignature());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Object valueHandler = new Object() {};
        JavaType javaTypeWithValueHandler = javaType.withValueHandler(valueHandler);

        assertEquals(String.class, javaTypeWithValueHandler.getRawClass());
        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertEquals(valueHandler, javaTypeWithValueHandler.getValueHandler());
        assertEquals(javaType, javaTypeWithValueHandler.withoutValueHandler());
    }

    @Test
    public void testJavaTypeWithTypeHandler_Type() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Object valueHandler = new Object() {};
        @SuppressWarnings("unchecked")
        JavaType<String> javaTypeWithValueHandler = (JavaType<String>) javaType.withValueHandler((Type) valueHandler);

        assertEquals(String.class, javaTypeWithValueHandler.getRawClass());
        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertEquals(valueHandler, javaTypeWithValueHandler.getValueHandler());
        assertEquals(javaType, javaTypeWithValueHandler.withoutValueHandler());
    }

    @Test
    public void testJavaTypeWithTypeHandler_Value() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Object valueHandler = new Object() {};
        @SuppressWarnings("unchecked")
        JavaType<String> javaTypeWithValueHandler = (JavaType<String>) javaType.withValueHandler(valueHandler);

        assertEquals(String.class, javaTypeWithValueHandler.getRawClass());
        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertEquals(valueHandler, javaTypeWithValueHandler.getValueHandler());
        assertEquals(javaType, javaTypeWithValueHandler.withoutValueHandler());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Object contentValueHandler = new Object() {};
        JavaType javaTypeWithContentValueHandler = javaType.withContentValueHandler(contentValueHandler);

        assertEquals(List.class, javaTypeWithContentValueHandler.getRawClass());
        assertNotNull(javaTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(contentValueHandler, javaTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(javaType, javaTypeWithContentValueHandler.withoutContentValueHandler());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler_Type() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Object contentValueHandler = new Object() {};
        @SuppressWarnings("unchecked")
        JavaType<List<String>> javaTypeWithContentValueHandler = (JavaType<List<String>>) javaType.withContentValueHandler((Type) contentValueHandler);

        assertEquals(List.class, javaTypeWithContentValueHandler.getRawClass());
        assertNotNull(javaTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(contentValueHandler, javaTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(javaType, javaTypeWithContentValueHandler.withoutContentValueHandler());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler_Value() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Object contentValueHandler = new Object() {};
        @SuppressWarnings("unchecked")
        JavaType<List<String>> javaTypeWithContentValueHandler = (JavaType<List<String>>) javaType.withContentValueHandler(contentValueHandler);

        assertEquals(List.class, javaTypeWithContentValueHandler.getRawClass());
        assertNotNull(javaTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(contentValueHandler, javaTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(javaType, javaTypeWithContentValueHandler.withoutContentValueHandler());
    }

    @Test
    public void testJavaTypeWithTypeHandler_static() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        @SuppressWarnings("unchecked")
        JavaType<String> javaTypeWithStaticTyping = (JavaType<String>) javaType.withStaticTyping();

        assertEquals(String.class, javaTypeWithStaticTyping.getRawClass());
        assertTrue(javaTypeWithStaticTyping.useStaticType());
        assertFalse(javaTypeWithStaticTyping.withoutStaticTyping().useStaticType());
    }

    @Test
    public void testJavaTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Class<StringBuffer> stringBufferClass = StringBuffer.class;

        JavaType stringBufferType = javaType.narrowBy(stringBufferClass);
        assertEquals(StringBuffer.class, stringBufferType.getRawClass());
        assertEquals(javaType, stringBufferType.withoutTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrow_notAssignable() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Class<Integer> integerClass = Integer.class;

        JavaType integerType = javaType.narrowBy(integerClass);
    }

    @Test
    public void testJavaTypeWiden() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Class<Object> objectClass = Object.class;

        JavaType objectType = javaType.widenBy(objectClass);
        assertEquals(Object.class, objectType.getRawClass());
        assertEquals(javaType, objectType.withoutTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWiden_notAssignable() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        Class<Integer> integerClass = Integer.class;

        JavaType integerType = javaType.widenBy(integerClass);
    }

    @Test
    public void testJavaTypeNarrowContents() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Class<List<Integer>> listIntegerClass = new ArrayList<Integer>().getClass();

        JavaType listIntegerType = javaType.narrowContentsBy(listIntegerClass);
        assertEquals(List.class, listIntegerType.getRawClass());
        assertEquals(Integer.class, listIntegerType.containedType(0).getRawClass());
        assertEquals(javaType, listIntegerType.withoutTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowContents_notAssignable() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Class<List<Double>> listDoubleClass = new ArrayList<Double>().getClass();

        JavaType listDoubleType = javaType.narrowContentsBy(listDoubleClass);
    }

    @Test
    public void testJavaTypeWidenContents() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Class<List<Object>> listObjectClass = new ArrayList<Object>().getClass();

        JavaType listObjectType = javaType.widenContentsBy(listObjectClass);
        assertEquals(List.class, listObjectType.getRawClass());
        assertEquals(Object.class, listObjectType.containedType(0).getRawClass());
        assertEquals(javaType, listObjectType.withoutTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenContents_notAssignable() {
        Class<List<String>> listStringClass = new ArrayList<String>().getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listStringClass);
        Class<List<Integer>> listIntegerClass = new ArrayList<Integer>().getClass();

        JavaType listIntegerType = javaType.widenContentsBy(listIntegerClass);
    }
}