import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_typicalCase() {
        String canonical = "java.util.HashMap";
        
        try {
            JavaType result = typeParser.parse(canonical);
            assertNotNull(result);
            assertEquals(canonical, result.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Expected IllegalArgumentException not to be thrown");
        }
    }

    @Test
    public void testParse_emptyString() {
        String canonical = "";
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_invalidClass() {
        String canonical = "com.invalid.InvalidClass";
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
            assertEquals("Can not locate class 'com.invalid.InvalidClass', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParse_typeWithGenerics() {
        String canonical = "java.util.HashMap<java.lang.String, java.lang.Integer>";
        
        try {
            JavaType result = typeParser.parse(canonical);
            assertNotNull(result);
            assertEquals(canonical, result.toCanonical());
            assertEquals("java.util.HashMap", result.getRawClass().getName());
            assertEquals("java.lang.String", result.containedType(0).toCanonical());
            assertEquals("java.lang.Integer", result.containedType(1).toCanonical());
        } catch (IllegalArgumentException e) {
            fail("Expected IllegalArgumentException not to be thrown");
        }
    }

    @Test
    public void testParse_typeWithOpeningBracketMissing() {
        String canonical = "java.util.HashMap<java.lang.String, java.lang.Integer>";
        // remove opening bracket
        canonical = canonical.replace("<", "");
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_typeWithInvalidToken() {
        String canonical = "java.util.HashMap<java.lang.String, java.lang.Integer>";
        // replace comma with semicolon
        canonical = canonical.replace(",", ";");
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParse_typeWithClosingBracketMissing() {
        String canonical = "java.util.HashMap<java.lang.String, java.lang.Integer>";
        // remove closing bracket
        canonical = canonical.replace(">", "");
        
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}