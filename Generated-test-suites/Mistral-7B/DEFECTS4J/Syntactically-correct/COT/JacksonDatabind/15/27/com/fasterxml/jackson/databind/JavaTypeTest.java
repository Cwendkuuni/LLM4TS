package com.example; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {
    

    @Test
    public void testJavaTypeConstruction() {
        Class<String> rawStringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawStringClass);
        assertEquals(String.class, javaType.getRawClass());
        assertTrue(javaType instanceof JavaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidSubclass() {
        Class<String> rawStringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType invalidSubclass = javaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeCopyMethods() {
        Class<String> rawStringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawStringClass);

        Object valueHandler = new Object();
        JavaType javaTypeWithValueHandler = javaType.withValueHandler(valueHandler);
        assertEquals(valueHandler, javaTypeWithValueHandler.getValueHandler());

        Object typeHandler = new Object();
        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, javaTypeWithTypeHandler.getTypeHandler());

        boolean asStatic = true;
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.useStaticType() == asStatic);
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<String> rawStringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(rawStringClass);
        JavaType narrowedType = stringJavaType.narrowBy(StringBuffer.class);
        assertTrue(StringBuffer.class.isAssignableFrom(narrowedType.getRawClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() {
        Class<String> rawStringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(rawStringClass);
        JavaType narrowedType = stringJavaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<String> rawStringClass = String.class;
        JavaType stringJavaType = TypeFactory.defaultInstance().constructType(rawStringClass);
        JavaType widenedType = stringJavaType.widenBy(Object.class);
        assertTrue(Object.class.isAssignableFrom(widenedType.getRawClass()));
    }

    @Test
    public void testJavaTypeIsContainerType() {
        Class<List<String>> rawListClass = List.class.getComponentType();
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(rawListClass);
        assertTrue(listJavaType.isContainerType());
    }

    
}