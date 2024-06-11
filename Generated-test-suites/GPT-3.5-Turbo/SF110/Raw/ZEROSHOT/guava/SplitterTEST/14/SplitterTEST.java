import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.*;
import java.util.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(' ');
        Iterable<String> result = splitter.split("hello world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        Iterable<String> result = splitter.split("one,two;three");
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("hello  world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("abcde");
        List<String> expected = Arrays.asList("abc", "de");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("one,,two,,three");
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expected = Arrays.asList("one", "two,three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" one , two , three ");
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("one,two,three");
        List<String> expected = Arrays.asList("one", "two", "three");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter keyValueSplitter = Splitter.on(":");
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(keyValueSplitter);
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
}