import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertTrue(result.iterator().next().equals("apple"));
        // Add more test cases for edge cases and exceptions
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("apple;banana;orange");
        assertEquals(3, Lists.newArrayList(result).size());
        // Add more test cases for edge cases and exceptions
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("hello");
        assertEquals(3, Lists.newArrayList(result).size());
        // Add more test cases for edge cases and exceptions
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.omitEmptyStrings().split("apple,,banana");
        assertEquals(2, Lists.newArrayList(result).size());
        // Add more test cases for edge cases and exceptions
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.limit(2).split("apple,banana,orange");
        assertEquals(2, Lists.newArrayList(result).size());
        // Add more test cases for edge cases and exceptions
    }
    
    // Add more test methods for the remaining Splitter class methods
    
}