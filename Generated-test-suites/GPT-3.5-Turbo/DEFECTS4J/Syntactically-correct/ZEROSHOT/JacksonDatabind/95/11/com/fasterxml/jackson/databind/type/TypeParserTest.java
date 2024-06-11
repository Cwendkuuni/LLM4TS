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
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
 
        assertNotNull(result);
        assertEquals(ArrayList.class, result.getRawClass());
        assertEquals(1, result.containedTypeCount());
        assertEquals(String.class, result.containedType(0).getRawClass());
    }
 
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = parser.parseType(tokens);
 
        assertNotNull(result);
        assertEquals(ArrayList.class, result.getRawClass());
        assertEquals(1, result.containedTypeCount());
        assertEquals(String.class, result.containedType(0).getRawClass());
    }
 
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, int>");
        List<JavaType> result = parser.parseTypes(tokens);
 
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(String.class, result.get(0).getRawClass());
        assertEquals(int.class, result.get(1).getRawClass());
    }
 
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        Class<?> result = parser.findClass("java.util.List", tokens);
 
        assertNotNull(result);
        assertEquals(ArrayList.class, result);
    }
 
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        IllegalArgumentException result = parser._problem(tokens, "Unexpected end-of-string");
 
        assertNotNull(result);
        assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Unexpected end-of-string", result.getMessage());
    }
 
}