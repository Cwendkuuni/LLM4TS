import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(' ');
        Iterable<String> result = splitter.split("Hello World");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("Hello,World");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("Hello,World");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Iterable<String> result = splitter.split("Hello    World");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("HelloWorld");
        List<String> expected = Arrays.asList("He", "ll", "oW", "or", "ld");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testSplitToString() {
        Splitter splitter = Splitter.on(",");
        String result = splitter.split("Hello,World").toString();
        String expected = "[Hello, World]";
        assertEquals(expected, result);
    }
    
    // Test cases for edge cases and exception handling scenarios
    
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableResultList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("Hello,World");
        result.add("Extra"); // Should throw UnsupportedOperationException
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,,World");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("Hello,World,Extra");
        List<String> expected = Arrays.asList("Hello", "World,Extra");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" Hello , World ");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split(" Hello , World ");
        List<String> expected = Arrays.asList("Hello", "World");
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testMapSplitter() {
        Splitter outerSplitter = Splitter.on(":");
        Splitter entrySplitter = Splitter.on(",");
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        
        String input = "key1,value1,key2,value2,key3,value3";
        Map<String, String> result = mapSplitter.split(input);
        
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        
        assertEquals(expected, result);
    }

    // 
    // Add more test cases here
    // 
}