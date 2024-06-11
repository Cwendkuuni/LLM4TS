import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String typeString = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(typeString);
        JavaType expected = factory.constructParametricType(List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<String>");
        JavaType result = parser.parseType(tokens);
        JavaType expected = factory.constructParametricType(List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<String>,java.lang.Integer");
        JavaType result = parser.parseTypes(tokens).get(0);
        JavaType expected = factory.constructParametricType(List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String className = "java.util.List";
        Class<?> result = parser.findClass(className, new TypeParser.MyTokenizer(""));
        assertEquals(List.class, result);
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("invalid<?>");
        IllegalArgumentException result = parser._problem(tokens, "Test error message");
        String expected = "Failed to parse type 'invalid<?>' (remaining: '<?>'): Test error message";
        assertEquals(expected, result.getMessage());
    }
}