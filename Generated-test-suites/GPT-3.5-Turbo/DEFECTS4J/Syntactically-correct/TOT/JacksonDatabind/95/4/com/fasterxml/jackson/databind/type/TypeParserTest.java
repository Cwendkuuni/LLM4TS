import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        
        String canonical1 = "java.util.List<java.lang.String>";
        JavaType parsedType1 = typeParser.parse(canonical1);
        Assert.assertEquals(typeFactory.constructParametricType(List.class, String.class), parsedType1);

        String canonical2 = "com.example.CustomClass<java.lang.String, java.lang.Integer>";
        JavaType parsedType2 = typeParser.parse(canonical2);
        Assert.assertEquals(typeFactory.constructParametricType(CustomClass.class, String.class, Integer.class), parsedType2);

        String canonical3 = "java.lang.Object";
        JavaType parsedType3 = typeParser.parse(canonical3);
        Assert.assertEquals(typeFactory.constructType(Object.class), parsedType3);
    }

    @Test
    public void testParseType() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        String canonical = "java.util.List<java.lang.String>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(canonical);
        
        Class<?> base = typeParser.findClass("java.util.List", tokenizer);
        Assert.assertEquals(List.class, base);

        tokenizer.nextToken();  // Skip '<' token
        List<JavaType> parameterTypes = typeParser.parseTypes(tokenizer);
        Assert.assertEquals(1, parameterTypes.size());
        Assert.assertEquals(typeFactory.constructType(String.class), parameterTypes.get(0));

        JavaType parsedType = typeParser.parseType(tokenizer);
        Assert.assertEquals(typeFactory.constructParametricType(List.class, String.class), parsedType);
    }

    @Test
    public void testParseTypes() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        String canonical = "java.util.List<java.lang.String, java.lang.Integer>";
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer(canonical);

        List<JavaType> types = typeParser.parseTypes(tokenizer);
        Assert.assertEquals(2, types.size());
        Assert.assertEquals(typeFactory.constructType(String.class), types.get(0));
        Assert.assertEquals(typeFactory.constructType(Integer.class), types.get(1));
        
        tokenizer.nextToken();  // Skip '>' token
        JavaType parsedType = typeParser.parseType(tokenizer);
        Assert.assertEquals(typeFactory.constructParametricType(List.class, String.class, Integer.class), parsedType);
    }

    @Test
    public void testFindClass() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);

        String className = "java.util.List";
        Class<?> clazz = typeParser.findClass(className, null);
        Assert.assertEquals(List.class, clazz);
    }

    @Test
    public void testProblem() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.util.List<java.lang.String");

        IllegalArgumentException exception = typeParser._problem(tokenizer, "Unexpected end-of-string");
        Assert.assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: ''): Unexpected end-of-string", exception.getMessage());
    }
}