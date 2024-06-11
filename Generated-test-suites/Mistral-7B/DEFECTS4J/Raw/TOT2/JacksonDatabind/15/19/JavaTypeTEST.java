package com.example; // replace with your package name

import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    private TypeFactory typeFactory;

    @Before
    public void setup() {
        typeFactory = new TypeFactory();
    }

    // Test case for the constructor
    @Test
    public void testJavaTypeConstruction() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test case for the withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        JavaType javaType = typeFactory.constructType(String.class);
        JavaType newJavaType = javaType.withTypeHandler(new TestTypeHandler());

        assertNotSame(javaType, newJavaType);
        assertEquals(newTestTypeHandlerClass(), newJavaType.getRawClass());
    }

    // Test case for the isContainerType method
    @Test
    public void testIsContainerType() {
        JavaType javaType = typeFactory.constructType(ArrayList.class);
        assertTrue(javaType.isContainerType());

        JavaType javaTypeInt = typeFactory.constructType(Integer.class);
        assertFalse(javaTypeInt.isContainerType());
    }

    // Test case for the narrowBy method
    @Test
    public void testNarrowBy() {
        JavaType javaTypeList = typeFactory.constructType(List.class);
        JavaType javaTypeArrayList = javaTypeList.narrowBy(ArrayList.class);

        assertNotSame(javaTypeList, javaTypeArrayList);
        assertEquals(ArrayList.class, javaTypeArrayList.getRawClass());
    }

    // Test case for the widenBy method
    @Test
    public void testWidenBy() {
        JavaType javaTypeArrayList = typeFactory.constructType(ArrayList.class);
        JavaType javaTypeList = javaTypeArrayList.widenBy(List.class);

        assertNotSame(javaTypeArrayList, javaTypeList);
        assertEquals(List.class, javaTypeList.getRawClass());
    }

    // Test case for the isInterface method
    @Test
    public void testIsInterface() {
        JavaType javaTypeList = typeFactory.constructType(List.class);
        assertTrue(javaTypeList.isInterface());

        JavaType javaTypeString = typeFactory.constructType(String.class);
        assertFalse(javaTypeString.isInterface());
    }

    // Test case for the isFinal method
    @Test
    public void testIsFinal() {
        JavaType javaTypeList = typeFactory.constructType(List.class);
        assertFalse(javaTypeList.isFinal());

        JavaType javaTypeString = typeFactory.constructType(String.class);
        assertTrue(String.class.isFinal());
        assertTrue(javaTypeString.isFinal());
    }

    // Test case for the isMapLikeType method
    @Test
    public void testIsMapLikeType() {
        JavaType javaTypeMap = typeFactory.constructType(Map.class);
        assertTrue(javaTypeMap.isMapLikeType());

        JavaType javaTypeList = typeFactory.constructType(List.class);
        assertFalse(javaTypeList.isMapLikeType());
    }

    // Test case for the isCollectionLikeType method
    @Test
    public void testIsCollectionLikeType() {
        JavaType javaTypeList = typeFactory.constructType(List.class);
        assertTrue(javaTypeList.isCollectionLikeType());

        JavaType javaTypeMap = typeFactory.constructType(Map.class);
        assertFalse(javaTypeMap.isCollectionLikeType());
    }

    // Test case for the useStaticType method
    @Test
    public void testUseStaticType() {
        JavaType javaTypeList = typeFactory.constructType(List.class);
        assertFalse(javaTypeList.useStaticType());

        JavaType javaTypeIntStatic = typeFactory.constructType(Integer.class).withStaticTyping();
        assertTrue(javaTypeIntStatic.useStaticType());
    }

    // Helper classes and mocks
    private static class TestTypeHandler {}

    private static class TestTypeHandlerClass extends TestTypeHandler {}

}