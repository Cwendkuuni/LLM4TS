
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1: Test parsing of a basic JavaType
        String canonical1 = "java.util.List";
        JavaType result1 = parser.parse(canonical1);
        assertEquals("java.util.List", result1.getRawClass().getName());
        
        // Test case 2: Test parsing of a generic JavaType
        String canonical2 = "java.util.Map<java.lang.Integer, java.lang.String>";
        JavaType result2 = parser.parse(canonical2);
        assertEquals("java.util.Map", result2.getRawClass().getName());
        assertEquals("java.lang.Integer", result2.getBindings().getTypeParameters().get(0).getRawClass().getName());
        assertEquals("java.lang.String", result2.getBindings().getTypeParameters().get(1).getRawClass().getName());
        
        // Test case 3: Test parsing of an incomplete type specification
        String canonical3 = "com.fasterxml.jackson.databind.JavaType<java.util.List, java.util.Map>";
        try {
            JavaType result3 = parser.parse(canonical3);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.fasterxml.jackson.databind.JavaType<java.util.List, java.util.Map>' (remaining: '<java.util.List, java.util.Map>): Unexpected token 'com.fasterxml.jackson.databind.JavaType', expected ',' or '>')", e.getMessage());
        }
        
        // Test case 4: Test parsing of empty string
        String canonical4 = "";
        try {
            JavaType result4 = parser.parse(canonical4);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1: Test parsing of a basic JavaType
        TypeParser.MyTokenizer tokenizer1 = parser.new MyTokenizer("java.util.List");
        JavaType result1 = parser.parseType(tokenizer1);
        assertEquals("java.util.List", result1.getRawClass().getName());
        
        // Test case 2: Test parsing of a generic JavaType
        TypeParser.MyTokenizer tokenizer2 = parser.new MyTokenizer("java.util.Map<java.lang.Integer, java.lang.String>");
        JavaType result2 = parser.parseType(tokenizer2);
        assertEquals("java.util.Map", result2.getRawClass().getName());
        assertEquals("java.lang.Integer", result2.getBindings().getTypeParameters().get(0).getRawClass().getName());
        assertEquals("java.lang.String", result2.getBindings().getTypeParameters().get(1).getRawClass().getName());
        
        // Test case 3: Test parsing of an incomplete type specification
        TypeParser.MyTokenizer tokenizer3 = parser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType<java.util.List, java.util.Map>");
        try {
            JavaType result3 = parser.parseType(tokenizer3);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.fasterxml.jackson.databind.JavaType<java.util.List, java.util.Map>' (remaining: '>'): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 4: Test parsing of empty type
        TypeParser.MyTokenizer tokenizer4 = parser.new MyTokenizer("");
        try {
            JavaType result4 = parser.parseType(tokenizer4);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1: Test parsing of a list of JavaTypes
        TypeParser.MyTokenizer tokenizer1 = parser.new MyTokenizer("java.util.List, java.util.Map<java.lang.Integer, java.lang.String>");
        List<JavaType> result1 = parser.parseTypes(tokenizer1);
        assertEquals(2, result1.size());
        assertEquals("java.util.List", result1.get(0).getRawClass().getName());
        assertEquals("java.util.Map", result1.get(1).getRawClass().getName());
        assertEquals("java.lang.Integer", result1.get(1).getBindings().getTypeParameters().get(0).getRawClass().getName());
        assertEquals("java.lang.String", result1.get(1).getBindings().getTypeParameters().get(1).getRawClass().getName());
        
        // Test case 2: Test parsing of an incomplete list of JavaTypes
        TypeParser.MyTokenizer tokenizer2 = parser.new MyTokenizer("java.util.List, java.util.Map<java.lang.Integer, java.lang.String>");
        try {
            List<JavaType> result2 = parser.parseTypes(tokenizer2);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List, java.util.Map<java.lang.Integer, java.lang.String>' (remaining: '<java.lang.Integer, java.lang.String>): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 3: Test parsing of empty type list
        TypeParser.MyTokenizer tokenizer3 = parser.new MyTokenizer("");
        try {
            List<JavaType> result3 = parser.parseTypes(tokenizer3);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1: Test finding of a valid class
        TypeParser.MyTokenizer tokenizer1 = parser.new MyTokenizer("java.util.List");
        Class<?> result1 = parser.findClass("java.util.List", tokenizer1);
        assertEquals("java.util.List", result1.getName());
        
        // Test case 2: Test finding of a non-existent class
        TypeParser.MyTokenizer tokenizer2 = parser.new MyTokenizer("java.foo.Bar");
        try {
            Class<?> result2 = parser.findClass("java.foo.Bar", tokenizer2);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'java.foo.Bar', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void test_problem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1: Test creation of IllegalArgumentException with tokens
        TypeParser.MyTokenizer tokenizer1 = parser.new MyTokenizer("");
        IllegalArgumentException result1 = parser._problem(tokenizer1, "Unexpected end-of-string");
        assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", result1.getMessage());
        
        // Test case 2: Test creation of IllegalArgumentException with tokens and message
        TypeParser.MyTokenizer tokenizer2 = parser.new MyTokenizer("java.foo.Bar");
        IllegalArgumentException result2 = parser._problem(tokenizer2, "Unexpected token 'java.foo.Bar', expected ',' or '>')");
        assertEquals("Failed to parse type 'java.foo.Bar' (remaining: 'java.foo.Bar'): Unexpected token 'java.foo.Bar', expected ',' or '>')", result2.getMessage());
    }

}
