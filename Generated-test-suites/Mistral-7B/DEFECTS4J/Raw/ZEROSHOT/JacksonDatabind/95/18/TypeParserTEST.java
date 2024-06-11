package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeParser;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTypeFactory;

/**
 * Test class for {@link TypeParser}
 */
public class TypeParserTest {

    @Test
    public void testParseSimpleType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType type = parser.parse("int");
        assertEquals(JavaType.getType(int.class), type);
    }

    @Test
    public void testParseClassType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType type = parser.parse("java.lang.String");
        assertEquals(JavaType.getType(String.class), type);
    }

    @Test
    public void testParseArrayType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType type = parser.parse("int []");
        assertEquals(JavaType.constructCollectionType(JavaType.getType(int[].class)), type);
    }

    @Test
    public void testParseListType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType listType = parser.parse("java.util.List<int>");
        JavaType collectionType = JavaType.constructCollectionType(JavaType.getType(List.class));
        JavaType listElementType = JavaType.getType(int.class);
        JavaType expectedType = collectionType.makeGeneric(listElementType);
        assertEquals(expectedType, listType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        parser.parse("InvalidType"); // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingClosingBracket() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        parser.parse("java.util.List<int> foo"); // should throw IllegalArgumentException
    }

    @Test
    public void testParseComplexGenericType() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType expectedType = TypeBindings.constructType(String[].class, Map.class, List.class, int.class);
        JavaType type = parser.parse("String[][] extends Map<?, List<Integer>>");
        assertEquals(expectedType, type);
    }

    @Test
    public void testParseTypesWithComma() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType type = parser.parse("int, long");
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(JavaType.getType(int.class));
        expectedTypes.add(JavaType.getType(long.class));
        JavaType collectionType = JavaType.constructArrayType(JavaType.TYPE_INT_ARRAY);
        assertEquals(collectionType.makeGeneric(expectedTypes), type);
    }

    @Test
    public void testParseTypesWithBrackets() {
        TypeFactory typeFactory = new DefaultTypeFactory();
        TypeParser parser = new TypeParser(typeFactory);

        JavaType type = parser.parse("int[], long[]");
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(JavaType.constructArrayType(JavaType.getType(int.class)));
        expectedTypes.add(JavaType.constructArrayType(JavaType.getType(long.class)));
        JavaType collectionType = JavaType.constructArrayType(JavaType.TYPE_OBJECT_ARRAY);
        assertEquals(collectionType.makeGeneric(expectedTypes), type);
    }
}