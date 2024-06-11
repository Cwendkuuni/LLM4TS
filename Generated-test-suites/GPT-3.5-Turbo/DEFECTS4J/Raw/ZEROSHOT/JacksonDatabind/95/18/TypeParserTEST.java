package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        String canonical = "java.util.List<java.lang.String>";
        JavaType expected = factory.constructParametricType(List.class, String.class);
        JavaType result = parser.parse(canonical);

        assertEquals(expected, result);
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        String canonical = "java.util.List";
        Class<?> expected = List.class;
        JavaType result = parser.parseType(new TypeParser.MyTokenizer(canonical));

        assertEquals(expected, result.getRawClass());
    }

    @Test
    public void testParseTypes_singleType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        String canonical = "java.lang.String";
        JavaType expected = factory.constructType(String.class);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer(canonical);
        tokens.nextToken();

        JavaType result = parser.parseTypes(tokens).get(0);

        assertEquals(expected, result);
    }

    @Test
    public void testParseTypes_multipleTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        String canonical = "java.lang.Integer, java.lang.String, java.lang.Boolean";
        JavaType expected1 = factory.constructType(Integer.class);
        JavaType expected2 = factory.constructType(String.class);
        JavaType expected3 = factory.constructType(Boolean.class);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer(canonical);
        tokens.nextToken();

        JavaType result1 = parser.parseTypes(tokens).get(0);
        JavaType result2 = parser.parseTypes(tokens).get(1);
        JavaType result3 = parser.parseTypes(tokens).get(2);

        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
    }

    @Test
    public void testFindClass_foundClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        String className = "java.lang.String";

        Class<?> expected = String.class;
        Class<?> result = parser.findClass(className, new TypeParser.MyTokenizer(""));

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_classNotFound() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        String className = "com.example.Foo";

        parser.findClass(className, new TypeParser.MyTokenizer(""));
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        String msg = "Example problem message";

        try {
            parser._problem(tokens, msg);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            String expected = String.format("Failed to parse type '%s' (remaining: '%s'): %s", tokens.getAllInput(), tokens.getRemainingInput(), msg);
            String result = e.getMessage();

            assertEquals(expected, result);
        }
    }
}