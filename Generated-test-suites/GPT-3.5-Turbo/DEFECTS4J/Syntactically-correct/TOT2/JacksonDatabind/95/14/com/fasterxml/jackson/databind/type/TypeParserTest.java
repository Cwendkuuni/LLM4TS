import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Test typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        JavaType type = parser.parse("java.util.List<String>");
        assertNotNull(type);
        assertEquals("java.util.List",type.getRawClass().getName());

        // Test edge case: empty input
        try {
            parser.parse("");
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test error scenario: unexpected tokens
        try {
            parser.parse("java.util.List<String> extra");
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseType() {
        // Test typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.util.List<String>");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);

        // Test edge case: end of string
        tokens = new MyTokenizer("");
        try {
            parser.parseType(tokens);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test error scenario: unexpected token
        tokens = new MyTokenizer("java.util.List<String");
        try {
            parser.parseType(tokens);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseTypes() {
        // Test typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("<String,Integer>");
        List<JavaType> types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(2, types.size());

        // Test error scenario: unexpected end-of-string
        tokens = new MyTokenizer("<String,Integer");
        try {
            parser.parseTypes(tokens);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test edge case: single type
        tokens = new MyTokenizer("<String>");
        types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(1, types.size());
    }

    @Test
    public void testFindClass() {
        // Test typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        Class<?> clazz = parser.findClass("java.util.List", new MyTokenizer(""));
        assertNotNull(clazz);
        assertEquals("java.util.List", clazz.getName());

        // Test error scenario: class not found
        try {
            parser.findClass("NonExistentClass", new MyTokenizer(""));
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProblem() {
        // Test typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.util.List<String>");
        IllegalArgumentException exception = parser._problem(tokens, "Test message");
        assertNotNull(exception);
        assertEquals("Failed to parse type 'java.util.List<String>' (remaining: ''): Test message", exception.getMessage());
    }
}