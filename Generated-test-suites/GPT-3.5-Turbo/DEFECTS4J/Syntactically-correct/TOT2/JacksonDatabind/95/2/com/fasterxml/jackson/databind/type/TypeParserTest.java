import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_NullInput_ThrowsException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        try {
            typeParser.parse(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'null' (remaining: 'null'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_EmptyInput_ThrowsException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidInput_ThrowsException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        try {
            typeParser.parse("invalidInput");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'invalidInput' (remaining: 'invalidInput'): Can not locate class 'invalidInput', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParseType_NoTokensLeft_ThrowsException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_EndOfInput() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("<>");
        try {
            typeParser.parseTypes(tokens);
            assertEquals(0, tokens.countTokens());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testFindClass_ValidClassName_ReturnsClass() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        Class<?> clazz = typeParser.findClass("java.lang.String", new MyTokenizer(""));
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testFindClass_InvalidClassName_ThrowsException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        try {
            typeParser.findClass("InvalidClass", new MyTokenizer(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Can not locate class 'InvalidClass', problem: null", e.getMessage());
        }
    }

    @Test
    public void testProblemMessage_ConcatenatesStrings() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        IllegalArgumentException exception = typeParser._problem(tokens, "Test message");
        assertEquals("Failed to parse type '' (remaining: ''): Test message", exception.getMessage());
    }
}