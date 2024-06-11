import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setup() {
        splitter = Splitter.on(',');
    }

    @Test
    public void testOnCharSeparator() {
        Iterable<String> result = splitter.split("apple,banana,cherry");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Iterable<String> result = Splitter.on(CharMatcher.anyOf(",;")).split("apple,banana;cherry");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnStringSeparator() {
        Iterable<String> result = Splitter.on("and").split("apple and banana and cherry");
        String[] expected = {"apple ", " banana ", " cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnPatternSeparator() {
        Iterable<String> result = Splitter.onPattern("\\s+").split("apple  banana   cherry");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testFixedLength() {
        Iterable<String> result = Splitter.fixedLength(2).split("12345");
        String[] expected = {"12", "34", "5"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOmitEmptyStrings() {
        Iterable<String> result = Splitter.on(",").omitEmptyStrings().split("apple,,,,banana,cherry,");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testLimit() {
        Iterable<String> result = Splitter.on(",").limit(2).split("apple,banana,cherry");
        String[] expected = {"apple", "banana,cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testTrimResults() {
        Iterable<String> result = Splitter.on(",").trimResults().split(" apple , banana , cherry ");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Iterable<String> result = Splitter.on(",").trimResults(CharMatcher.is('-')).split("-apple-,--banana-,-cherry-");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSplitToList() {
        List<String> result = splitter.splitToList("apple,banana,cherry");
        String[] expected = {"apple", "banana", "cherry"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("apple=red,banana=yellow,cherry=red");
        assertEquals("red", result.get("apple"));
        assertEquals("yellow", result.get("banana"));
        assertEquals("red", result.get("cherry"));
    }

}