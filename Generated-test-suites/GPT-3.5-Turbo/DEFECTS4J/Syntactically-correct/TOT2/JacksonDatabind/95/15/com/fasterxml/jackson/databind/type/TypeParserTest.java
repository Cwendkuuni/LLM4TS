
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {
    
    @Test
    public void testParse_validInput_returnsJavaType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType result = typeParser.parse("java.util.List<com.example.MyClass>");
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(1, result.containedTypeCount());
        assertEquals("com.example.MyClass", result.getContentType().getRawClass().getName());
    }
    
    @Test
    public void testParse_invalidInput_throwsIllegalArgumentException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        
        try {
            JavaType result = typeParser.parse("invalidInput");
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'invalidInput' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    // Add more test methods for other public methods of TypeParser class
    
}
