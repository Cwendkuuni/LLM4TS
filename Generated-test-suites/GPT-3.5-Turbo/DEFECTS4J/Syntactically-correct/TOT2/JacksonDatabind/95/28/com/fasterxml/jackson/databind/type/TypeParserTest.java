import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setup() {
        TypeFactory factory = new TypeFactory();
        parser = new TypeParser(factory);
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParseTypeWithGenerics() {
        String canonical = "java.util.Map<java.lang.Integer, java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals(Map.class, result.getRawClass());
        assertEquals(Integer.class, result.containedType(0).getRawClass());
        assertEquals(String.class, result.containedType(1).getRawClass());
    }

    @Test
    public void testParseTypeWithUnexpectedTokens() {
        String canonical = "java.util.Set<java.lang.String> extraTokens";
        try {
            JavaType result = parser.parse(canonical);
            fail("Should have thrown IllegalArgumentException for unexpected tokens");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.Set<java.lang.String> extraTokens' (remaining: ' extraTokens'): Unexpected tokens after complete type", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypeWithNonGenericBase() {
        String canonical = "java.lang.String";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals(String.class, result.getRawClass());
        assertNull(result.getBindings());
    }

    @Test
    public void testParseTypeWithEndOfString() {
        String canonical = "";
        try {
            JavaType result = parser.parse(canonical);
            fail("Should have thrown IllegalArgumentException for unexpected end-of-string");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypesWithUnexpectedToken() {
        String canonical = "java.lang.List<java.lang.Integer, java.lang.String>";
        try {
            MyTokenizer tokens = parser.new MyTokenizer(canonical);
            parser.parseTypes(tokens);
            fail("Should have thrown IllegalArgumentException for unexpected token ','");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.List<java.lang.Integer, java.lang.String>' (remaining: '<java.lang.Integer, java.lang.String>): Unexpected token ',', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        String className = "java.util.List";
        MyTokenizer tokens = parser.new MyTokenizer(className);
        Class<?> result = parser.findClass(className, tokens);
        assertNotNull(result);
        assertEquals(List.class, result);
    }

    @Test
    public void testFindClassWithException() {
        String className = "nonexistent.Class";
        MyTokenizer tokens = parser.new MyTokenizer(className);
        try {
            parser.findClass(className, tokens);
            fail("Should have thrown IllegalArgumentException for nonexistent class");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not locate class 'nonexistent.Class', problem: "));
        }
    }

    // Include more test cases for edge cases and exceptions

}