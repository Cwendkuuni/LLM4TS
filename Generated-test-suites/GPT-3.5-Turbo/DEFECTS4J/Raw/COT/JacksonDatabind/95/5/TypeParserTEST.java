import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {
    
    private TypeParser typeParser;
    private TypeFactory typeFactory;
    
    @Before
    public void setUp() {
        typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse_WithValidInput_ReturnsJavaType() {
        String canonical = "java.util.List<java.lang.String>";
        try {
            JavaType result = typeParser.parse(canonical);
            assertTrue(result instanceof JavaType);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_WithEmptyString_ThrowsIllegalArgumentException() {
        String canonical = "";
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseType_WithValidInput_ReturnsJavaType() {
        String input = "java.util.List";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(input);
        
        try {
            JavaType result = typeParser.parseType(tokenizer);
            assertTrue(result instanceof JavaType);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseType_WithEmptyString_ThrowsIllegalArgumentException() {
        String input = "";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(input);
        
        try {
            typeParser.parseType(tokenizer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes_WithValidInput_ReturnsListOfJavaTypes() {
        String input = "java.lang.String,java.util.Date";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(input);
        
        try {
            JavaType[] expectedTypes = {typeFactory.constructType(String.class), typeFactory.constructType(Date.class)};
            List<JavaType> result = typeParser.parseTypes(tokenizer);
            assertArrayEquals(expectedTypes, result.toArray());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes_WithEmptyString_ThrowsIllegalArgumentException() {
        String input = "";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(input);
        
        try {
            typeParser.parseTypes(tokenizer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass_WithValidClassName_ReturnsClassObject() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(className);
        
        try {
            Class<?> result = typeParser.findClass(className, tokenizer);
            assertEquals(List.class, result);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testFindClass_WithInvalidClassName_ThrowsIllegalArgumentException() {
        String className = "InvalidClass";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(className);
        
        try {
            typeParser.findClass(className, tokenizer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'InvalidClass', problem: null", e.getMessage());
        }
    }
}