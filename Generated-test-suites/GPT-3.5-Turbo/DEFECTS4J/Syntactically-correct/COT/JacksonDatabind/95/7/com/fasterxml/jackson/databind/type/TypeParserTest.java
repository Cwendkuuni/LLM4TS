
import org.junit.Test;
import org.junit.Assert;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        // Arrange
        TypeParser typeParser = new TypeParser(new TypeFactory());
        String canonical = "java.util.List<java.lang.String>";
        
        // Act
        JavaType result = typeParser.parse(canonical);
        
        // Assert
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(String.class, result.getContentType().getRawClass());
    }
    
    @Test
    public void testParseType() {
        // Arrange
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        // Act
        JavaType result = typeParser.parseType(tokens);
        
        // Assert
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(String.class, result.getContentType().getRawClass());
    }
    
    @Test
    public void testParseTypes() {
        // Arrange
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer");
        
        // Act
        List<JavaType> result = typeParser.parseTypes(tokens);
        
        // Assert
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(String.class, result.get(0).getRawClass());
        Assert.assertEquals(Integer.class, result.get(1).getRawClass());
    }
    
    @Test
    public void testFindClass() {
        // Arrange
        TypeParser typeParser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        
        // Act
        Class<?> result = typeParser.findClass("java.lang.String", tokens);
        
        // Assert
        Assert.assertEquals(String.class, result);
    }
}
