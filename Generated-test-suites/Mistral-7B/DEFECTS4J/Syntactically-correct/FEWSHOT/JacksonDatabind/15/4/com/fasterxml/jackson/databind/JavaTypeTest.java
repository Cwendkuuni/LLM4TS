package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isAbstract());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaTypeInt.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isConcrete());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertTrue(javaTypeInt.isConcrete());
    }

    @Test
    public void testIsThrowable() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isThrowable());

        JavaType javaTypeException = TypeFactory.defaultInstance().constructType(Exception.class);
        assertTrue(javaTypeException.isThrowable());
    }

    @Test
    public void testIsArrayType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isArrayType());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaTypeInt.isArrayType());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isEnumType());

        JavaType javaTypeEnum = TypeFactory.defaultInstance().constructType(Enum.class);
        assertTrue(javaTypeEnum.isEnumType());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isInterface());

        JavaType javaTypeInterface = TypeFactory.defaultInstance().constructType(ITestInterface.class);
        assertTrue(javaTypeInterface.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isPrimitive());

        JavaType javaTypeBoolean = TypeFactory.defaultInstance().constructType(boolean.class);
        assertTrue(javaTypeBoolean.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isFinal());

        JavaType javaTypeFinalClass = TypeFactory.defaultInstance().constructType(FinalClass.class);
        assertTrue(javaTypeFinalClass.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isContainerType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaTypeInt.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(javaType.isMapLikeType());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaTypeInt.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.useStaticType());

        JavaType javaTypeEnum = TypeFactory.defaultInstance().constructType(Enum.class);
        assertTrue(javaTypeEnum.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.hasGenericTypes());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaTypeInt.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertNull(javaTypeMap.getKeyType());

        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
        assertEquals(String.class, javaTypeMapStringInt.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class, int.class);
        assertNull(javaTypeList.getContentType());

        JavaType javaTypeListString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        assertEquals(String.class, javaTypeListString.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(0, javaTypeMap.containedTypeCount());

        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
        assertEquals(2, javaTypeMapStringInt.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
        assertEquals(String.class, javaTypeMapStringInt.containedType(0));
        assertEquals(int.class, javaTypeMapStringInt.containedType(1));

        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        assertNull(javaTypeListInt.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
        assertEquals("java.lang.String", javaTypeMapStringInt.containedTypeName(0));
        assertEquals("int", javaTypeMapStringInt.containedTypeName(1));

        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        assertNull(javaTypeListInt.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertNull(javaTypeMap.getParameterSource());

        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String[].class, int[].class);
        assertEquals(Object[].class, javaTypeMapStringInt.getParameterSource());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        StringBuilder sb = new StringBuilder();
        javaTypeListInt.getGenericSignature(sb);
        assertEquals("<int>", sb.toString());

        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
        StringBuilder sb2 = new StringBuilder();
        javaTypeMapStringInt.getGenericSignature(sb2);
        assertEquals("<java.lang.String, int>", sb2.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        StringBuilder sb = new StringBuilder();
        javaTypeListInt.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());

        JavaType javaTypeMapStringInt = TypeFactory.defaultInstance().constructType(Map.class, String.class, int.class);
        StringBuilder sb2 = new StringBuilder();
        javaTypeMapStringInt.getErasedSignature(sb2);
        assertEquals("java.util.Map", sb2.toString());
    }
}

class FinalClass {
    // A final class just for testing
}

interface ITestInterface {
    // An interface just for testing
}