import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("-");
        Iterable<String> result = splitter.split("apple-banana-cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Iterable<String> result = splitter.split("apple banana cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("apple,,banana,,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals(2, Lists.newArrayList(result).size());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" apple,banana,cherry ");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("apple,banana,cherry");
        assertEquals(3, result.size());
    }

    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        Map<String, String> map = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", map.get("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSeparator() {
        Splitter.on("").split("apple,banana,cherry");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEntry() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        mapSplitter.split("key1=value1,key2");
    }

}