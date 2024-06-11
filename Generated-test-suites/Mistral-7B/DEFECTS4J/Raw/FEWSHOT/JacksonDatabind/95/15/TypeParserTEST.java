package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @Test
    public void testTypeParser_EmptyString() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        try {
            JavaType type = parser.parse("");
            assertNull(type);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testTypeParser_ValidClassName() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        try {
            JavaType type = parser.parse("String");
            assertNull(type.getGenerics());
            assertEquals("java.lang.String", type.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTypeParser_ValidTypeWithGenerics() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        try {
            JavaType type = parser.parse("List<Integer>");
            List<JavaType> generics = type.getGenerics();
            assertEquals(1, generics.size());
            JavaType genericType = generics.get(0);
            assertEquals("java.lang.Integer", genericType.getRawClass().getName());
            assertEquals("java.util.List", type.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTypeParser_ValidTypeWithMultipleGenerics() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        try {
            JavaType type = parser.parse("Map<String, List<Integer>>");
            List<JavaType> generics = type.getGenerics();
            assertEquals(2, generics.size());
            JavaType keyType = generics.get(0);
            JavaType valueType = generics.get(1);
            assertEquals("java.lang.String", keyType.getRawClass().getName());
            assertEquals("java.util.List", valueType.getRawClass().getName());
            assertEquals("java.util.Map", type.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeParser_InvalidTypeSyntax() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        parser.parse("List<Integer>foobar"); // missing '>': expected '>'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeParser_MissingClosingBracket() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        parser.parse("List<Integer>"); // missing '>'
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeParser_InvalidClassname() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        parser.parse("List<Integer>FooBar"); // invalid class name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeParser_InvalidToken() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        parser.parse("List<Integer>#"); // invalid token
    }

    @Test
    public void testTypeParser_NestedGenerics() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(TypeResolverBase resolver, Class<?> rawClass, TypeBindings bindings) {
                return null;
            }
        };
        TypeParser parser = new TypeParser(_factory);
        try {
            JavaType type = parser.parse("Map<String, List<Map<String, Integer>>>");
            List<JavaType> generics = type.getGenerics();
            assertEquals(2, generics.size());
            JavaType keyType = generics.get(0);
            List<JavaType> keyGenerics = keyType.getGenerics();
            assertEquals(1, keyGenerics.size());
            JavaType keyInnerType = keyGenerics.get(0);
            assertEquals("java.lang.String", keyInnerType.getRawClass().getName());
            JavaType valueType = generics.get(1);
            List<JavaType> valueGenerics = valueType.getGenerics();
            assertEquals(1, valueGenerics.size());
            JavaType valueInnerType = valueGenerics.get(0);
            assertEquals("java.lang.Map", type.getRawClass().getName());
            assertEquals("java.util.Map", valueInnerType.getRawClass().getName());
            assertEquals("java.lang.String", keyType.getRawClass().getName());
            assertEquals("java.util.List", valueType.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}