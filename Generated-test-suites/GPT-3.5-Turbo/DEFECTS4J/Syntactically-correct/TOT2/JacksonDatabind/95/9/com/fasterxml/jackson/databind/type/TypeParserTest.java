import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Test typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(result);
        
        // Test edge case
        try {
            parser.parse("invalidString");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Test error scenario
        try {
            parser.parse("java.util.List<java.lang.String> extraTokens");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testParseType() {
        // Test typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
        
        // Test edge case
        try {
            parser.parseType(new MyTokenizer(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Test error scenario
        try {
            parser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testParseTypes() {
        // Test typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Test edge case
        tokens = new MyTokenizer("java.lang.String");
        result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Test error scenario
        tokens = new MyTokenizer("java.lang.String, java.lang.Integer extraTokens");
        try {
            parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testFindClass() {
        // Test typical use case
        TypeParser parser = new TypeParser(new TypeFactory());
        Class<?> result = parser.findClass("java.lang.String", new MyTokenizer("java.lang.String"));
        assertNotNull(result);
        
        // Test edge case
        try {
            parser.findClass("invalidClass", new MyTokenizer(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Test error scenario
        try {
            parser.findClass("invalidClass", new MyTokenizer("java.lang.String"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

}