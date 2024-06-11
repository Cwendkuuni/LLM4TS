package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeDescriptor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaTypeTest {

    @Test
    public void testJavaTypeCreation() {
        // Test creation with no handlers and static typing
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(false, javaType.useStaticType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());

        // Test creation with value handler
        Object valueHandler = new Object() {};
        JavaType javaTypeWithValueHandler = javaType.withValueHandler(valueHandler);
        assertEquals(javaType, javaTypeWithValueHandler);
        assertNotNull(javaTypeWithValueHandler.getValueHandler());
        assertEquals(valueHandler, javaTypeWithValueHandler.getValueHandler());

        // Test creation with type handler
        Object typeHandler = new Object() {};
        JavaType javaTypeWithTypeHandler = javaType.withTypeHandler(typeHandler);
        assertEquals(javaType, javaTypeWithTypeHandler);
        assertNotNull(javaTypeWithTypeHandler.getTypeHandler());
        assertEquals(typeHandler, javaTypeWithTypeHandler.getTypeHandler());

        // Test creation with both value and type handlers
        JavaType javaTypeWithBothHandlers = javaType.withValueHandler(valueHandler).withTypeHandler(typeHandler);
        assertEquals(javaType, javaTypeWithBothHandlers);
        assertNotNull(javaTypeWithBothHandlers.getValueHandler());
        assertEquals(valueHandler, javaTypeWithBothHandlers.getValueHandler());
        assertNotNull(javaTypeWithBothHandlers.getTypeHandler());
        assertEquals(typeHandler, javaTypeWithBothHandlers.getTypeHandler());

        // Test creation with static typing
        JavaType javaTypeWithStaticTyping = javaTypeWithBothHandlers.withStaticTyping();
        assertEquals(javaTypeWithBothHandlers, javaTypeWithStaticTyping);
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<String> rawClass = String[].class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<String> subclass = String.class;
        JavaType narrowedType = javaType.narrowBy(subclass);
        assertEquals(javaType, narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<Integer> subclass = Integer.class;
        JavaType narrowedType = javaType.narrowBy(subclass);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testJavaTypeWidening() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<?> superclass = Object[].class;
        JavaType widenedType = javaType.widenBy(superclass);
        assertEquals(javaType, widenedType);
        assertEquals(Object[].class, widenedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningInvalid() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<Number> superclass = Number.class;
        JavaType widenedType = javaType.widenBy(superclass);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testJavaTypeNarrowingContents() {
        Class<List<String>> rawClass = new TypeDescriptor.ParameterizedType(List.class, String.class).getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<List<Integer>> contentClass = new TypeDescriptor.ParameterizedType(List.class, Integer.class).getRawType();
        JavaType narrowedType = javaType.narrowContentsBy(contentClass);
        assertEquals(javaType, narrowedType);
        assertEquals(List.class, narrowedType.getRawClass());
        assertEquals(Integer.class, narrowedType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingContentsInvalid() {
        Class<List<String>> rawClass = new TypeDescriptor.ParameterizedType(List.class, String.class).getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<Map<String, Integer>> contentClass = new TypeDescriptor.ParameterizedType(Map.class, String.class, Integer.class).getRawType();
        JavaType narrowedType = javaType.narrowContentsBy(contentClass);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testJavaTypeWideningContents() {
        Class<List<String>> rawClass = new TypeDescriptor.ParameterizedType(List.class, String.class).getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        Class<List<Object>> contentClass = new TypeDescriptor.ParameterizedType(List.class, Object.class).getRawType();
        JavaType widenedType = javaType.widenContentsBy(contentClass);
        assertEquals(javaType, widenedType);
        assertEquals(List.class, widenedType.getRawClass());
        assertNull(widenedType.getContentType());
    }

    @Test
    public void testJavaTypeWideningContentsToUnknown() {
        Class<List<String>> rawClass = new TypeDescriptor.ParameterizedType(List.class, String.class).getRawType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);

        JavaType widenedType = javaType.widenContentsBy(TypeFactory.unknownType());
        assertEquals(javaType, widenedType);
        assertEquals(List.class, widenedType.getRawClass());
        assertNull(widenedType.getContentType());
    }
}