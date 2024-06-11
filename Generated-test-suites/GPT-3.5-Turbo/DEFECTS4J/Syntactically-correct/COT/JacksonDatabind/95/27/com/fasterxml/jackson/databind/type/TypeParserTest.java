import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        String canonical = "java.util.List<java.lang.String>";
        JavaType type = parser.parse(canonical);
        assertNotNull(type);
        assertEquals("java.util.List<java.lang.String>", type.toCanonical());
    }

    @Test
    public void testParseType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals("java.util.List<java.lang.String>", type.toCanonical());
    }

    @Test
    public void testParseTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        TypeParser.MyTokenizer tokens2 = parser.new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        JavaType type1 = parser.parseType(tokens);
        JavaType type2 = parser.parseType(tokens2);
        assertNotNull(type1);
        assertNotNull(type2);
        assertEquals("java.util.List<java.lang.String>", type1.toCanonical());
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", type2.toCanonical());
    }

    @Test
    public void testFindClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        Class<?> clazz = parser.findClass("java.lang.String", tokens);
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testProblem() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        IllegalArgumentException ex = parser._problem(tokens, "Test message");
        assertNotNull(ex);
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Test message", ex.getMessage());
    }

    @Test
    public void testParseEdgeCases() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        String canonical1 = "java.util.List";
        String canonical2 = "java.util.Map<java.lang.String, java.lang.Integer>";
        String canonical3 = "java.util.List<>";
        JavaType type1 = parser.parse(canonical1);
        JavaType type2 = parser.parse(canonical2);
        JavaType type3 = parser.parse(canonical3);
        assertNotNull(type1);
        assertNotNull(type2);
        assertNotNull(type3);
        assertEquals(canonical1, type1.toCanonical());
        assertEquals(canonical2, type2.toCanonical());
        assertEquals("java.util.List<?>", type3.toCanonical());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseExceptions() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        String canonical = "java.util.";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeExceptions() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.pushBack(">");
        parser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesExceptions() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.pushBack("java.util.Map<java.lang.String, java.lang.Integer>");
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassExceptions() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.UnknownClass");
        parser.findClass("java.util.UnknownClass", tokens);
    }
    
}


@RunWith(JUnit4.class)
public class TypeParserTest {

   // Your test cases here

}
