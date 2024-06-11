package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypeParserTest {

    private TypeParser typeParser;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        typeFactory = mock(TypeFactory.class);
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse() {
        try {
            JavaType javaType = typeParser.parse("");
            assertNotNull(javaType);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testParseType() {
        try {
            TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
            JavaType javaType = typeParser.parseType(tokens);
            assertNotNull(javaType);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testParseTypes() {
        try {
            TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
            List<JavaType> javaTypes = typeParser.parseTypes(tokens);
            assertNotNull(javaTypes);
            assertEquals(Collections.emptyList(), javaTypes);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        Class<?> result = typeParser.findClass("", tokens);
        assertNotNull(result);
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        IllegalArgumentException exception = typeParser._problem(tokens, "");
        assertNotNull(exception);
    }
}