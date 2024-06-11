package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isAbstract());

        JavaType javaTypeInterface = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaTypeInterface.isInterface());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isConcrete());

        JavaType javaTypeInterface = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeInterface.isConcrete());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaTypeArray = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaTypeArray.isContainerType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaTypeArrayList = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        assertTrue(javaTypeArrayList.isCollectionLikeType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaTypeHashMap = TypeFactory.defaultInstance().constructType(java.util.HashMap.class);
        assertTrue(javaTypeHashMap.isMapLikeType());

        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.isMapLikeType());
    }

    @Test
    public void testHasRawClass() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaTypeString.hasRawClass(String.class));

        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(java.util.List.class);
        assertFalse(javaTypeList.hasRawClass(String.class));
        assertTrue(javaTypeList.hasRawClass(java.util.List.class));
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaTypeString.hasGenericTypes());

        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(java.util.List.class);
        assertTrue(javaTypeList.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaTypeHashMap = TypeFactory.defaultInstance().constructType(java.util.HashMap.class);
        assertNull(javaTypeHashMap.getKeyType());

        JavaType javaTypeMapWithStringKey = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, String.class);
        assertEquals(String.class, javaTypeMapWithStringKey.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaTypeArrayList = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        assertNull(javaTypeArrayList.getContentType());

        JavaType javaTypeListWithIntegerContent = TypeFactory.defaultInstance()
                .constructType(List.class, Integer.class);
        assertEquals(Integer.class, javaTypeListWithIntegerContent.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaTypeArrayList = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        assertEquals(1, javaTypeArrayList.containedTypeCount());

        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        assertEquals(2, javaTypeMap.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaTypeMapWithStringKeyAndStringValue = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, String.class);
        assertEquals(String.class, javaTypeMapWithStringKeyAndStringValue.containedType(0));
        assertEquals(String.class, javaTypeMapWithStringKeyAndStringValue.containedType(1));

        JavaType javaTypeListWithIntegerContent = TypeFactory.defaultInstance()
                .constructType(List.class, Integer.class);
        assertEquals(Integer.class, javaTypeListWithIntegerContent.containedType(0));
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaTypeMapWithStringKeyAndStringValue = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, String.class);
        assertEquals(String.class, javaTypeMapWithStringKeyAndStringValue.containedTypeOrUnknown(0));
        assertEquals(String.class, javaTypeMapWithStringKeyAndStringValue.containedTypeOrUnknown(1));

        JavaType javaTypeListWithNullContent = TypeFactory.defaultInstance().constructType(List.class);
        assertNull(javaTypeListWithNullContent.containedTypeOrUnknown(0));
    }
}