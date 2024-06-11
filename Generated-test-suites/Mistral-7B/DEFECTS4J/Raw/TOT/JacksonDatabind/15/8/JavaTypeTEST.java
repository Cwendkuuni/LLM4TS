package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJavaTypeConstruction() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        assertEquals(int[].class, javaType.getRawClass());
        assertTrue(javaType.isContainerType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testJavaTypeWithValueHandler() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(String.class)
                .withValueHandler(new MyStringValueHandler());

        assertEquals(String.class, javaType.getRawClass());
        assertNotNull(javaType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidSubclass() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(String.class)
                .narrowBy(int[].class);
    }

    @Test
    public void testJavaTypeNarrowing() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(int[].class);

        JavaType narrowedType = javaType.narrowBy(int[].class);
        assertEquals(int[].class, narrowedType.getRawClass());
        assertEquals(javaType, narrowedType);
    }

    @Test
    public void testJavaTypeWidening() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(int[].class);

        JavaType widenedType = javaType.widenBy(Object.class);
        assertNotNull(widenedType);
        assertTrue(widenedType.hasRawClass(Object.class));
    }

    @Test
    public void testJavaTypeIsContainerType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(List.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testJavaTypeIsMapLikeType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Map.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(ArrayList.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testJavaTypeIsEnumType() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Color.class);
        assertTrue(javaType.isEnumType());
    }

    @Test
    public void testJavaTypeIsInterface() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Comparable.class);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testJavaTypeIsPrimitive() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(int.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testJavaTypeIsFinal() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(String.class);
        assertFalse(javaType.isFinal());

        JavaType finalStringType = TypeFactory.defaultInstance()
                .constructType(String.class, true);
        assertTrue(finalStringType.isFinal());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(String.class)
                .withContentTypeHandler(new MyStringContentTypeHandler());

        assertEquals(String.class, javaType.getRawClass());
        assertNotNull(javaType.getTypeHandler());
    }

    interface MyValueHandler<T> {
        T handle(T value);
    }

    static class MyStringValueHandler implements MyValueHandler<String> {
        @Override
        public String handle(String value) {
            return value.toUpperCase();
        }
    }

    static class MyStringContentTypeHandler {
        // empty implementation
    }
}