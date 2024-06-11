import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_ValidInput_ReturnsJavaType() {
        // Arrange
        String input = "java.util.List<T>";
        
        // Act
        JavaType result = typeParser.parse(input);
        
        // Assert
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidInput_ThrowsIllegalArgumentException() {
        // Arrange
        String input = "java.util.List";
        
        // Act
        typeParser.parse(input);
        
        // Assert
        // Expecting IllegalArgumentException
    }
    
    @Test
    public void testParseType_ValidInput_ReturnsJavaType() {
        // Arrange
        String input = "java.util.List<T>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(input);
        
        // Act
        JavaType result = typeParser.parseType(tokenizer);
        
        // Assert
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_InvalidInput_ThrowsIllegalArgumentException() {
        // Arrange
        String input = "";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(input);
        
        // Act
        typeParser.parseType(tokenizer);
        
        // Assert
        // Expecting IllegalArgumentException
    }
    
    @Test
    public void testParseTypes_ValidInput_ReturnsListOfJavaType() {
        // Arrange
        String input = "java.util.List<T>, java.util.Map<K, V>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(input);
        
        // Act
        List<JavaType> result = typeParser.parseTypes(tokenizer);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("java.util.List", result.get(0).getRawClass().getName());
        assertEquals("java.util.Map", result.get(1).getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_InvalidInput_ThrowsIllegalArgumentException() {
        // Arrange
        String input = "java.util.List<T, java.util.Map<K, V>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(input);
        
        // Act
        typeParser.parseTypes(tokenizer);
        
        // Assert
        // Expecting IllegalArgumentException
    }
    
    @Test
    public void testFindClass_ValidClassName_ReturnsClassObject() {
        // Arrange
        String className = "java.util.List";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("");
        
        // Act
        Class<?> result = typeParser.findClass(className, tokenizer);
        
        // Assert
        assertNotNull(result);
        assertEquals("java.util.List", result.getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_InvalidClassName_ThrowsIllegalArgumentException() {
        // Arrange
        String className = "java.util.NonExistingClass";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("");
        
        // Act
        typeParser.findClass(className, tokenizer);
        
        // Assert
        // Expecting IllegalArgumentException
    }
    
    @Test
    public void testProblem_ValidInput_ReturnsIllegalArgumentException() {
        // Arrange
        String input = "java.util.List<T>";
        String msg = "Unexpected end-of-string";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("");
        
        // Act
        IllegalArgumentException result = typeParser._problem(tokenizer, msg);
        
        // Assert
        assertNotNull(result);
        assertEquals("Failed to parse type 'java.util.List<T>' (remaining: ''): Unexpected end-of-string", result.getMessage());
    }
    
    @Test
    public void testMyTokenizer_HasMoreTokens_ReturnsTrue() {
        // Arrange
        String str = "java.util.List<T>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(str);
        
        // Act & Assert
        assertTrue(tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizer_NextToken_ReturnsNextToken() {
        // Arrange
        String str = "java.util.List<T>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(str);
        
        // Act
        String result = tokenizer.nextToken();
        
        // Assert
        assertEquals("java.util.List", result);
    }
    
    @Test
    public void testMyTokenizer_PushBack_RemembersToken() {
        // Arrange
        String str = "java.util.List<T>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(str);
        
        // Act
        tokenizer.pushBack("T>");
        String result = tokenizer.nextToken();
        
        // Assert
        assertEquals("T>", result);
    }
    
    @Test
    public void testMyTokenizer_GetAllInput_ReturnsOriginalInput() {
        // Arrange
        String str = "java.util.List<T>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(str);
        
        // Act
        String result = tokenizer.getAllInput();
        
        // Assert
        assertEquals(str, result);
    }
    
    @Test
    public void testMyTokenizer_GetRemainingInput_ReturnsRemainingInput() {
        // Arrange
        String str = "java.util.List<T>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(str);
        tokenizer.nextToken();
        
        // Act
        String result = tokenizer.getRemainingInput();
        
        // Assert
        assertEquals("<T>", result);
    }
}