import org.junit.*;
import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOn_char() {
        Splitter splitter = Splitter.on(',');
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testOn_string() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOn_emptyString() {
        Splitter.on("");
    }

    @Test
    public void testOn_pattern() {
        Splitter splitter = Splitter.on(Pattern.compile(","));
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testOnPattern_string() {
        Splitter splitter = Splitter.onPattern(",");
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "abcdefghij";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(5, Iterables.size(result));
        assertEquals("ab", Iterables.get(result, 0));
        assertEquals("cd", Iterables.get(result, 1));
        assertEquals("ef", Iterables.get(result, 2));
        assertEquals("gh", Iterables.get(result, 3));
        assertEquals("ij", Iterables.get(result, 4));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        String input = "apple,,banana,,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(2, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana,orange", Iterables.get(result, 1));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        String input = " apple , banana , orange ";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testTrimResults_trimmer() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        String input = " apple , banana , orange ";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testSplit_charSequence() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        assertNotNull(result);
        assertEquals(3, Iterables.size(result));
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,banana,orange";
        List<String> result = splitter.splitToList(input);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("orange", result.get(2));
    }

    @Test
    public void testWithKeyValueSeparator_string() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        String input = "key1:value1,key2:value2,key3:value3";
        Map<String, String> result = mapSplitter.split(input);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparator_char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(':');
        String input = "key1:value1,key2:value2,key3:value3";
        Map<String, String> result = mapSplitter.split(input);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparator_splitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(Splitter.on(":"));
        String input = "key1:value1,key2:value2,key3:value3";
        Map<String, String> result = mapSplitter.split(input);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

}