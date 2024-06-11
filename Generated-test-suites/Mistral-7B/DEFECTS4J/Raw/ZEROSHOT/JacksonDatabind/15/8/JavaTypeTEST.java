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
        assertEquals(JavaType.class, javaType.getClass());
        assertEquals(String.class.getName(), javaType.toString());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.useStaticType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.getParameterSource());

        assertEquals(TypeFactory.defaultInstance().constructType(String[].class), javaType.narrowBy(String[].class));
        assertNull(javaType.narrowBy(Integer.class));
        assertEquals(TypeFactory.defaultInstance().constructType(String[].class), javaType.widenBy(String[].class));
        assertNull(javaType.widenBy(Integer.class));
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        Object valueHandler = new Object() {};
        JavaType javaTypeWithValueHandler = javaType.withValueHandler(valueHandler);

        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertEquals(javaType.getClass(), javaTypeWithValueHandler.getClass());
        assertEquals(javaType.getRawClass(), javaTypeWithValueHandler.getRawClass());
        assertEquals(javaType.toString(), javaTypeWithValueHandler.toString());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        Object typeHandler = new Object() {};
        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler(typeHandler);

        assertNotNull(javaTypeWithTypeHandler.getTypeHandler());
        assertEquals(javaType.getClass(), javaTypeWithTypeHandler.getClass());
        assertEquals(javaType.getRawClass(), javaTypeWithTypeHandler.getRawClass());
        assertEquals(javaType.toString(), javaTypeWithTypeHandler.toString());
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();

        assertTrue(javaTypeWithStaticTyping.useStaticType());
        assertEquals(javaType.getClass(), javaTypeWithStaticTyping.getClass());
        assertEquals(javaType.getRawClass(), javaTypeWithStaticTyping.getRawClass());
        assertEquals(javaType.toString(), javaTypeWithStaticTyping.toString());
    }

    @Test
    public void testJavaTypeNarrow() {
        Class<String> stringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType arrayOfStringJavaType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType narrowedJavaType = stringJavaType.narrowBy(String[].class);

        assertEquals(arrayOfStringJavaType, narrowedJavaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowInvalid() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        javaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeWiden() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType widenedJavaType = javaType.widenBy(Object.class);

        assertEquals(TypeFactory.defaultInstance().constructType(Object.class), widenedJavaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenInvalid() {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        javaType.widenBy(Integer.class);
    }
}