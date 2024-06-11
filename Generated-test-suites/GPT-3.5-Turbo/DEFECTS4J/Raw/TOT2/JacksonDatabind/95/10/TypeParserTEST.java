import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.lang.String");
        assertNotNull(result);

        // Edge case: Empty string
        try {
            parser.parse("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseType() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<Integer>");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);

        // Edge case: Unexpected end-of-string
        tokens = new MyTokenizer("");
        try {
            parser.parseType(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseTypes() {
        // Typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("<java.lang.String, Integer>");
        List<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(2, result.size());

        // Edge case: Unexpected token
        tokens = new MyTokenizer("<java.lang.String Integer>");
        try {
            parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
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

        // Edge case: Class not found
        try {
            parser.findClass("non.existent.Class", new MyTokenizer("non.existent.Class"));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}