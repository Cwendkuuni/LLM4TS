package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType intType = TypeFactory.intType();
        assertTrue(intType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType intType = TypeFactory.intType();
        assertFalse(intType.isConcrete());
    }

    @Test
    public void testIsEnumType() {
        JavaType enumType = TypeFactory.valueType(Enum.class);
        assertTrue(enumType.isEnumType());
    }

    @Test
    public void testIsInterface() {
        JavaType interfaceType = TypeFactory.interfaceType(Object.class);
        assertTrue(interfaceType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType intType = TypeFactory.intType();
        assertTrue(intType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType intType = TypeFactory.intType();
        assertFalse(intType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        assertTrue(listType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        assertTrue(listType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = TypeFactory.mapType(Map.class, String.class, Integer.class);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType intType = TypeFactory.intType();
        assertFalse(intType.useStaticType());

        JavaType stringType = TypeFactory.valueType(String.class);
        assertTrue(stringType.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        assertTrue(listType.hasGenericTypes());

        JavaType intType = TypeFactory.intType();
        assertFalse(intType.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapType = TypeFactory.mapType(Map.class, String.class, Integer.class);
        assertNull(mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        assertNull(listType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType mapType = TypeFactory.mapType(Map.class, String.class, Integer.class);
        assertEquals(2, mapType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType mapType = TypeFactory.mapType(Map.class, String.class, Integer.class);
        JavaType stringType = mapType.containedType(0);
        assertEquals(String.class, stringType.getRawClass());

        JavaType integerType = mapType.containedType(1);
        assertEquals(Integer.class, integerType.getRawClass());
    }

    @Test
    public void testContainedTypeName() {
        JavaType mapType = TypeFactory.mapType(Map.class, String.class, Integer.class);
        assertEquals("java.lang.String", mapType.containedTypeName(0));
        assertEquals("java.lang.Integer", mapType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        assertSame(List.class, listType.getParameterSource());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType mapType = TypeFactory.mapType(Map.class, String.class, Integer.class);
        JavaType stringType = mapType.containedTypeOrUnknown(0);
        assertEquals(String.class, stringType.getRawClass());

        JavaType unknownType = mapType.containedTypeOrUnknown(2);
        assertEquals(Object.class, unknownType.getRawClass());
    }

    @Test
    public void testGetValueHandler() {
        JavaType stringType = TypeFactory.valueType(String.class);
        String handler = (String) stringType.getValueHandler();
        assertNull(handler);
    }

    @Test
    public void testgetTypeHandler() {
        JavaType stringType = TypeFactory.valueType(String.class);
        String handler = (String) stringType.getTypeHandler();
        assertNull(handler);
    }

    @Test
    public void testToString() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        assertEquals("java.util.List<java.lang.String>", listType.toString());
    }

    @Test
    public void testEquals() {
        JavaType listType1 = TypeFactory.collectionType(List.class, String.class);
        JavaType listType2 = TypeFactory.collectionType(List.class, String.class);
        assertTrue(listType1.equals(listType2));

        JavaType listType3 = TypeFactory.collectionType(ArrayList.class, String.class);
        assertFalse(listType1.equals(listType3));
    }

    @Test
    public void testHashCode() {
        JavaType listType = TypeFactory.collectionType(List.class, String.class);
        JavaType listType2 = TypeFactory.collectionType(List.class, String.class);
        assertEquals(listType.hashCode(), listType2.hashCode());

        JavaType listType3 = TypeFactory.collectionType(ArrayList.class, String.class);
        assertNotEquals(listType.hashCode(), listType3.hashCode());
    }
}