package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private static final TypeFactory TYPE_FACTORY = new TypeFactory() {
        @Override
        public JavaType findValueType(Class<?> raw, Map<Class<?>, JavaType> cache) {
            return null;
        }

        @Override
        public JavaType findConstructable(Class<?> cls, Map<Class<?>, JavaType> cache) {
            return null;
        }

        @Override
        public JavaType findRawType(Class<?> clazz) {
            return _factory._fromClass(null, clazz, null);
        }

        @Override
        public JavaType _fromClass(TypeResolverBase parent, Class<?> raw, TypeBindings bindings) {
            return null;
        }
    };

    @Test
    public void testParseEmptyString() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            JavaType result = parser.parse(" ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected end-of-string"));
        }
    }

    @Test
    public void testParseValidType() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.lang.String");
        assertSame(JavaType.class, result.getClass());
        assertSame(String.class, result.getRawClass());
    }

    @Test
    public void testParseValidTypeWithGenerics() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.util.ArrayList<java.lang.Integer>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("Integer"));
    }

    @Test
    public void testParseInvalidType() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            parser.parse("java.lang.String (InvalidType)");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected token '('"));
        }
    }

    @Test
    public void testParseInvalidTypeWithMissingClosingBracket() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            parser.parse("java.lang.String<Integer>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected end-of-string"));
        }
    }

    @Test
    public void testParseInvalidTypeWithMissingClosingAngleBracket() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            parser.parse("java.lang.String<Integer, >");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected ','"));
        }
    }

    @Test
    public void testParseMultipleTypes() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.lang.String java.util.ArrayList<java.lang.Integer>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("Integer"));
    }

    @Test
    public void testParseInvalidTokenAfterType() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            parser.parse("java.lang.String java.util.ArrayList<java.lang.Integer> foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected token 'foo'"));
        }
    }

    @Test
    public void testParseInvalidTokenBeforeType() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            parser.parse("foo java.util.ArrayList<java.lang.Integer>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected token 'foo'"));
        }
    }

    @Test
    public void testParseInvalidTokenInsideType() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        try {
            parser.parse("java.lang.String java.util.ArrayList<java.lang.InvalidType>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not locate class 'InvalidType'"));
        }
    }

    @Test
    public void testParseValidTypeWithBounds() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType boundType = _factory._fromClass(null, Collection.class, null);
        JavaType result = parser.parse("java.util.ArrayList<java.lang.Integer extends Number>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("Integer"));
        assertTrue(result.getTypeParameters().get(0).getBounds().contains(Number.class));
        assertFalse(result.getTypeParameters().get(0).getBounds().isEmpty());
    }

    @Test
    public void testParseValidTypeWithMultipleBounds() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType boundType1 = _factory._fromClass(null, Number.class, null);
        JavaType boundType2 = _factory._fromClass(null, Comparable.class, null);
        JavaType result = parser.parse("java.util.ArrayList<List<Number> extends java.util.List<Comparable<? extends Number>>>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        JavaType listType = result.getTypeParameters().get(0);
        assertSame(List.class, listType.getRawClass());
        assertNotNull(listType.getTypeParameters());
        assertEquals(1, listType.getTypeParameters().size());
        JavaType numberType = listType.getTypeParameters().get(0);
        assertSame(Number.class, numberType.getRawClass());
        assertNotNull(numberType.getTypeParameters());
        assertEquals(0, numberType.getTypeParameters().size());
        assertTrue(listType.getBounds().contains(List.class));
        assertTrue(listType.getBounds().contains(Comparable.class));
        assertFalse(listType.getBounds().isEmpty());
    }

    @Test
    public void testParseInvalidTypeWithInvalidBound() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.util.ArrayList<java.lang.Integer extends java.util.ArrayList<java.lang.String>>");
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseValidTypeWithWildcard() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.util.ArrayList<? extends java.lang.Number>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(0, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("?"));
        assertTrue(result.getTypeParameters().get(0).getBound() instanceof WildcardType);
        assertSame(WildcardType.class, result.getTypeParameters().get(0).getBound().getClass());
    }

    @Test
    public void testParseValidTypeWithUpperBounds() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.util.ArrayList<? extends List<? super java.lang.Number>>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(0, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("?"));
        assertTrue(result.getTypeParameters().get(0).getUpperBounds().contains(List.class));
        java.util.List<java.util.List<? extends java.lang.Number>> listList = Arrays.asList(new ArrayList<List<? extends Number>>(), new ArrayList<List<? extends Number>>());
        assertTrue(result.getTypeParameters().get(0).getUpperBounds().containsAll(listList));
        assertFalse(result.getTypeParameters().get(0).getUpperBounds().isEmpty());
    }

    @Test
    public void testParseValidTypeWithLowerBounds() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.util.ArrayList<? super List<? extends java.lang.Number>>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(0, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("?"));
        assertTrue(result.getTypeParameters().get(0).getLowerBounds().contains(List.class));
        java.util.List<java.util.List<? extends java.lang.Number>> listList = Arrays.asList(new ArrayList<List<? extends Number>>(), new ArrayList<List<? extends Number>>());
        assertTrue(result.getTypeParameters().get(0).getLowerBounds().containsAll(listList));
        assertFalse(result.getTypeParameters().get(0).getLowerBounds().isEmpty());
    }

    @Test
    public void testParseValidTypeWithMultipleWildcards() {
        TypeParser parser = new TypeParser(TYPE_FACTORY);
        JavaType result = parser.parse("java.util.ArrayList<? extends java.util.List<? extends java.lang.Number>>");
        assertSame(ArrayList.class, result.getRawClass());
        assertNotNull(result.getTypeParameters());
        assertEquals(1, result.getTypeParameters().size());
        assertTrue(result.getTypeParameters().get(0).getName().equals("?"));
        JavaType listType = result.getTypeParameters().get(0);
        assertTrue(listType.getName().equals("?"));
        assertTrue(listType.getBound() instanceof WildcardType);
        assertTrue(listType.getBound().getName().equals("?"));
        JavaType innerListType = listType.getBounds().get(0);
        assertTrue(innerListType.getName().equals("?"));
        assertTrue(innerListType.getBound() instanceof WildcardType);
        assertTrue(innerListType.getBound().getName().equals("?"));
        assertTrue(innerListType.getBounds().contains(List.class));
        java.util.List<java.util.List<? extends java.lang.Number>> listList = Arrays.asList(new ArrayList<List<? extends Number>>(), new ArrayList<List<? extends Number>>());
        assertTrue(listType.getBounds().containsAll(listList));
        assertFalse(listType.getBounds().isEmpty());
    }
}