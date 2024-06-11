
package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_WithValidCanonical_ReturnsJavaType() {
        String canonical = "java.util.List";
        JavaType javaType = typeParser.parse(canonical);

        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithInvalidCanonical_ThrowsIllegalArgumentException() {
        String canonical = "invalid.Type";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithUnexpectedTokensAfterCompleteType_ThrowsIllegalArgumentException() {
        String canonical = "java.util.List extraTokens";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithUnexpectedEndOfString_ThrowsIllegalArgumentException() {
        String canonical = "";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType_WithValidGenericType_ReturnsJavaType() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = typeParser.parseType(tokenizer);

        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testParseTypes_WithValidCommaSeparatedListOfTypes_ReturnsListOfJavaTypes() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(
                "java.util.Map<java.lang.String,java.util.List<java.lang.Integer>>," +
                "java.util.List<java.lang.Long>"
        );
        List<JavaType> javaTypes = typeParser.parseTypes(tokenizer);

        assertNotNull(javaTypes);
        assertEquals(2, javaTypes.size());

        JavaType mapType = javaTypes.get(0);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(List.class, mapType.getContentType().getRawClass());
        assertEquals(Integer.class, mapType.getContentType().getContentType().getRawClass());

        JavaType listType = javaTypes.get(1);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Long.class, listType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_WithUnexpectedEndOfString_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(
                "java.util.List<java.lang.String>"
        );
        typeParser.parseTypes(tokenizer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_WithUnexpectedToken_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(
                "java.util.List<java.lang.String invalidToken"
        );
        typeParser.parseTypes(tokenizer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_WithInvalidClassName_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(
                "invalid.Type"
        );
        typeParser.findClass("invalid.Type", tokenizer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_WithInvalidClassFinder_ThrowsIllegalArgumentException() {
        TypeFactory typeFactory = null;
        TypeParser typeParser = new TypeParser(typeFactory);

        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(
                "java.util.List"
        );
        typeParser.findClass("java.util.List", tokenizer);
    }
}

