import org.junit.*;
import com.fasterxml.jackson.databind.JavaType;

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
        JavaType expected = factory._fromClass(null, java.util.List.class, TypeBindings.create(java.util.List.class, factory._fromClass(null, java.lang.String.class, null)));
        JavaType result = parser.parse(canonical);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType expected = factory._fromClass(null, java.util.List.class, TypeBindings.create(java.util.List.class, factory._fromClass(null, java.lang.String.class, null)));
        JavaType result = parser.parseType(tokens);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        List<JavaType> expected = new ArrayList<JavaType>();
        expected.add(factory._fromClass(null, java.util.List.class, TypeBindings.create(java.util.List.class, factory._fromClass(null, java.lang.String.class, null))));
        List<JavaType> result = parser.parseTypes(tokens);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        Class<?> expected = java.lang.String.class;
        Class<?> result = parser.findClass("java.lang.String", tokens);
        Assert.assertEquals(expected, result);
    }
}