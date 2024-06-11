package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {
    private TypeParser typeParser;

    @Before
    public void setUp() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse() {
        JavaType type = typeParser.parse("java.lang.String");
        assertEquals("java.lang.String", type.toString());

        type = typeParser.parse("com.fasterxml.jackson.databind.ObjectMapper");
        assertEquals("com.fasterxml.jackson.databind.ObjectMapper", type.toString());

        type = typeParser.parse("java.util.List<java.lang.String>");
        assertEquals("java.util.List<java.lang.String>", type.toString());

        type = typeParser.parse("java.util.Map<java.lang.String, com.fasterxml.jackson.databind.JsonNode>");
        assertEquals("java.util.Map<java.lang.String, com.fasterxml.jackson.databind.JsonNode>", type.toString());

        type = typeParser.parse("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>");
        assertEquals("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>", type.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidTypeName() {
        typeParser.parse("java.lang.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        typeParser.parse("java.lang.StringInteger");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidCharacters() {
        typeParser.parse("java.lang.&");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidGenericParameters() {
        typeParser.parse("java.util.List<java.lang.");
    }

    @Test
    public void testParseWithNoGenericType() {
        JavaType type = typeParser.parse("java.util.List");
        assertEquals("java.util.List", type.toString());
        assertNull(type.getRawClass());
        assertTrue(type.isContainerType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isReferenceType());
    }

    @Test
    public void testParseWithEmptyGenericParameters() {
        JavaType type = typeParser.parse("java.util.List<>");
        assertEquals("java.util.List<>", type.toString());
        assertNull(type.getRawClass());
        assertTrue(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
        assertFalse(type.isReferenceType());
        assertEquals(0, type.containedTypeCount());
    }

    @Test
    public void testParseWithWildcardGenericParameters() {
        JavaType type = typeParser.parse("java.util.List<?>");
        assertEquals("java.util.List<?>", type.toString());
        assertNull(type.getRawClass());
        assertTrue(type.isContainerType());
        assertTrue(type.isCollectionLikeType());
        assertFalse(type.isReferenceType());
        assertEquals(1, type.containedTypeCount());
        assertTrue(type.getContentType().isWildcardType());
    }

    @Test
    public void testParseWithMultipleWildcardGenericParameters() {
        JavaType type = typeParser.parse("java.util.Map<?, ?>");
        assertEquals("java.util.Map<?, ?>", type.toString());
        assertNull(type.getRawClass());
        assertTrue(type.isContainerType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isReferenceType());
        assertEquals(2, type.containedTypeCount());
        assertTrue(type.getKeyType().isWildcardType());
        assertTrue(type.getValueType().isWildcardType());
    }

    @Test
    public void testParseWithNestedGenericParameters() {
        JavaType type = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", type.toString());
        assertNull(type.getRawClass());
        assertTrue(type.isContainerType());
        assertFalse(type.isCollectionLikeType());
        assertFalse(type.isReferenceType());
        assertEquals(2, type.containedTypeCount());
        assertTrue(type.getKeyType().isTypeOrSubTypeOf(String.class));
        assertTrue(type.getValueType().isCollectionLikeType());
        assertEquals(1, type.getValueType().containedTypeCount());
        assertTrue(type.getValueType().getContentType().isTypeOrSubTypeOf(Integer.class));
    }
}