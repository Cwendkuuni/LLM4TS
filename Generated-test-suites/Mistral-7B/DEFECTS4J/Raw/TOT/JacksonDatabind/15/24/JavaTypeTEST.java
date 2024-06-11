package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeDescriptor;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    // Test case for constructor
    @Test
    public void testJavaTypeConstructor() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
        assertNotNull(stringType);
        assertEquals(String.class, stringType.getRawClass());
        assertFalse(stringType.isConcrete());
        assertTrue(stringType.isStringType());
    }

    // Test case for isContainerType
    @Test
    public void testIsContainerType() {
        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        assertTrue(intArrayType.isContainerType());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(stringType.isContainerType());
    }

    // Test case for isMapLikeType and isCollectionLikeType
    @Test
    public void testIsMapLikeTypeAndIsCollectionLikeType() {
        JavaType javaUtilListType = TypeFactory.defaultInstance().constructType(java.util.List.class);
        JavaType javaUtilMapType = TypeFactory.defaultInstance().constructType(java.util.Map.class);

        assertFalse(javaUtilListType.isMapLikeType());
        assertFalse(javaUtilListType.isCollectionLikeType());

        assertFalse(javaUtilMapType.isMapLikeType(false));
        assertTrue(javaUtilMapType.isMapLikeType(true));
        assertFalse(javaUtilMapType.isCollectionLikeType());
    }

    // Test case for narrowBy and widenBy
    @Test
    public void testNarrowByAndWidenBy() {
        Class<String> stringClass = String.class;
        Class<Integer> intClass = Integer.class;

        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);
        JavaType intType = TypeFactory.defaultInstance().constructType(intClass);

        JavaType stringNarrowed = stringType.narrowBy(intClass);
        JavaType intWidened = intType.widenBy(stringClass);

        assertTrue(intType.isAssignableFrom(stringNarrowed));
        assertTrue(stringType.isAssignableFrom(intWidened));
    }

    // Test case for useStaticType
    @Test
    public void testUseStaticType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        assertFalse(stringType.useStaticType());
        assertTrue(intType.useStaticType());

        JavaType stringStaticType = stringType.withStaticTyping();
        assertTrue(stringStaticType.useStaticType());
    }
}