
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;

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
        JavaType type = parser.parse(canonical);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertEquals(String.class, type.containedType(0).getRawClass());
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.ArrayList");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
    }
    
    @Test
    public void testParseTypeWithGenerics() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertEquals(String.class, type.containedType(0).getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithInvalidGenerics() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.ArrayList<java.lang.String");
        parser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.ArrayList");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(0, type.containedTypeCount());
    }
    
    @Test
    public void testParseTypesWithGenerics() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertEquals(String.class, type.containedType(0).getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithInvalidGenerics() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.ArrayList<java.lang.String");
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithInvalidTokenType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.ArrayList,");
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithInvalidClassName() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.NonExistentClass");
        parser.findClass(tokens.nextToken(), tokens);
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.ArrayList");
        IllegalArgumentException exception = parser._problem(tokens, "Unexpected end-of-string");
        assertNotNull(exception);
        assertEquals("Failed to parse type 'java.util.ArrayList' (remaining: ''): Unexpected end-of-string", exception.getMessage());
    }
    
}
