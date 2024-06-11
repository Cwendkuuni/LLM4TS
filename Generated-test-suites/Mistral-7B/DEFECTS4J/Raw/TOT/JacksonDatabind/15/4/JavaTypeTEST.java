package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaTypeTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJavaTypeConstruction() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals("java.lang.String", javaType.toString());
    }

    @Test
    public void testJavaTypeNarrowing() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType narrowType = javaType.narrowBy(String[].class);
        assertEquals(String[].class, narrowType.getRawClass());
        assertEquals("java.lang.String[]", narrowType.toString());

        JavaType forcedNarrowType = javaType.forcedNarrowBy(String[].class);
        assertEquals(String[].class, forcedNarrowType.getRawClass());
        assertEquals("java.lang.String[]", forcedNarrowType.toString());
    }

    @Test
    public void testJavaTypeWidening() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType widenedType = javaType.widenBy(Object.class);
        assertEquals(Object.class, widenedType.getRawClass());
        assertEquals("java.lang.Object", widenedType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidNarrowing() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType invalidNarrowType = javaType.narrowBy(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidWidening() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType invalidWidenType = javaType.widenBy(Number[].class);
    }

    @Test
    public void testJavaTypeGetHandlers() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        Object valueHandler = javaType.getValueHandler();
        assertNull(valueHandler);

        Object typeHandler = javaType.getTypeHandler();
        assertNull(typeHandler);
    }

    @Test
    public void testJavaTypeWithHandlers() throws Exception {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);

        JavaType withValueHandlerType = javaType.withValueHandler(new Object() {});
        Object valueHandler = withValueHandlerType.getValueHandler();
        assertNotNull(valueHandler);

        JavaType withTypeHandlerType = javaType.withTypeHandler(new Object() {});
        Object typeHandler = withTypeHandlerType.getTypeHandler();
        assertNotNull(typeHandler);
    }

    @Test
    public void testJavaTypeWithStaticTyping() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(stringClass);

        JavaType withStaticTypingType = javaType.withStaticTyping();
        boolean useStaticType = withStaticTypingType.useStaticType();
        assertTrue(useStaticType);
    }

    @Test
    public void testJavaTypeIsContainerType() throws Exception {
        Class<List<String>> listClass = new TypeReference<List<String>>(){}.getType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);

        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testJavaTypeIsMapLikeType() throws Exception {
        Class<Map<String, Integer>> mapClass = new TypeReference<Map<String, Integer>>(){}.getType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(mapClass);

        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() throws Exception {
        Class<List<String>> listClass = new TypeReference<List<String>>(){}.getType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);

        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testJavaTypeEquals() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(stringClass);

        assertTrue(javaType1.equals(javaType2));
        assertTrue(javaType2.equals(javaType1));
    }

    @Test
    public void testJavaTypeHashCode() throws Exception {
        Class<String> stringClass = String.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(stringClass);

        int hashCode1 = javaType1.hashCode();
        int hashCode2 = javaType2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}