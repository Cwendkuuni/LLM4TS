package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeWithValueHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class, new String[]{"java.lang.String"});
        JavaType javaTypeWithValueHandler = javaType.withValueHandler("MyValueHandler");

        assertEquals(javaType.getClass(), javaTypeWithValueHandler.getClass());
        assertEquals(javaType._valueHandler, null);
        assertEquals(javaTypeWithValueHandler._valueHandler, "MyValueHandler");
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class, new String[]{"java.lang.String"});
        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler("MyTypeHandler");

        assertEquals(javaType.getClass(), javaTypeWithTypeHandler.getClass());
        assertEquals(javaType._typeHandler, null);
        assertEquals(javaTypeWithTypeHandler._typeHandler, "MyTypeHandler");
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class, new String[]{"java.lang.String"});
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();

        assertEquals(javaType.getClass(), javaTypeWithStaticTyping.getClass());
        assertEquals(javaType._asStatic, false);
        assertEquals(javaTypeWithStaticTyping._asStatic, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByWithIncompatibleClass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class, new String[]{"java.lang.String"});
        JavaType javaTypeNarrowed = javaType.narrowBy(Integer.class);
    }

    @Test
    public void testWidenByWithCompatibleClass() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(String[].class, new String[]{"java.lang.String"});
        JavaType javaTypeWidened = javaType.widenBy(Object[].class);

        assertEquals(javaType.getClass(), javaTypeWidened.getClass());
    }

    @Test
    public void testIsContainerType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeArray = typeFactory.constructType(String[].class, new String[]{"java.lang.String"});
        JavaType javaTypeMap = typeFactory.constructType(Map.class, new Class[] {String.class, Integer.class});
        JavaType javaTypeCollection = typeFactory.constructType(ArrayList.class, new Class[] {String.class});

        assertTrue(javaTypeArray.isContainerType());
        assertTrue(javaTypeMap.isContainerType());
        assertTrue(javaTypeCollection.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeCollection = typeFactory.constructType(ArrayList.class, new Class[] {String.class});
        JavaType javaTypeMap = typeFactory.constructType(Map.class, new Class[] {String.class, Integer.class});

        assertFalse(javaTypeMap.isCollectionLikeType());
        assertTrue(javaTypeCollection.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeMap = typeFactory.constructType(Map.class, new Class[] {String.class, Integer.class});

        assertTrue(javaTypeMap.isMapLikeType());
    }
}