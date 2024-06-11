import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_withEmptyString_shouldThrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        
        // Act & Assert
        try {
            parser.parse("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParse_withValidCanonicalString_shouldReturnJavaType() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        
        // Act
        JavaType result = parser.parse("java.util.List<java.lang.String>");
        
        // Assert
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals("java.lang.String", result.getContentType().getRawClass().getName());
    }
    
    @Test
    public void testParse_withInvalidCanonicalString_shouldThrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        
        // Act & Assert
        try {
            parser.parse("java.util.List<java.lang.String");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParse_withUnexpectedTokensAfterCompleteType_shouldThrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        
        // Act & Assert
        try {
            parser.parse("java.lang.Object unexpected");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParseType_withUnexpectedEndOfString_shouldThrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        
        // Act & Assert
        try {
            parser.parseType(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testParseType_withNonGenericType_shouldReturnJavaType() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        
        // Act
        JavaType result = parser.parseType(tokens);
        
        // Assert
        assertNotNull(result);
        assertEquals("java.lang.String", result.getRawClass().getName());
    }
    
    @Test
    public void testParseType_withGenericTypes_shouldReturnJavaType() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        
        // Act
        JavaType result = parser.parseType(tokens);
        
        // Assert
        assertNotNull(result);
        assertEquals("java.util.Map", result.getRawClass().getName());
        assertEquals("java.lang.String", result.getKeyType().getRawClass().getName());
        assertEquals("java.lang.Integer", result.getContentType().getRawClass().getName());
    }
    
    @Test
    public void testParseTypes_withValidTypes_shouldReturnListOfJavaTypes() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer>");
        
        // Act
        List<JavaType> result = parser.parseTypes(tokens);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("java.lang.String", result.get(0).getRawClass().getName());
        assertEquals("java.util.List", result.get(1).getRawClass().getName());
        assertEquals("java.lang.Integer", result.get(1).getContentType().getRawClass().getName());
    }
    
    @Test
    public void testParseTypes_withInvalidTypes_shouldThrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer");
        
        // Act & Assert
        try {
            parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFindClass_withInvalidClassName_shouldThrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("InvalidClass");
        
        // Act & Assert
        try {
            parser.findClass("InvalidClass", tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Can not locate class 'InvalidClass', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass_withRuntimeException_shouldRethrowException() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("RuntimeExceptionClass");
        
        // Act & Assert
        try {
            parser.findClass("RuntimeExceptionClass", tokens);
            fail("Expected RuntimeException to be rethrown");
        } catch (RuntimeException e) {
            // Expected exception
            assertEquals("RuntimeException: Unable to find class", e.getMessage());
        }
    }
    
    @Test
    public void testProblem_shouldCreateIllegalArgumentExceptionWithFormattedMessage() {
        // Arrange
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("input");
        
        // Act
        IllegalArgumentException exception = parser._problem(tokens, "Error message");
        
        // Assert
        assertNotNull(exception);
        assertEquals("Failed to parse type 'input' (remaining: 'input'): Error message", exception.getMessage());
    }

}