import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {
    private TypeFactory typeFactory;
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeFactory = TypeFactory.defaultInstance();
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse() {
        String canonical = "com.fasterxml.jackson.databind.JavaType";
        JavaType expected = typeFactory.constructType(JavaType.class);
        JavaType actual = typeParser.parse(canonical);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidCanonical() {
        String canonical = "com.fasterxml.jackson.databind.SomeUnknownType";
        typeParser.parse(canonical);
    }
    
    @Test
    public void testParseWithGenerics() {
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType expected = typeFactory.constructMapType(Map.class, String.class, Integer.class);
        JavaType actual = typeParser.parse(canonical);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseWithNestedGenerics() {
        String canonical = "java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>";
        JavaType expected = typeFactory.constructParametricType(List.class,
                typeFactory.constructMapType(Map.class, String.class, Integer.class));
        JavaType actual = typeParser.parse(canonical);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidGenericsSyntax() {
        String canonical = "java.util.Map<java.lang.String";
        typeParser.parse(canonical);
    }
}