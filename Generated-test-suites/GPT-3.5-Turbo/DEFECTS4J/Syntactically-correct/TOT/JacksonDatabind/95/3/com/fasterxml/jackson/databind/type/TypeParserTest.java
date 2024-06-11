import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

public class TypeParserTest {

    private TypeFactory factory = TypeFactory.defaultInstance();
    private TypeParser parser = new TypeParser(factory);

    @Test
    public void testParse_InvalidCanonical_ThrowsIllegalArgumentException() {
        try {
            parser.parse("invalidCanonical");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'invalidCanonical' (remaining: 'invalidCanonical'): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_ValidCanonical_ReturnsJavaType() {
        JavaType javaType = parser.parse("java.util.List<java.lang.String>");
        Assert.assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
        Assert.assertEquals(List.class, javaType.getRawClass());
    }
    
    @Test
    public void testParseType_NoTokens_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            parser.parseType(tokens);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseType_NonGenericBase_ReturnsJavaType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType javaType = parser.parseType(tokens);
        Assert.assertEquals("java.lang.String", javaType.toCanonical());
        Assert.assertEquals(String.class, javaType.getRawClass());
    }
    
    @Test
    public void testParseType_GenericBase_ReturnsJavaType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = parser.parseType(tokens);
        Assert.assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
        Assert.assertEquals(List.class, javaType.getRawClass());
    }
    
    @Test
    public void testParseTypes_NoTokens_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            parser.parseTypes(tokens);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes_InvalidToken_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String> invalidToken");
        try {
            parser.parseTypes(tokens);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String> invalidToken' (remaining: 'invalidToken'): Unexpected token 'invalidToken', expected ',' or '>')", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes_GenericsClosed_ReturnsListOfType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = parser.parseTypes(tokens);
        Assert.assertEquals("java.util.List<java.lang.String>", javaType.toCanonical());
        Assert.assertEquals(List.class, javaType.getRawClass());
    }

    @Test
    public void testFindClass_ValidClassName_ReturnsClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        Class<?> clazz = parser.findClass("java.util.List", tokens);
        Assert.assertEquals(List.class, clazz);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_InvalidClassName_ThrowsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.findClass("invalidClassName", tokens);
    }
    
}