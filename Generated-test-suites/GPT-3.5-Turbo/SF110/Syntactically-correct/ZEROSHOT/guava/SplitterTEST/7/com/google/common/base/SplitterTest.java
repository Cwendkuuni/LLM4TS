import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        String input = "apple,orange,banana";
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        String input = "apple;orange,banana";
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(":");
        String input = "key1:value1;key2:value2";
        List<String> expected = Arrays.asList("key1", "value1;key2", "value2");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "apple   orange   banana";
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "appleorangebanana";
        List<String> expected = Arrays.asList("app", "leo", "ran", "ge", "ban", "ana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        String input = ":::key1:value1:::key2:value2:::";
        List<String> expected = Arrays.asList("key1", "value1", "key2", "value2");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        String input = "apple,orange,banana";
        List<String> expected = Arrays.asList("apple", "orange,banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        String input = "  apple  ,  orange  ,  banana  ";
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('_'));
        String input = "__apple___,___orange___,___banana__";
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,orange,banana";
        Iterable<String> expected = Arrays.asList("apple", "orange", "banana");
        Iterable<String> actual = splitter.split(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,orange,banana";
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        List<String> actual = splitter.splitToList(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSplitToMap() {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings().withKeyValueSeparator(":");
        String input = "key1:value1,key2:value2,key3:value3";
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        Map<String, String> actual = splitter.split(input);
        assertEquals(expected, actual);
    }
}