import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {
    
    TypeFactory factory = new TypeFactory();

    @Test
    public void testParseValidType() {
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(1, result.containedTypeCount());
        assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test
    public void testParseInvalidType() {
        TypeParser parser = new TypeParser(factory);
        String canonical = "InvalidType<SomeOtherType>";
        try {
            JavaType result = parser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'InvalidType<SomeOtherType>' (remaining: '<SomeOtherType>'): Can not locate class 'InvalidType', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParseEmptyType() {
        TypeParser parser = new TypeParser(factory);
        String canonical = "";
        try {
            JavaType result = parser.parse(canonical);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    // Add more test cases for edge cases and exception handling scenarios

}