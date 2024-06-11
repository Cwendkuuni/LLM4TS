import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TypeParserTest {
    
    private TypeFactory factory = TypeFactory.defaultInstance();
    private TypeParser parser = new TypeParser(factory);
    
    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType type = parser.parse(canonical);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }
    
    @Test
    public void testParseType() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("<java.lang.String>");
        Class<?> base = parser.findClass(className, tokens);
        assertNotNull(base);
        assertEquals(ArrayList.class, base);
        
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String,java.lang.Integer");
        List<JavaType> types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(2, types.size());
        assertEquals(String.class, types.get(0).getRawClass());
        assertEquals(Integer.class, types.get(1).getRawClass());
    }
    
    @Test
    public void testFindClass() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("<java.lang.String>");
        Class<?> base = parser.findClass(className, tokens);
        assertNotNull(base);
        assertEquals(ArrayList.class, base);
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        try {
            parser._problem(tokens, "Unexpected end-of-string");
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
}