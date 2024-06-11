
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }
    
    @After
    public void tearDown() {
        typeParser = null;
    }
    
    @Test
    public void testParse_withValidCanonicalType_shouldParseSuccessfully() {
        String canonical = "java.util.ArrayList<String>";
        JavaType result = typeParser.parse(canonical);
        assertNotNull(result);
        assertEquals(ArrayList.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidCanonicalType_shouldThrowIllegalArgumentException() {
        String canonical = "InvalidCanonicalType";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedTokensAfterCompleteType_shouldThrowIllegalArgumentException() {
        String canonical = "java.util.ArrayList<String> unexpectedTokens";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedEndOfString_shouldThrowIllegalArgumentException() {
        String canonical = "";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withUnexpectedEndOfString_shouldThrowIllegalArgumentException() {
        String canonical = "";
        MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        typeParser.parseType(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withUnexpectedToken_shouldThrowIllegalArgumentException() {
        String canonical = "invalidToken";
        MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        typeParser.parseType(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withUnexpectedEndOfString_shouldThrowIllegalArgumentException() {
        String canonical = "";
        MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withUnexpectedToken_shouldThrowIllegalArgumentException() {
        String canonical = "invalidToken";
        MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        typeParser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass_withExistingClassName_shouldReturnClass() {
        String className = "java.util.ArrayList";
        Class<?> result = typeParser.findClass(className, null);
        assertNotNull(result);
        assertEquals(ArrayList.class, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_withNonExistingClassName_shouldThrowIllegalArgumentException() {
        String className = "NonExistingClass";
        typeParser.findClass(className, null);
    }
    
    @Test
    public void testProblem_withValidTokenizer_shouldReturnIllegalArgumentException() {
        MyTokenizer tokens = typeParser.new MyTokenizer("input");
        IllegalArgumentException exception = typeParser._problem(tokens, "test message");
        assertNotNull(exception);
        assertEquals("Failed to parse type 'input' (remaining: 'input'): test message", exception.getMessage());
    }
    
    @Test
    public void testMyTokenizer_hasMoreTokens_withEmptyInput_shouldReturnFalse() {
        MyTokenizer tokens = typeParser.new MyTokenizer("");
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizer_hasMoreTokens_withNonEmptyInput_shouldReturnTrue() {
        MyTokenizer tokens = typeParser.new MyTokenizer("<>");
        assertTrue(tokens.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizer_nextToken_withEmptyInput_shouldReturnEmptyToken() {
        MyTokenizer tokens = typeParser.new MyTokenizer("");
        String token = tokens.nextToken();
        assertEquals("", token);
    }
    
    @Test
    public void testMyTokenizer_nextToken_withNonEmptyInput_shouldReturnNextToken() {
        MyTokenizer tokens = typeParser.new MyTokenizer("token1 token2");
        String token = tokens.nextToken();
        assertEquals("token1", token);
    }
    
    @Test
    public void testMyTokenizer_pushBack_withToken_shouldPushBackToken() {
        MyTokenizer tokens = typeParser.new MyTokenizer("token1 token2");
        String token1 = tokens.nextToken();
        tokens.pushBack(token1);
        String token2 = tokens.nextToken();
        assertEquals("token1", token2);
    }
    
    @Test
    public void testMyTokenizer_getAllInput_withInput_shouldReturnAllInput() {
        MyTokenizer tokens = typeParser.new MyTokenizer("input");
        String result = tokens.getAllInput();
        assertEquals("input", result);
    }
    
    @Test
    public void testMyTokenizer_getRemainingInput_withInput_shouldReturnRemainingInput() {
        MyTokenizer tokens = typeParser.new MyTokenizer("input");
        String result = tokens.getRemainingInput();
        assertEquals("input", result);
    }

}

