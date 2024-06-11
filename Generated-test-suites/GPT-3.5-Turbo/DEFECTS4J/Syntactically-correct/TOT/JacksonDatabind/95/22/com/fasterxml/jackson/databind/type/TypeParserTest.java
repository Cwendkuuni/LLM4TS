import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;
    private TypeFactory typeFactory;
    
    @Before
    public void setup() {
        typeFactory = mock(TypeFactory.class);
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse_typicalUseCase() {
        String canonical = "java.util.List<java.lang.String>";
        MyTokenizer tokenizer = mock(MyTokenizer.class);
        when(typeFactory._fromClass(null, List.class, null)).thenReturn(mock(JavaType.class));
        
        try {
            JavaType result = typeParser.parse(canonical);
            
            verify(tokenizer, times(2)).hasMoreTokens(); // Checking that tokenizer.hasMoreTokens() called twice
            verify(tokenizer, times(2)).nextToken(); // Checking that tokenizer.nextToken() called twice
            verify(tokenizer).getAllInput(); // Checking that tokenizer.getAllInput() called once
            verify(tokenizer).getRemainingInput(); // Checking that tokenizer.getRemainingInput() called once
            verify(typeFactory).findClass("java.util.List"); // Checking that typeFactory.findClass() was called
            verify(typeFactory).findClass("java.lang.String"); // Checking that typeFactory.findClass() was called
            verify(typeFactory._fromClass(null, List.class, null)); // Checking that typeFactory._fromClass() was called
            
            assertNotNull(result);
        } catch (IllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_endOfString() {
        String canonical = "";
        MyTokenizer tokenizer = mock(MyTokenizer.class);
        
        try {
            JavaType result = typeParser.parse(canonical);
            
            verify(tokenizer).hasMoreTokens(); // Checking that tokenizer.hasMoreTokens() called once
            verify(tokenizer).getAllInput(); // Checking that tokenizer.getAllInput() called once
            verify(tokenizer).getRemainingInput(); // Checking that tokenizer.getRemainingInput() called once
            
            assertNotNull(result);
        } catch (IllegalArgumentException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_endOfString_exception() {
        String canonical = "java.util.List<java.lang.String> extra";
        MyTokenizer tokenizer = mock(MyTokenizer.class);
        
        try {
            JavaType result = typeParser.parse(canonical);
            
            fail("Expected exception IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            verify(tokenizer, times(2)).hasMoreTokens(); // Checking that tokenizer.hasMoreTokens() called twice
            verify(tokenizer, times(2)).nextToken(); // Checking that tokenizer.nextToken() called twice
            verify(tokenizer).getAllInput(); // Checking that tokenizer.getAllInput() called once
            verify(tokenizer).getRemainingInput(); // Checking that tokenizer.getRemainingInput() called once
            
            assertEquals("Failed to parse type 'java.util.List<java.lang.String> extra' (remaining: ' extra'): Unexpected tokens after complete type", e.getMessage());
        }
    }
    
    @Test
    public void testParse_unexpectedEndOfString() {
        String canonical = "java.util.List<java.lang.String<";
        MyTokenizer tokenizer = mock(MyTokenizer.class);
        
        try {
            JavaType result = typeParser.parse(canonical);
            
            fail("Expected exception IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            verify(tokenizer, times(2)).hasMoreTokens(); // Checking that tokenizer.hasMoreTokens() called twice
            verify(tokenizer, times(2)).nextToken(); // Checking that tokenizer.nextToken() called twice
            verify(tokenizer).getAllInput(); // Checking that tokenizer.getAllInput() called once
            verify(tokenizer).getRemainingInput(); // Checking that tokenizer.getRemainingInput() called once
            
            assertEquals("Failed to parse type 'java.util.List<java.lang.String<' (remaining: '<'): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_baseClass_notFound() {
        String canonical = "java.util.UnknownClass";
        MyTokenizer tokenizer = mock(MyTokenizer.class);
        when(typeFactory.findClass("java.util.UnknownClass")).thenThrow(new RuntimeException("Class not found exception"));
        
        try {
            JavaType result = typeParser.parse(canonical);
            
            fail("Expected exception IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            verify(tokenizer).hasMoreTokens(); // Checking that tokenizer.hasMoreTokens() called once
            verify(tokenizer).getAllInput(); // Checking that tokenizer.getAllInput() called once
            verify(tokenizer).getRemainingInput(); // Checking that tokenizer.getRemainingInput() called once
            verify(typeFactory).findClass("java.util.UnknownClass"); // Checking that typeFactory.findClass() was called
            
            assertEquals("Failed to parse type 'java.util.UnknownClass' (remaining: 'UnknownClass'): Can not locate class 'java.util.UnknownClass', problem: Class not found exception", e.getMessage());
        }
    }

}