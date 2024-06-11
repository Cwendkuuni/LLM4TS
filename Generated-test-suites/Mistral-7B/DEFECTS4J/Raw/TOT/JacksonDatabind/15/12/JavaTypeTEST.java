package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeWithValueHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithValueHandler = javaType.withValueHandler("testValueHandler");

        assertEquals(javaType.getClass(), javaTypeWithValueHandler.getClass());
        assertEquals("testValueHandler", javaTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler("testTypeHandler");

        assertEquals(javaType.getClass(), javaTypeWithTypeHandler.getClass());
        assertEquals("testTypeHandler", javaTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(Map.class, String.class, Integer.class);
        JavaType javaTypeWithContentTypeHandler = javaType.withContentTypeHandler("testContentTypeHandler");

        assertEquals(javaType.getClass(), javaTypeWithContentTypeHandler.getClass());
        assertEquals("testContentTypeHandler", javaTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();

        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByInvalidClass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeNarrowBy = javaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeNarrowBySameClass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeNarrowBy = javaType.narrowBy(String.class);

        assertEquals(javaType.getClass(), javaTypeNarrowBy.getClass());
    }

    @Test
    public void testJavaTypeWidenBy() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(Integer.class);
        JavaType javaTypeWidenBy = javaType.widenBy(Number.class);

        assertFalse(javaTypeWidenBy.isPrimitive());
        assertTrue(javaTypeWidenBy.isInstance(new Float(1.0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenByInvalidClass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType javaTypeWidenBy = javaType.widenBy(Integer.class);
    }
}