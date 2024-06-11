
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(canonical);
        assertNotNull(result);
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        assertNotNull(result);
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.nextToken(); // Skip class name token
        JavaType result = typeParser.parseTypes(tokens);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnexpectedEndOfString() {
        String canonical = "";
        JavaType result = typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnexpectedTokensAfterCompleteType() {
        String canonical = "java.util.List<java.lang.String>";
        canonical += " unexpected";
        JavaType result = typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        JavaType result = typeParser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeUnexpectedToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.nextToken(); // Skip class name token
        tokens.pushBack("unexpected");
        JavaType result = typeParser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        JavaType result = typeParser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesUnexpectedToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.nextToken(); // Skip class name token
        tokens.pushBack("unexpected");
        JavaType result = typeParser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassException() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.nextToken(); // Skip class name token
        tokens.nextToken(); // Skip '<' token
        tokens.nextToken(); // Skip type token
        tokens.pushBack("classnotfound");
        typeParser.findClass("classnotfound", tokens);
    }
}
