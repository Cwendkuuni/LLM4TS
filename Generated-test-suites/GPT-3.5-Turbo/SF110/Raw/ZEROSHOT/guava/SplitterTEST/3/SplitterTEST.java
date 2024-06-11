import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on('a');
        Iterable<String> result = splitter.split("banana");
        List<String> expected = Arrays.asList("b", "n", "n");
        assertEquals(expected, result);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(':'));
        Iterable<String> result = splitter.split("apple:orange:banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("apple:orange:banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile(":");
        Splitter splitter = Splitter.on(pattern);
        Iterable<String> result = splitter.split("apple:orange:banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testOnPatternSeparatorWithEmptyString() {
        Pattern pattern = Pattern.compile("");
        try {
            Splitter splitter = Splitter.on(pattern);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("The pattern may not match the empty string: " + pattern, e.getMessage());
        }
    }

    @Test
    public void testOnPatternSeparatorWithSeparatorPattern() {
        Splitter splitter = Splitter.onPattern("[\\s,]");
        Iterable<String> result = splitter.split("apple,orange banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("123456");
        List<String> expected = Arrays.asList("12", "34", "56");
        assertEquals(expected, result);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        Iterable<String> result = splitter.split(":apple::orange:banana:");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        Iterable<String> result = splitter.split("apple:orange:banana");
        List<String> expected = Arrays.asList("apple", "orange:banana");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(":").trimResults();
        Iterable<String> result = splitter.split(": apple : orange : banana :");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(":").trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split(": apple : orange : banana :");
        List<String> expected = Arrays.asList("apple ", " orange ", " banana ");
        assertEquals(expected, result);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("apple:orange:banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        List<String> result = splitter.splitToList("apple:orange:banana");
        List<String> expected = Arrays.asList("apple", "orange", "banana");
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorWithStringSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorWithCharSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorWithSplitter() {
        Splitter keyValueSplitter = Splitter.on('=');
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator(keyValueSplitter);
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }

}