package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory typeFactory = new TypeFactoryBuilder().build();
    private TypeParser typeParser = new TypeParser(typeFactory);

    @Test
    public void testParse_simpleTypes() {
        JavaType type = typeParser.parse("int");
        assertEquals(Integer.TYPE, type.getRawClass());

        type = typeParser.parse("java.lang.String");
        assertEquals(String.class, type.getRawClass());

        type = typeParser.parse("java.util.List<java.lang.Integer>");
        List<JavaType> expectedTypes = Arrays.asList(Integer.TYPE);
        assertEquals(expectedTypes, type.getTypeParameters());
        assertEquals(List.class, type.getRawClass());
    }

    @Test
    public void testParse_typesWithGenerics() {
        JavaType type = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        List<JavaType> expectedTypes = Arrays.asList(String.class, List.class, Integer.TYPE);
        assertEquals(expectedTypes, type.getGenericTypes());
        assertEquals(Map.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidType() {
        typeParser.parse("invalidType");
        fail("Should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_missingClosingBracket() {
        typeParser.parse("java.util.List<java.lang.Integer>");
        typeParser.parse("<");
        fail("Should have thrown an IllegalArgumentException");
    }

    @Test
    public void testParse_emptyListType() {
        JavaType type = typeParser.parse("java.util.List<>()");
        assertSame(List.class, type.getRawClass());
        assertFalse(type.hasTypeParameters());
    }

    @Test
    public void testParse_nullInput() {
        try {
            typeParser.parse(null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '<null>', expected non-null string", e.getMessage());
        }
    }

    @Test
    public void testParse_typesWithArray() {
        JavaType type = typeParser.parse("java.lang.String[][]");
        JavaType arrayType = type.getRawClass();
        assertEquals(String[].class, arrayType);
        JavaType innerArrayType = arrayType.getComponentType();
        assertEquals(String[].class, innerArrayType);
        JavaType leafType = innerArrayType.getComponentType();
        assertEquals(String.class, leafType);
    }

    @Test
    public void testParse_typesWithCustomClass() {
        JavaType customType = typeParser.parse("com.custom.MyCustomType");
        assertEquals(CustomType.class, customType.getRawClass());
    }

    @Test
    public void testParse_typesWithWildcard() {
        JavaType type = typeParser.parse("java.util.List<?>");
        assertEquals(List.class, type.getRawClass());
        assertTrue(type.isGeneric());
        assertTrue(type.isRaw());
        assertNull(type.getRawClass());
        assertFalse(type.hasTypeParameters());
        assertEquals(1, type.getNumberOfTypeVariables());
    }

    @Test
    public void testParse_typesWithWildcardAndGenerics() {
        JavaType type = typeParser.parse("java.util.List<java.util.List<?>>");
        assertEquals(List.class, type.getRawClass());
        assertTrue(type.isGeneric());
        assertTrue(type.isRaw());
        assertNull(type.getRawClass());
        List<JavaType> typeParameters = type.getTypeParameters();
        assertEquals(1, typeParameters.size());
        assertEquals(List.class, typeParameters.get(0).getRawClass());
        assertTrue(typeParameters.get(0).isGeneric());
        assertTrue(typeParameters.get(0).isRaw());
        assertNull(typeParameters.get(0).getRawClass());
        JavaType innerListType = typeParameters.get(0).getComponentType();
        assertEquals(List.class, innerListType.getRawClass());
        assertTrue(innerListType.isGeneric());
        assertTrue(innerListType.isRaw());
        assertNull(innerListType.getRawClass());
        JavaType leafType = innerListType.getComponentType();
        assertNull(leafType);
    }

    @Test
    public void testParse_typesWithWildcardAndGenerics_withTypeVariable() {
        JavaType type = typeParser.parse("java.util.List<MyType<T>>");
        assertEquals(List.class, type.getRawClass());
        assertTrue(type.isGeneric());
        assertTrue(type.isRaw());
        assertNull(type.getRawClass());
        List<JavaType> typeParameters = type.getGenericTypes();
        List<JavaType> expectedTypes = Arrays.asList(TypeVariableReference.construct("MyType", 0));
        assertEquals(expectedTypes, typeParameters);
        JavaType innerListType = typeParameters.get(0).getComponentType();
        TypeVariableReference innerTypeVariable = (TypeVariableReference) innerListType;
        assertEquals("MyType", innerTypeVariable.getName());
        assertEquals(1, innerTypeVariable.getGenericArguments().length);
        assertEquals("T", innerTypeVariable.getGenericArguments()[0].getName());
    }
}