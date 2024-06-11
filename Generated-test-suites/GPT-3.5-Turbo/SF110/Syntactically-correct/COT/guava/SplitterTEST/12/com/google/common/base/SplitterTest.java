
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("apple,banana,cherry");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",."));
        List<String> result = splitter.splitToList("apple,banana.cherry");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("|");
        List<String> result = splitter.splitToList("apple|banana|cherry");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.splitToList("apple banana cherry");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.splitToList("abcdef");
        assertEquals(3, result.size());
        assertEquals("ab", result.get(0));
        assertEquals("cd", result.get(1));
        assertEquals("ef", result.get(2));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        List<String> result = splitter.splitToList("apple,,banana,,cherry");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        List<String> result = splitter.splitToList("apple,banana,cherry");
        assertEquals(2, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana,cherry", result.get(1));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList("  apple ,  banana ,  cherry  ");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testTrimResultsWithCustomMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        List<String> result = splitter.splitToList("  apple ,  banana ,  cherry  ");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("  banana", result.get(1));
        assertEquals("  cherry  ", result.get(2));
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("apple,banana,cherry");
        int count = 0;
        for (String str : result) {
            count++;
            switch (count) {
                case 1:
                    assertEquals("apple", str);
                    break;
                case 2:
                    assertEquals("banana", str);
                    break;
                case 3:
                    assertEquals("cherry", str);
                    break;
            }
        }
        assertEquals(3, count);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("apple,banana,cherry");
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("name=John,age=30");
        assertEquals(2, result.size());
        assertEquals("John", result.get("name"));
        assertEquals("30", result.get("age"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEntry() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        mapSplitter.split("name=John,age");
    }
}

