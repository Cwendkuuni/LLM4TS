import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals(result.getRawClass(), List.class);
        assertEquals(result.getContentType().getRawClass(), String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_MissingClosingBracket() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedTokens() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String> unexpected";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "";
        parser.parse(canonical);
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
        assertEquals(result.getRawClass(), List.class);
        assertEquals(result.getContentType().getRawClass(), String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_UnexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseType(tokens);
    }

    @Test
    public void testParseType_NonGenericType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
        assertEquals(result.getRawClass(), String.class);
        assertNull(result.getContentType());
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getRawClass(), String.class);
        assertEquals(result.get(1).getRawClass(), Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedToken() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, ");
        parser.parseTypes(tokens);
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> result = parser.findClass("java.lang.String", tokens);
        assertNotNull(result);
        assertEquals(result, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_ClassNotFound() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        parser.findClass("com.example.MyClass", tokens);
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        String msg = "Test message";
        IllegalArgumentException result = parser._problem(tokens, msg);
        assertNotNull(result);
        assertEquals(result.getMessage(), "Failed to parse type 'java.lang.String' (remaining: ''): " + msg);
    }

}