package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(String name, Class<?> clazz, TypeBindings binding) {
                return (JavaType) (clazz == String.class ? new SimpleType(String.class) : new SimpleType(clazz));
            }
        };
    }

    @Test
    public void testParseEmptyString() {
        TypeParser parser = new TypeParser(_factory);
        JavaType result = parser.parse(" ");
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testParsePrimitive() {
        TypeParser parser = new TypeParser(_factory);
        JavaType result = parser.parse("int");
        assertEquals(Integer.TYPE, result.getRawClass());
    }

    @Test
    public void testParsePrimitiveArray() {
        TypeParser parser = new TypeParser(_factory);
        JavaType result = parser.parse("int[]");
        assertEquals(int[].class, result.getRawClass());
    }

    @Test
    public void testParseClass() {
        TypeParser parser = new TypeParser(_factory);
        JavaType result = parser.parse("java.lang.String");
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics() {
        TypeParser parser = new TypeParser(_factory);
        JavaType result = parser.parse("java.util.List<java.lang.String>");
        assertEquals(List.class, result.getRawClass());
        assertTrue(result.hasTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        assertEquals(String.class, result.getTypeParameters().get(0));
    }

    @Test
    public void testParseClassWithGenericsAndArray() {
        TypeParser parser = new TypeParser(_factory);
        JavaType result = parser.parse("java.util.List<java.lang.String>[]");
        assertEquals(List[].class, result.getRawClass());
        assertTrue(result.hasTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        assertEquals(String.class, result.getTypeParameters().get(0));
    }

    @Test
    public void testParseClassWithInvalidGenerics() {
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<java.lang.String>X"));
    }

    @Test
    public void testParseClassWithInvalidTokens() {
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<java.lang.String>Xxy"));
    }

    @Test
    public void testParseClassWithInvalidName() {
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<java.lang.String>Xx"));
    }

    @Test
    public void testParseClassWithNonExistentClass() {
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("com.non.existent.Class<java.lang.String>"));
    }

    @Test
    public void testParseValidTokenStream() {
        TypeParser parser = new TypeParser(_factory);
        List<JavaType> result = parser.parseTypes("int , java.util.List<java.lang.String> , java.util.List<java.lang.String>[] , java.util.List<java.lang.String[]>");
        List<JavaType> expected = Arrays.asList(
                new SimpleType(Integer.TYPE),
                new SimpleType(List.class).mappedType(String.class),
                new SimpleType(List[].class).mappedType(new SimpleType(String[].class)),
                new SimpleType(List.class).mappedType(new SimpleType(String[].class))
        );
        assertArrayEquals(expected.toArray(new JavaType[0]), result.toArray(new JavaType[0]));
    }

    @Test
    public void testParseInvalidTokenStream() {
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parseTypes("int , java.util.List<java.lang.String> , java.util.List<java.lang.String>[] , java.util.List<java.lang.String[]>X"));
    }

    @Test
    public void testParseInvalidTokenStreamWithPushback() {
        TypeParser parser = new TypeParser(_factory);
        List<JavaType> result = parser.parseTypes("int , java.util.List<java.lang.String> , java.util.List<java.lang.String>[] , java.util.List<java.lang.String[]>");
        parser.pushBack("X");
        assertThrows(IllegalArgumentException.class, () -> result.add(parser.nextToken()));
    }
}