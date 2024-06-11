import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testWithTypeFactory() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser newParser = parser.withFactory(factory);
        
        assertSame(factory, newParser._factory);
    }

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        JavaType javaType = parser.parse("java.util.List<Integer>");
        
        assertNotNull(javaType);
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.util.List");

        JavaType javaType = parser.parseType(tokens);

        assertNotNull(javaType);
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("com.fasterxml.jackson.databind.type.MyTokenizer, java.util.List");

        List<JavaType> types = parser.parseTypes(tokens);

        assertEquals(2, types.size());
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.lang.String");

        Class<?> clazz = parser.findClass("java.lang.String", tokens);

        assertEquals(String.class, clazz);
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.lang.Integer");

        IllegalArgumentException ex = parser._problem(tokens, "Some problem message");

        assertNotNull(ex);
    }
}