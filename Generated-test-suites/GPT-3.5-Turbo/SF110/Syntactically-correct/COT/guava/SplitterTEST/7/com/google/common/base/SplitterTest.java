
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(';');
        
        Iterable<String> result = splitter.split("apple;banana;cherry");
        
        assertEquals("apple", result.iterator().next());
        // Test other elements in result
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        
        Iterable<String> result = splitter.split("apple,banana,cherry");
        
        assertEquals("apple", result.iterator().next());
        // Test other elements in result
    }
    
    // Generate other test cases for public methods    
}

