
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(null);
        JavaType result = parser.parse("");
        assertNull(result);
    }

    @Test
    public void testParseType() {
        TypeParser parser = new TypeParser(null);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseType(tokens);
        });
        assertEquals("Unexpected end-of-string", exception.getMessage());
    }

    @Test
    public void testParseTypes() {
        TypeParser parser = new TypeParser(null);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseTypes(tokens);
        });
        assertEquals("Unexpected end-of-string", exception.getMessage());
    }

    @Test
    public void testFindClass() {
        TypeParser parser = new TypeParser(null);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.findClass("", tokens);
        });
        assertEquals("Can not locate class '', problem: null", exception.getMessage());
    }

    @Test
    public void test_problem() {
        TypeParser parser = new TypeParser(null);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parser._problem(tokens, "");
        });
        assertEquals("Failed to parse type '', remaining: '': ", exception.getMessage());
    }

}
