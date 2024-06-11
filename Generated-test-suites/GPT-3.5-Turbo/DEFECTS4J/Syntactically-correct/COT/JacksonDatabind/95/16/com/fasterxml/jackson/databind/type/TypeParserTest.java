
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        JavaType result = parser.parse("");
        assertNotNull(result);
    }

    @Test
    public void testParseType() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");

        try {
            parser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");

        try {
            parser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        Class<?> result = parser.findClass("", tokens);
        assertNull(result);
    }

    @Test
    public void test_problem() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        IllegalArgumentException exception = parser._problem(tokens, "message");
        assertEquals("Failed to parse type '' (remaining: ''): message", exception.getMessage());
    }

    @Test
    public void testMyTokenizer_hasMoreTokens() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        boolean result = tokens.hasMoreTokens();
        assertFalse(result);
    }

    @Test
    public void testMyTokenizer_nextToken() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("token");
        String result = tokens.nextToken();
        assertEquals("token", result);
    }

    @Test
    public void testMyTokenizer_pushBack() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("token");
        tokens.pushBack("pushback");
        String result = tokens.nextToken();
        assertEquals("pushback", result);
    }

    @Test
    public void testMyTokenizer_getAllInput() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("input");
        String result = tokens.getAllInput();
        assertEquals("input", result);
    }

    @Test
    public void testMyTokenizer_getRemainingInput() {
        TypeFactory f = new TypeFactory();
        TypeParser parser = new TypeParser(f);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("input");
        tokens.nextToken();
        String result = tokens.getRemainingInput();
        assertEquals("nput", result);
    }

}
