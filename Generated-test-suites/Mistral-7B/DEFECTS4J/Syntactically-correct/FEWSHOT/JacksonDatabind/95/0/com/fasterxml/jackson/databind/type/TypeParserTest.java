package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private static final TypeFactory TYPE_FACTORY = new TypeFactory() {
        @Override
        public JavaType findTypeFromString(String className, Class<?> baseType, TypeBindings bindings) {
            return null;
        }

        @Override
        public JavaType _fromClass(Class<?> rawClass, Class<?> baseType, TypeBindings bindings) {
            return null;
        }
    };

    @Test
    public void testParseEmpty() {
        try {
            new TypeParser(TYPE_FACTORY).parse("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ')', problem: Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseSimpleClass() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        assertEquals(String.class, parser.parse("String").getRawClass());
    }

    @Test
    public void testParseClassWithGenerics1() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        List<JavaType> types = new ArrayList<>();
        types.add(parser.parse("List<String>"));
        TypeBindings bindings = TypeBindings.create(String.class, types);
        assertEquals(List.class, parser.parse("List<String>").getRawClass());
        assertEquals(bindings, parser.parse("List<String>").getTypeBindings());
    }

    @Test
    public void testParseClassWithGenerics2() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        List<JavaType> types = new ArrayList<>();
        types.add(parser.parse("List<String>"));
        types.add(parser.parse("Integer>"));
        TypeBindings bindings = TypeBindings.create(String.class, types);
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(String.class);
        expectedTypes.add(Integer.class);
        TypeBindings expectedBindings = TypeBindings.create(String.class, expectedTypes);
        List<JavaType> resultTypes = parser.parse("List<String>Integer>").getTypeArguments();
        assertEquals(expectedTypes, resultTypes);
        assertEquals(expectedBindings, parser.parse("List<String>Integer>").getTypeBindings());
    }

    @Test
    public void testParseClassWithGenerics3() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        List<JavaType> types = new ArrayList<>();
        types.add(parser.parse("List<String>"));
        types.add(parser.parse("Integer>"));
        types.add(parser.parse("String>"));
        TypeBindings bindings = TypeBindings.create(String.class, types);
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(String.class);
        expectedTypes.add(Integer.class);
        expectedTypes.add(String.class);
        TypeBindings expectedBindings = TypeBindings.create(String.class, expectedTypes);
        List<JavaType> resultTypes = parser.parse("List<String>Integer>String>").getTypeArguments();
        assertEquals(expectedTypes, resultTypes);
        assertEquals(expectedBindings, parser.parse("List<String>Integer>String>").getTypeBindings());
    }

    @Test
    public void testParseClassWithGenerics4() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        List<JavaType> types = new ArrayList<>();
        types.add(parser.parse("List<String>"));
        types.add(parser.parse("Integer>"));
        types.add(parser.parse("String>"));
        types.add(parser.parse("String>"));
        TypeBindings bindings = TypeBindings.create(String.class, types);
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(String.class);
        expectedTypes.add(Integer.class);
        expectedTypes.add(String.class);
        expectedTypes.add(String.class);
        TypeBindings expectedBindings = TypeBindings.create(String.class, expectedTypes);
        List<JavaType> resultTypes = parser.parse("List<String>Integer>String>String>").getTypeArguments();
        assertEquals(expectedTypes, resultTypes);
        assertEquals(expectedBindings, parser.parse("List<String>Integer>String>String>").getTypeBindings());
    }

    @Test
    public void testParseClassWithGenericArray() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType arrayType = parser.parse("String[]");
        assertEquals(String[].class, arrayType.getRawClass());
    }

    @Test
    public void testParseClassWithGenericArrayAndGenerics() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        List<JavaType> types = new ArrayList<>();
        types.add(parser.parse("List<String>"));
        types.add(parser.parse("Integer>"));
        types.add(parser.parse("String[]"));
        TypeBindings bindings = TypeBindings.create(String.class, types);
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(String.class);
        expectedTypes.add(Integer.class);
        expectedTypes.add(String[].class);
        TypeBindings expectedBindings = TypeBindings.create(String.class, expectedTypes);
        List<JavaType> resultTypes = parser.parse("List<String>Integer>String[]").getTypeArguments();
        assertEquals(expectedTypes, resultTypes);
        assertEquals(expectedBindings, parser.parse("List<String>Integer>String[]").getTypeBindings());
    }

    @Test
    public void testParseClassWithInvalidToken1() {
        try {
            new TypeParser(TYPE_FACTORY).parse("List<String>Integer>ABC>");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>Integer>ABC>' (remaining: ')', problem: Unexpected token 'A'");
        }
    }

    @Test
    public void testParseClassWithInvalidToken2() {
        try {
            new TypeParser(TYPE_FACTORY).parse("List<String>Integer>String>ABC");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>Integer>String>ABC' (remaining: ')', problem: Unexpected token 'A'");
        }
    }

    @Test
    public void testParseClassWithInvalidToken3() {
        try {
            new TypeParser(TYPE_FACTORY).parse("List<String>Integer>String>ABC>");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>Integer>String>ABC>' (remaining: ', problem: Unexpected token 'A'");
        }
    }

    @Test
    public void testParseClassWithInvalidToken4() {
        try {
            new TypeParser(TYPE_FACTORY).parse("List<String>Integer>String>ABC");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>Integer>String>ABC' (remaining: ', problem: Unexpected token 'A'");
        }
    }
}