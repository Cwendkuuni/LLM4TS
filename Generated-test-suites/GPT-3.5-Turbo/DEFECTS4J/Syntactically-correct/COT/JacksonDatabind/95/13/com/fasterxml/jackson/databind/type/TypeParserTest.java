
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeParser = new TypeParser();
    }
    
    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType type = typeParser.parse(canonical);
        assertNotNull(type);
        assertEquals(type.getRawClass(), List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        String canonical = "java.util.List<java.lang.String";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        String canonical = "java.util.List<java.lang.String>>";
        typeParser.parse(canonical);
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType type = typeParser.parseType(tokens);
        assertNotNull(type);
        assertEquals(type.getRawClass(), List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        typeParser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        List<JavaType> types = typeParser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(types.size(), 1);
        JavaType type = types.get(0);
        assertEquals(type.getRawClass(), List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<");
        typeParser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        Class<?> clazz = typeParser.findClass("java.util.List", tokens);
        assertNotNull(clazz);
        assertEquals(clazz, List.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithInvalidClassName() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        typeParser.findClass("invalid.class.name", tokens);
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        IllegalArgumentException exception = typeParser._problem(tokens, "Invalid type");
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Failed to parse type 'java.util.List' (remaining: ''): Invalid type");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMyTokenizerWithUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        tokens.nextToken();
    }
    
    @Test
    public void testMyTokenizerWithPushBack() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        tokens.pushBack("java.util.");
        String token = tokens.nextToken();
        assertNotNull(token);
        assertEquals(token, "java.util.");
    }
}
