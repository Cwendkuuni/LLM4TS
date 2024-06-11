import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse_withValidInput_shouldReturnJavaType() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(canonical);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals("java.lang.String", result.getContentType().getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidInput_shouldThrowException() {
        String canonical = "java.util.List<java.lang.String";
        typeParser.parse(canonical);
    }
    
    @Test
    public void testWithFactory_withSameFactory_shouldReturnSameInstance() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        TypeParser newParser = typeParser.withFactory(typeFactory);
        
        assertSame(typeParser, newParser);
    }
    
    @Test
    public void testWithFactory_withDifferentFactory_shouldReturnNewInstance() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        TypeParser newParser = typeParser.withFactory(typeFactory);
        
        assertNotSame(typeParser, newParser);
        assertSame(typeFactory, newParser._factory);
    }
    
    @Test
    public void testParseType_withValidBaseType_shouldReturnJavaType() {
        String canonical = "java.util.List";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        
        JavaType result = typeParser.parseType(tokens);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withEmptyBaseType_shouldThrowException() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        
        typeParser.parseType(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withInvalidBaseType_shouldThrowException() {
        String canonical = "javaUtilList";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        
        typeParser.parseType(tokens);
    }
    
    @Test
    public void testParseType_withGenerics_shouldReturnJavaType() {
        String canonical = "java.util.List<java.lang.String>";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        
        JavaType result = typeParser.parseType(tokens);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals("java.lang.String", result.getContentType().getRawClass().getName());
    }
    
    @Test
    public void testParseType_withMissingEndToken_shouldThrowException() {
        String canonical = "java.util.List<java.lang.String";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes_withValidTypes_shouldReturnListOfJavaTypes() {
        String canonical = "java.util.List<java.lang.String,java.lang.Integer>";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        
        List<JavaType> result = typeParser.parseTypes(tokens);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("java.lang.String", result.get(0).getRawClass().getName());
        assertEquals("java.lang.Integer", result.get(1).getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withNoTypes_shouldThrowException() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withInvalidToken_shouldThrowException() {
        String canonical = "java.util.List<java.lang.String,java.lang.Integer>";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        tokens.nextToken(); // Skip first token
        
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withMissingEndToken_shouldThrowException() {
        String canonical = "java.util.List<java.lang.String,java.lang.Integer";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(canonical);
        
        typeParser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass_withExistingClass_shouldReturnClass() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(className);
        
        Class<?> result = typeParser.findClass(className, tokens);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_withNonExistingClass_shouldThrowException() {
        String className = "javaUtilList";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(className);
        
        typeParser.findClass(className, tokens);
    }
    
    @Test
    public void testProblem_withTokens_shouldReturnIllegalArgumentException() {
        String input = "java.util.List<java.lang.String>";
        String remaining = "";
        String msg = "Unexpected end-of-string";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer(input);
        
        IllegalArgumentException result = typeParser._problem(tokens, msg);
        
        assertNotNull(result);
        assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Unexpected end-of-string", result.getMessage());
    }
    
    @Test
    public void testMyTokenizer_hasMoreTokens_withEmptyInput_shouldReturnFalse() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        
        boolean result = tokens.hasMoreTokens();
        
        assertFalse(result);
    }
    
    @Test
    public void testMyTokenizer_hasMoreTokens_withoutPushBack_shouldReturnTrue() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        
        boolean result = tokens.hasMoreTokens();
        
        assertTrue(result);
    }
    
    @Test
    public void testMyTokenizer_hasMoreTokens_withPushBack_shouldReturnTrue() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        tokens.nextToken(); // Consume one token
        tokens.pushBack("java.util");
        
        boolean result = tokens.hasMoreTokens();
        
        assertTrue(result);
    }
    
    @Test
    public void testMyTokenizer_nextToken_withoutPushBack_shouldReturnToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        
        String result = tokens.nextToken();
        
        assertEquals("java.util.List", result);
    }
    
    @Test
    public void testMyTokenizer_nextToken_withPushBack_shouldReturnPushedToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        tokens.nextToken(); // Consume one token
        tokens.pushBack("java.util");
        
        String result = tokens.nextToken();
        
        assertEquals("java.util", result);
    }
    
    @Test
    public void testMyTokenizer_pushBack_withToken_shouldPushToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        tokens.pushBack("java.util");
        
        String result = tokens.nextToken();
        
        assertEquals("java.util", result);
    }
    
    @Test
    public void testMyTokenizer_getAllInput_withInput_shouldReturnInput() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        
        String result = tokens.getAllInput();
        
        assertEquals("java.util.List", result);
    }
    
    @Test
    public void testMyTokenizer_getRemainingInput_withRemainingTokens_shouldReturnRemainingInput() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        tokens.nextToken(); // Consume one token
        
        String result = tokens.getRemainingInput();
        
        assertEquals(".List", result);
    }
}