package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaTypeTest {

    private static final TypeFactory TYPE_FACTORY = new TypeFactory();

    @Test
    public void testIsAbstract() {
        JavaType javaType = TYPE_FACTORY.constructType(int[].class);
        assertTrue(Modifier.isAbstract(javaType.getRawClass().getModifiers()));
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TYPE_FACTORY.constructType(int[].class);
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TYPE_FACTORY.constructType(Enum.class);
        assertTrue(javaType.isEnumType());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = TYPE_FACTORY.constructType(Iterable.class);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TYPE_FACTORY.constructType(int.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TYPE_FACTORY.constructType(String[].class);
        assertTrue(Modifier.isFinal(javaType.getRawClass().getModifiers()));
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TYPE_FACTORY.constructType(ArrayList.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TYPE_FACTORY.constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TYPE_FACTORY.constructType(Map.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TYPE_FACTORY.constructType(String.class);
        assertFalse(javaType.useStaticType());

        javaType = TYPE_FACTORY.constructType(Byte.TYPE);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testHasRawClass() {
        JavaType javaType = TYPE_FACTORY.constructType(int[].class);
        assertTrue(javaType.hasRawClass(int[].class));
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TYPE_FACTORY.constructType(Map.class);
        assertTrue(javaType.hasGenericTypes());
    }

    @Test
    public void testContainsType() {
        JavaType javaType = TYPE_FACTORY.constructType(Map.class);
        JavaType keyType = TYPE_FACTORY.constructType(String.class);
        JavaType valueType = TYPE_FACTORY.constructType(Integer.class);

        javaType.narrowBy(Map.class);
        assertEquals(javaType, javaType.containedType(0));
        assertEquals(keyType, javaType.containedType(0).containedType(0));
        assertEquals(valueType, javaType.containedType(0).containedType(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = TYPE_FACTORY.constructType(Map.class);
        assertNull(javaType.getParameterSource());

        javaType = TYPE_FACTORY.constructType(Map.class, String.class, Integer.class);
        assertNotNull(javaType.getParameterSource());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TYPE_FACTORY.constructType(Map.class, String.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        assertEquals("Map<java.lang.String, java.lang.Integer>", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TYPE_FACTORY.constructType(Map.class, String.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        assertEquals("java.util.Map", sb.toString());
    }
}