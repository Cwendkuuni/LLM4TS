package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class TypeParserTest {

    private TypeFactory typeFactory;
    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse_withCompleteType_returnsJavaType() {
        String canonical = "java.util.List<com.example.Person>";
        JavaType result = typeParser.parse(canonical);
        Assert.assertNotNull(result);
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(Person.class, result.getBindings().getTypeParameters().get(0).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withIncompleteType_throwsIllegalArgumentException() {
        String canonical = "java.util.List";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedTokens_throwsIllegalArgumentException() {
        String canonical = "java.util.List<com.example.Person> extra tokens";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType_withParameterTypes_returnsJavaTypeWithBindings() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.Person, java.lang.String");
        List<JavaType> result = typeParser.parseTypes(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(Person.class, result.get(0).getRawClass());
        Assert.assertEquals(String.class, result.get(1).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withUnexpectedToken_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.Person unexpectedToken");
        typeParser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withUnexpectedEndOfString_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.Person");
        typeParser.parseTypes(tokens);
    }

    @Test
    public void testFindClass_withValidClassName_returnsClass() {
        String className = "com.example.Person";
        Class<?> result = typeParser.findClass(className, new TypeParser.MyTokenizer(""));
        Assert.assertNotNull(result);
        Assert.assertEquals(Person.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_withInvalidClassName_throwsIllegalArgumentException() {
        String className = "com.example.Invalid";
        typeParser.findClass(className, new TypeParser.MyTokenizer(""));
    }

    @Test
    public void testProblem_returnsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.Person");
        IllegalArgumentException result = typeParser._problem(tokens, "Invalid type");
        Assert.assertNotNull(result);
        Assert.assertEquals("Failed to parse type 'com.example.Person' (remaining: ''): Invalid type", result.getMessage());
    }
}