import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setup() {
        typeParser = new TypeParser();
    }

    @Test
    public void testParse_WithValidCanonicalString_ShouldReturnCorrectJavaType() {
        String canonical = "java.util.HashMap<java.lang.String, java.util.List<java.lang.Integer>>";
        JavaType expectedType = typeParser.parse(canonical);
        
        assertEquals(HashMap.class, expectedType.getRawClass());
        assertEquals(String.class, expectedType.getKeyType().getRawClass());
        assertEquals(ArrayList.class, expectedType.getContentType().getRawClass());
        assertEquals(Integer.class, expectedType.getContentType().getContentType().getRawClass());
    }
    
    @Test
    public void testParse_WithEmptyString_ShouldThrowIllegalArgumentException() {
        String canonical = "";
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_WithInvalidCanonicalString_ShouldThrowIllegalArgumentException() {
        String canonical = "java.util.HashMap<java.lang.String, java.util.List<java.lang.Integer>";
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.HashMap<java.lang.String, java.util.List<java.lang.Integer>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_WithUnexpectedTokensAfterCompleteType_ShouldThrowIllegalArgumentException() {
        String canonical = "java.util.HashMap<java.lang.String, java.util.List<java.lang.Integer>> extra";
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.HashMap<java.lang.String, java.util.List<java.lang.Integer>>' (remaining: 'extra'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_WithValidCanonicalString_ShouldReturnCorrectList() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String,java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.Boolean>");

        List<JavaType> expectedTypes = typeParser.parseTypes(tokens);

        assertEquals(3, expectedTypes.size());
        assertEquals(String.class, expectedTypes.get(0).getRawClass());
        assertEquals(ArrayList.class, expectedTypes.get(1).getRawClass());
        assertEquals(Integer.class, expectedTypes.get(1).getContentType().getRawClass());
        assertEquals(HashMap.class, expectedTypes.get(2).getRawClass());
        assertEquals(String.class, expectedTypes.get(2).getContentType().getRawClass());
        assertEquals(Boolean.class, expectedTypes.get(2).getContentType().getKeyType().getRawClass());
    }

    @Test
    public void testParseTypes_WithEmptyString_ShouldThrowIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");

        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_WithUnexpectedToken_ShouldThrowIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String,java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.Boolean> extra");

        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String,java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.Boolean>' (remaining: 'extra'): Unexpected token 'extra', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testFindClass_WithValidClassName_ShouldReturnClassObject() {
        Class<?> expectedClass = typeParser.findClass("java.lang.String");
        
        assertEquals(String.class, expectedClass);
    }

    @Test
    public void testFindClass_WithInvalidClassName_ShouldThrowIllegalArgumentException() {
        try {
            typeParser.findClass("InvalidClassName");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'InvalidClassName', problem: null", e.getMessage());
        }
    }
}