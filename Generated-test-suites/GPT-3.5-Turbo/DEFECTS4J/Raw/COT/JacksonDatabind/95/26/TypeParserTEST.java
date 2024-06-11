import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

    private TypeFactory typeFactory = TypeFactory.defaultInstance();
    private TypeParser typeParser = new TypeParser(typeFactory);

    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType expectedType = typeFactory.constructCollectionType(List.class, String.class);
        JavaType actualType = typeParser.parse(canonical);
        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType expectedType = typeFactory.constructCollectionType(List.class, String.class);
        JavaType actualType = typeParser.parseType(tokens);
        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType expectedType = typeFactory.constructCollectionType(List.class, String.class);
        JavaType actualType = typeParser.parseTypes(tokens).get(0);
        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindClass() {
        String className = "java.lang.String";
        Class<?> expectedClass = String.class;
        Class<?> actualClass = typeParser.findClass(className, null);
        Assert.assertEquals(expectedClass, actualClass);
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        IllegalArgumentException expectedException = new IllegalArgumentException("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Unexpected end-of-string");
        IllegalArgumentException actualException = typeParser._problem(tokens, "Unexpected end-of-string");
        Assert.assertEquals(expectedException.getMessage(), actualException.getMessage());
    }

    @Test
    public void testAllInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        String expectedInput = "java.util.List<java.lang.String>";
        String actualInput = tokens.getAllInput();
        Assert.assertEquals(expectedInput, actualInput);
    }

    @Test
    public void testRemainingInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        tokens.nextToken();
        String expectedInput = "<java.lang.String>";
        String actualInput = tokens.getRemainingInput();
        Assert.assertEquals(expectedInput, actualInput);
    }

}