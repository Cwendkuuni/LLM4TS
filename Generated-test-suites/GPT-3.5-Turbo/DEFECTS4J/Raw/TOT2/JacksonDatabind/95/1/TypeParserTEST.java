import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.lang.String");
        assertNotNull(result);

        // Edge case: Empty input
        try {
            parser.parse("");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error scenario: Unexpected tokens remaining
        try {
            parser.parse("java.lang.String extraTokens");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseType() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);

        // Edge case: Unexpected end-of-string
        tokens = new MyTokenizer("java.lang.String");
        try {
            parser.parseType(tokens);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error scenario: Unexpected token
        tokens = new MyTokenizer("java.lang.String extraToken");
        try {
            parser.parseType(tokens);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseTypes() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(2, result.size());

        // Edge case: Unexpected end-of-string
        tokens = new MyTokenizer("java.lang.String");
        try {
            parser.parseTypes(tokens);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error scenario: Unexpected token
        tokens = new MyTokenizer("java.lang.String extraToken");
        try {
            parser.parseTypes(tokens);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFindClass() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        Class<?> result = parser.findClass("java.lang.String", new MyTokenizer("java.lang.String"));
        assertNotNull(result);

        // Error scenario: Class not found
        try {
            parser.findClass("nonexistent.Class", new MyTokenizer("nonexistent.Class"));
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}