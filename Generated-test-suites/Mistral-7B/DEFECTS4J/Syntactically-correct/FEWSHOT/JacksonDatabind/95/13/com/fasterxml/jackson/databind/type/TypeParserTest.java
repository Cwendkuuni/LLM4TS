package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParseNull() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse(null);
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseEmpty() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = parser.parse("");
        assertNull(type);
    }

    @Test
    public void testParseSimpleClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = parser.parse("String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType listType = parser.parse("List<String>");
        JavaType elementType = listType.getContentType();
        assertEquals(String.class, elementType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
    }

    @Test
    public void testParseComplexClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType complexType = parser.parse("com.example.MyClass<java.util.List<java.lang.String>, com.example.MyOtherClass>");
        assertEquals(MyClass.class, complexType.getRawClass());
        List<JavaType> typeArgs = complexType.getTypeArgs();
        assertEquals(2, typeArgs.size());
        JavaType listType = typeArgs.get(0);
        JavaType elementType = listType.getContentType();
        assertEquals(String.class, elementType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        JavaType otherType = typeArgs.get(1);
        assertEquals(MyOtherClass.class, otherType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        parser.parse("InvalidClass");
    }

    @Test
    public void testParseInvalidTokenization() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        String invalidInput = "List<String> foo";
        try {
            parser.parse(invalidInput);
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            String expectedMessage = String.format("Failed to parse type '%s' (remaining: '%s'): %s",
                    parser.getTokenizer().getAllInput(), parser.getTokenizer().getRemainingInput(),
                    "Unexpected token 'foo', expected ',' or '>')");
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testParseMultipleTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType listType = parser.parse("List<String>, Map<Integer, List<String>>");
        List<JavaType> types = listType.getTypeArgs();
        assertEquals(2, types.size());
        JavaType stringListType = types.get(0);
        JavaType stringType = stringListType.getContentType();
        assertEquals(String.class, stringType.getRawClass());
        JavaType intMapType = types.get(1);
        List<JavaType> intMapArgs = intMapType.getTypeArgs();
        assertEquals(2, intMapArgs.size());
        JavaType intType = intMapArgs.get(0);
        JavaType listType2 = intMapArgs.get(1);
        JavaType intListType = listType2.getContentType();
        assertEquals(String.class, intListType.getRawClass());
        assertEquals(List.class, listType2.getRawClass());
        JavaType intType2 = intType.getContentType();
        assertEquals(Integer.TYPE, intType2.getRawClass());
    }

    @Test
    public void testParseNestedGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType complexType = parser.parse("List<Map<Integer, List<List<String> > > >");
        List<JavaType> outerList = ((List<?>) complexType.getContentType()).getTypeArgs();
        JavaType innerListType = outerList.get(0);
        JavaType mapType = innerListType.getContentType();
        List<JavaType> mapArgs = mapType.getTypeArgs();
        assertEquals(2, mapArgs.size());
        JavaType intType = mapArgs.get(0);
        JavaType innerList = mapArgs.get(1);
        List<JavaType> innerListArgs = innerList.getTypeArgs();
        assertEquals(1, innerListArgs.size());
        JavaType listType = innerListArgs.get(0);
        List<JavaType> listArgs = listType.getTypeArgs();
        assertEquals(1, listArgs.size());
        JavaType stringType = listArgs.get(0);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(Integer.TYPE, intType.getRawClass());
        assertEquals(List.class, innerList.getRawClass());
        assertEquals(List.class, outerList.get(0).getRawClass());
    }
}