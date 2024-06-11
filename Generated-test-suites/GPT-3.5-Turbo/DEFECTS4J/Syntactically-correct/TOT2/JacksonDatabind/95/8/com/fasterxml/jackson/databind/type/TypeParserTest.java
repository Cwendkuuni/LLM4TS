import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Test typical case
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType result = typeParser.parse("java.util.List<String>");
        assertNotNull(result);
        
        // Test edge case with empty string
        try {
            result = typeParser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Test error scenario with invalid input
        try {
            result = typeParser.parse("java.util.List<String> <Extra>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List");
        
        // Test typical case
        JavaType result = typeParser.parseType(tokens);
        assertNotNull(result);
        
        // Test edge case with unexpected end-of-string
        tokens = new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("<java.util.List<String>>");
        
        // Test typical case
        List<JavaType> result = typeParser.parseTypes(tokens);
        assertNotNull(result);
        
        // Test edge case with unexpected end-of-string
        tokens = new MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Test error scenario with unexpected token
        tokens = new MyTokenizer("<java.util.List<String><Extra>");
        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testFindClass() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List");
        
        // Test typical case
        Class<?> result = typeParser.findClass("java.util.List", tokens);
        assertNotNull(result);
        
        // Test error scenario with non-existing class
        tokens = new MyTokenizer("");
        try {
            typeParser.findClass("NonExistingClass", tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}