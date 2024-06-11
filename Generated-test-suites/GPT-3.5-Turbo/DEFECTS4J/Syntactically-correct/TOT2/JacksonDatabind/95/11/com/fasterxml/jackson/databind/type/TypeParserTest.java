import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_ValidCanonicalString_ReturnsJavaType() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(canonical);
        
        assertNotNull(result);
        assertEquals(TypeFactory.defaultInstance().constructCollectionType(List.class, String.class), result);
    }

    @Test
    public void testParse_EmptyString_ThrowsIllegalArgumentException() {
        String canonical = "";
        try {
            JavaType result = typeParser.parse(canonical);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidCanonicalString_ThrowsIllegalArgumentException() {
        String canonical = "java.util.List?java.lang.String";
        try {
            JavaType result = typeParser.parse(canonical);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List?java.lang.String' (remaining: '?java.lang.String'): Can not locate class 'List?java.lang.String', problem: No question mark in class name", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_ValidCanonicalString_ReturnsListOfJavaType() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>, java.util.Map<java.lang.Integer, java.lang.Double>");
        List<JavaType> result = typeParser.parseTypes(tokens);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TypeFactory.defaultInstance().constructCollectionType(List.class, String.class), result.get(0));
    }

    @Test
    public void testFindClass_ValidClassName_ReturnsClass() {
        Class<?> result = typeParser.findClass("java.util.List", new TypeParser.MyTokenizer("java.util.List"));
        
        assertNotNull(result);
        assertEquals(List.class, result);
    }

    @Test
    public void testFindClass_InvalidClassName_ThrowsIllegalArgumentException() {
        try {
            Class<?> result = typeParser.findClass("List?", new TypeParser.MyTokenizer("List?"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List?' (remaining: '?'): Can not locate class 'List?', problem: No question mark in class name", e.getMessage());
        }
    }

}