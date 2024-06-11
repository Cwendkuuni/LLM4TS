
import com.fasterxml.jackson.databind.type.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "java.util.List<java.lang.String>";
        
        JavaType result = parser.parse(canonical);
        
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(String.class, result.getBindings().getBoundType(0).getRawClass());
    }

    @Test
    public void testParseType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.String>");
        
        JavaType result = parser.parseType(tokenizer);
        
        Assert.assertEquals(List.class, result.getRawClass());
        Assert.assertEquals(String.class, result.getBindings().getBoundType(0).getRawClass());
    }

    @Test
    public void testParseTypes() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.String>");
        
        List<JavaType> result = parser.parseTypes(tokenizer);
        
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(List.class, result.get(0).getRawClass());
        Assert.assertEquals(String.class, result.get(0).getBindings().getBoundType(0).getRawClass());
    }

    @Test
    public void testFindClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.String>");
        
        Class<?> result = parser.findClass("java.util.List", tokenizer);
        
        Assert.assertEquals(List.class, result);
    }

    @Test
    public void testProblem() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.String>");
        
        IllegalArgumentException exception = parser._problem(tokenizer, "Unexpected end-of-string");
        
        Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Unexpected end-of-string", exception.getMessage());
    }
}

