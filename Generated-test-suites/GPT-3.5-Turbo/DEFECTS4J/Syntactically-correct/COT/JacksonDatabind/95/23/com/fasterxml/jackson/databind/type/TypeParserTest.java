
import org.junit.Assert;
import org.junit.Test;

public class TypeParserTest {
        
    @Test
    public void testParse() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType result = typeParser.parse("java.util.Map<java.lang.String,java.util.List>");
        Assert.assertNotNull(result);
        Assert.assertEquals("java.util.Map<java.lang.String,java.util.List>", result.toCanonical());
    }
    
    @Test
    public void testParseType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String");
        JavaType result = typeParser.parseType(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals("java.lang.String", result.toCanonical());
    }
    
    @Test
    public void testParseTypes() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List,java.lang.String>");
        List<JavaType> result = typeParser.parseTypes(tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("java.util.List", result.get(0).toCanonical());
        Assert.assertEquals("java.lang.String", result.get(1).toCanonical());
    }
    
    @Test
    public void testFindClass() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String");
        Class<?> result = typeParser.findClass("java.lang.String", tokens);
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }
    
    @Test
    public void testProblem() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String");
        IllegalArgumentException result = typeParser._problem(tokens, "Test Error");
        Assert.assertNotNull(result);
        Assert.assertEquals("Failed to parse type 'java.lang.String' (remaining: 'java.lang.String'): Test Error", result.getMessage());
    }

}
