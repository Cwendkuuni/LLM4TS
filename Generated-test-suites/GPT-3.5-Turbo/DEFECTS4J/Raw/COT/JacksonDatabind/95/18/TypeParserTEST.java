
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        
        // Test empty input
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test valid input
        try {
            JavaType javaType = typeParser.parse("com.fasterxml.jackson.databind.ObjectMapper");
            assertTrue(javaType.isJavaLangObject());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
        
        // Test unexpected tokens
        try {
            typeParser.parse("com.fasterxml.jackson.databind.ObjectMapper, unexpected tokens");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.fasterxml.jackson.databind.ObjectMapper, unexpected tokens' (remaining: ', unexpected tokens'): Unexpected tokens after complete type", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        
        // Test unexpected end-of-string
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
            typeParser.parseType(tokenizer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test valid input
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.ObjectMapper");
            JavaType javaType = typeParser.parseType(tokenizer);
            assertTrue(javaType.isJavaLangObject());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
        
        // Test generics parsing
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.ObjectMapper, unexpected tokens");
            JavaType javaType = typeParser.parseType(tokenizer);
            assertTrue(javaType.isJavaLangObject());
            assertEquals("unexpected tokens", tokenizer.getRemainingInput());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        
        // Test unexpected end-of-string
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
            typeParser.parseTypes(tokenizer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test valid input
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.ObjectMapper");
            JavaType javaType = typeParser.parseType(tokenizer);
            assertTrue(javaType.isJavaLangObject());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
        
        // Test generics parsing
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.ObjectMapper, unexpected tokens");
            JavaType javaType = typeParser.parseType(tokenizer);
            assertTrue(javaType.isJavaLangObject());
            assertEquals("unexpected tokens", tokenizer.getRemainingInput());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test
    public void testFindClass() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        
        // Test class not found
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        try {
            typeParser.findClass("com.fasterxml.jackson.databind.ObjectMapper", tokenizer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Can not locate class 'com.fasterxml.jackson.databind.ObjectMapper', problem: null", e.getMessage());
        }
        
        // Test valid class
        tokenizer = new TypeParser.MyTokenizer("");
        try {
            Class<?> clazz = typeParser.findClass("java.lang.Object", tokenizer);
            assertEquals(Object.class, clazz);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

}
