import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1: basic parse with no generics
        String canonical1 = "com.example.MyClass";
        JavaType result1 = parser.parse(canonical1);
        assertEquals(factory._fromClass(null, com.example.MyClass.class, null), result1);
        
        // Test case 2: parse with generics
        String canonical2 = "java.util.List<com.example.MyClass>";
        JavaType result2 = parser.parse(canonical2);
        List<JavaType> parameterTypes2 = new ArrayList<>();
        parameterTypes2.add(factory._fromClass(null, com.example.MyClass.class, null));
        TypeBindings bindings2 = TypeBindings.create(List.class, parameterTypes2);
        assertEquals(factory._fromClass(null, List.class, bindings2), result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case: parse with invalid input
        String canonical = "invalidClass";
        parser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case: parse with unexpected tokens
        String canonical = "com.example.MyClass unexpectedToken";
        parser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case: parse with unexpected end of string
        String canonical = "com.example.MyClass<";
        parser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokenInGenerics() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case: parse with unexpected token in generics
        String canonical = "com.example.MyClass<com.example.UnexpectedToken>";
        parser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case: parse types with unexpected end of string
        String canonical = "com.example.MyClass,";
        MyTokenizer tokens = new TypeParser.MyTokenizer(canonical);
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithInvalidClassName() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case: find class with invalid class name
        String className = "invalidClass";
        MyTokenizer tokens = new TypeParser.MyTokenizer(className);
        parser.findClass(className, tokens);
    }
}