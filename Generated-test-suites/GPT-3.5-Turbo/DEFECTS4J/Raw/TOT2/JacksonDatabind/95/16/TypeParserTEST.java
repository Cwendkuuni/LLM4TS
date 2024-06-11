import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TypeParserTest {
    
    @Test
    public void testWithFactory() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        
        TypeParser newTypeParser = typeParser.withFactory(factory);
        
        assertEquals(factory, newTypeParser._factory);
    }
    
    @Test
    public void testParseSingleType() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        
        JavaType javaType = typeParser.parse("java.lang.String");
        
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }
    
    @Test
    public void testParseGenericType() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
        
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertTrue(javaType.containedTypeCount() > 0);
    }
    
    @Test
    public void testFindClassSuccess() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        
        String className = "java.lang.String";
        Class<?> clazz = typeParser.findClass(className, new TypeParser.MyTokenizer(""));
        
        assertEquals(String.class, clazz);
    }
    
    @Test
    public void testFindClassFailure() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        
        String className = "com.example.NonExistentClass";
        
        try {
            typeParser.findClass(className, new TypeParser.MyTokenizer(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        
        try {
            typeParser._problem(tokens, "Test message");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
    
}