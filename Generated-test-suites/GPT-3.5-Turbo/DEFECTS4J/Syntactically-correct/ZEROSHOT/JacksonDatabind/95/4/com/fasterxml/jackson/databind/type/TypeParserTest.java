import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse() {
        try {
            JavaType javaType = typeParser.parse("");
            assertEquals(null, javaType);
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        try {
            typeParser.findClass("", tokens);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class '', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void test_problem() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("");
        IllegalArgumentException exception = typeParser._problem(tokens, "Test Exception");
        assertEquals("Failed to parse type '', remaining: '': Test Exception", exception.getMessage());
    }

}