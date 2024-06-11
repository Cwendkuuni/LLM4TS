import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeFactory factory;
    private TypeParser parser;

    @Before
    public void setUp() {
        factory = new TypeFactory();
        parser = new TypeParser(factory);
    }

    @Test
    public void testParse_ValidCanonicalInput_ReturnsJavaType() {
        String input = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(input);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.containedType(0).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_EmptyInput_ThrowsIllegalArgumentException() {
        String input = "";
        parser.parse(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidClassName_ThrowsIllegalArgumentException() {
        String input = "RandomClass<java.lang.Integer>";
        parser.parse(input);
    }

    // Additional test cases for edge cases and exception scenarios can be added here

}