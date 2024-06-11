package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

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

        JavaType javaTypeInterface = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaTypeInterface.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isPrimitive());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertTrue(javaTypeInt.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isFinal());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isFinal());

        String s = "final";
        JavaType javaTypeFinal = TypeFactory.defaultInstance().constructType(s.getClass());
        assertTrue(javaTypeFinal.isFinal());
    }

    @Test
    public void testHasRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.hasRawClass(String[].class));

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertTrue(javaTypeInt.hasRawClass(int.class));

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaTypeString.hasRawClass(String.class));
    }

    @Test
    public void testIsArrayType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isArrayType());

        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaTypeInt.isArrayType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(javaTypeMap.isMapLikeType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isMapLikeType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaTypeList.isCollectionLikeType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isCollectionLikeType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(javaTypeMap.isContainerType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isContainerType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.useStaticType());

        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaTypeList.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaTypeList.hasGenericTypes());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.hasGenericTypes());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals(1, javaTypeList.containedTypeCount());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(2, javaTypeMap.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        JavaType containedType = javaTypeList.containedType(0);
        assertEquals(List.class, containedType.getRawClass());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = javaTypeMap.containedType(0);
        assertEquals(Map.Entry.class, keyType.getRawClass());
        JavaType valueType = javaTypeMap.containedType(1);
        assertEquals(Object.class, valueType.getRawClass());
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals("java.lang.Object", javaTypeList.containedTypeName(0));

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals("Ljava/util/Map$Entry;", javaTypeMap.containedTypeName(0));
        assertEquals("Ljava/lang/Object;", javaTypeMap.containedTypeName(1));
    }

    @Test
    public void testGetKeyType() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(Map.Entry.class, javaTypeMap.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(Object.class, javaTypeMap.getContentType());
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals(List.class, javaTypeList.getParameterSource());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(Map.class, javaTypeMap.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals("java.util.List", javaTypeList.getErasedSignature());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals("java.util.Map", javaTypeMap.getErasedSignature());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals("java.util.List<Ljava/lang/Object;>", javaTypeList.getGenericSignature());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals("java.util.Map<Ljava/lang/Object;Ljava/lang/Object;>", javaTypeMap.getGenericSignature());
    }

    @Test
    public void testToString() {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals("java.util.List<java.lang.Object>", javaTypeList.toString());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals("java.util.Map<java.lang.Object,java.lang.Object>", javaTypeMap.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaTypeList1 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaTypeList2 = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaTypeList1.equals(javaTypeList2));

        JavaType javaTypeList1Different = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertFalse(javaTypeList1.equals(javaTypeList1Different));

        JavaType javaTypeMap1 = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType javaTypeMap2 = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(javaTypeMap1.equals(javaTypeMap2));

        JavaType javaTypeMap1DifferentKey = TypeFactory.defaultInstance().constructType(Map.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        javaTypeMap1DifferentKey = typeFactory.constructType(new TypeReference<Map<String, Integer>>() {});
        assertFalse(javaTypeMap1.equals(javaTypeMap1DifferentKey));

        JavaType javaTypeMap1DifferentValue = TypeFactory.defaultInstance().constructType(Map.class);
        TypeFactory typeFactoryDifferentValue = TypeFactory.defaultInstance();
        javaTypeMap1DifferentValue = typeFactoryDifferentValue.constructType(new TypeReference<Map<Integer, String>>() {});
        assertFalse(javaTypeMap1.equals(javaTypeMap1DifferentValue));
    }
}