import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Test parsing a valid canonical string
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals("java.lang.String", result.containedType(0).getRawClass().getName());
    }

    @Test
    public void testParseType() {
        // Test parsing a simple type without generics
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
        assertEquals("java.lang.String", result.getRawClass().getName());
    }

    @Test
    public void testParseTypes() {
        // Test parsing multiple types with generics
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        List<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("java.lang.String", result.get(0).getRawClass().getName());
        assertEquals("java.lang.Integer", result.get(1).getRawClass().getName());
    }

    @Test
    public void testFindClass() {
        // Test finding a valid class
        TypeParser parser = new TypeParser(new TypeFactory());
        Class<?> result = parser.findClass("java.lang.String", new MyTokenizer(""));
        assertNotNull(result);
        assertEquals("java.lang.String", result.getName());
    }

    @Test
    public void testProblem() {
        // Test creating an IllegalArgumentException with proper message
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("test");
        IllegalArgumentException e = parser._problem(tokens, "Test message");
        assertNotNull(e);
        assertEquals("Failed to parse type 'test' (remaining: ''): Test message", e.getMessage());
    }
}