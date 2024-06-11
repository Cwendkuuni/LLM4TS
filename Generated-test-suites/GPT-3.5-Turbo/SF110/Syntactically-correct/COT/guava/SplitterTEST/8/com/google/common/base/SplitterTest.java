
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertArrayEquals(new String[]{"apple", "banana", "orange"}, result.toArray());
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",."));
        Iterable<String> result = splitter.split("apple,banana.orange");
        assertArrayEquals(new String[]{"apple", "banana", "orange"}, result.toArray());
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("key1:value1:key2:value2");
        assertArrayEquals(new String[]{"key1", "value1", "key2", "value2"}, result.toArray());
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile(","));
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertArrayEquals(new String[]{"apple", "banana", "orange"}, result.toArray());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("applebananaorange");
        assertArrayEquals(new String[]{"ap", "pl", "eb", "an", "an", "or", "an", "ge"}, result.toArray());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(':').omitEmptyStrings();
        Iterable<String> result = splitter.split("key1: :key2:value2");
        assertArrayEquals(new String[]{"key1", "key2", "value2"}, result.toArray());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(':').limit(2);
        Iterable<String> result = splitter.split("key1:value1:key2:value2");
        assertArrayEquals(new String[]{"key1", "value1:key2:value2"}, result.toArray());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(':').trimResults();
        Iterable<String> result = splitter.split("  key1 : value1  :  key2  : value2  ");
        assertArrayEquals(new String[]{"key1", "value1", "key2", "value2"}, result.toArray());
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(':').trimResults(CharMatcher.is('_'));
        Iterable<String> result = splitter.split("__key1__:__value1__");
        assertArrayEquals(new String[]{"key1", "value1"}, result.toArray());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(':');
        List<String> result = splitter.splitToList("key1:value1:key2:value2");
        assertArrayEquals(new String[]{"key1", "value1", "key2", "value2"}, result.toArray());
    }
    
    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
}
