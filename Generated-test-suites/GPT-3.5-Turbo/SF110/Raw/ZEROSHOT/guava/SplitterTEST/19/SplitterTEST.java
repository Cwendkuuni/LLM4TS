import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(' ');
        List<String> result = splitter.splitToList("hello world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        List<String> result = splitter.splitToList("apple,banana;cherry");
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("=");
        List<String> result = splitter.splitToList("key1=value1,key2=value2");
        List<String> expected = Arrays.asList("key1", "value1,key2", "value2");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s");
        List<String> result = splitter.splitToList("hello world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.splitToList("hello");
        List<String> expected = Arrays.asList("he", "ll", "o");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        List<String> result = splitter.splitToList("apple,,banana,");
        List<String> expected = Arrays.asList("apple", "banana");
        assertEquals(expected, result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        List<String> result = splitter.splitToList("apple,banana,cherry");
        List<String> expected = Arrays.asList("apple", "banana,cherry");
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        List<String> result = splitter.splitToList(" apple , banana , cherry ");
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,cherry");
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("apple,banana,cherry");
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
}