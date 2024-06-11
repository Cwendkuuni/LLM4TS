import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Test case 1: Valid input
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType type = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(type);
        
        // Test case 2: Empty input
        try {
            type = parser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 3: Invalid input
        try {
            type = parser.parse("InvalidType");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'InvalidType' (remaining: 'InvalidType'): Can not locate class 'InvalidType', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        // Test case 1: Valid input
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        
        // Test case 2: Empty input
        try {
            tokens = parser.new MyTokenizer("");
            type = parser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 3: Invalid input
        try {
            tokens = parser.new MyTokenizer("InvalidType");
            type = parser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'InvalidType' (remaining: 'InvalidType'): Can not locate class 'InvalidType', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes() {
        // Test case 1: Valid input
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String, java.util.Map>");
        List<JavaType> types = parser.parseTypes(tokens);
        assertEquals(2, types.size());
        
        // Test case 2: Empty input
        try {
            tokens = parser.new MyTokenizer("");
            types = parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 3: Invalid input
        try {
            tokens = parser.new MyTokenizer("InvalidType");
            types = parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'InvalidType' (remaining: 'InvalidType'): Can not locate class 'InvalidType', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        // Test case 1: Valid input
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        Class<?> cls = parser.findClass("java.util.List", tokens);
        assertEquals(List.class, cls);
        
        // Test case 2: Empty input
        try {
            tokens = parser.new MyTokenizer("");
            cls = parser.findClass("", tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 3: Invalid input
        try {
            tokens = parser.new MyTokenizer("InvalidType");
            cls = parser.findClass("InvalidType", tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'InvalidType' (remaining: 'InvalidType'): Can not locate class 'InvalidType', problem: null", e.getMessage());
        }
    }
}