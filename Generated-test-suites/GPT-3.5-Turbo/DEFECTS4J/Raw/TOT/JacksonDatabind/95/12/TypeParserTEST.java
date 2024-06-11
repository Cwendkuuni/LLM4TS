import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_SimpleType_Success() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "java.util.List";
        JavaType parsedType = parser.parse(canonical);
        assertEquals(parsedType.getRawClass(), List.class);
    }
    
    @Test
    public void testParse_GenericType_Success() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType parsedType = parser.parse(canonical);
        assertEquals(parsedType.getRawClass(), Map.class);
        assertEquals(parsedType.containedType(0).getRawClass(), String.class);
        assertEquals(parsedType.containedType(1).getRawClass(), Integer.class);
    }
    
    @Test
    public void testParse_ExtraTokensAfterCompleteType_ExceptionThrown() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "java.util.List<String> extraTokens";
        try {
            parser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), 
                "Failed to parse type 'java.util.List<String> extraTokens' (remaining: ' extraTokens'): Unexpected token 'extraTokens', expected ',' or '>')");
        }
    }
    
    @Test
    public void testParse_UnexpectedEndOfString_ExceptionThrown() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "";
        try {
            parser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), 
                "Failed to parse type '' (remaining: ''): Unexpected end-of-string");
        }
    }
    
    @Test
    public void testParse_ClassNotFound_ExceptionThrown() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "com.example.NonExistentClass<String>";
        try {
            parser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), 
                "Failed to parse type 'com.example.NonExistentClass<String>' (remaining: ''): Can not locate class 'com.example.NonExistentClass', problem: java.lang.ClassNotFoundException: com.example.NonExistentClass");
        }
    }

}