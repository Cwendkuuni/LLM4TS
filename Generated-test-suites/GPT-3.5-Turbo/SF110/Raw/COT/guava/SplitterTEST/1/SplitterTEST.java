
import org.junit.Test;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern(",");
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("123456");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("12");
        expectedResult.add("34");
        expectedResult.add("56");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("one,,two,,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("one,two,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two,three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" one , two , three ");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split(" one , two , three ");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult, result.toArray());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("one,two,three");
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("one");
        expectedResult.add("two");
        expectedResult.add("three");
        assertArrayEquals(expectedResult.toArray(), result.toArray());
    }

    @Test
    public void testWithKeyValueSeparatorOnChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split("key1:val1,key2:val2");
        Map<String, String> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", "val1");
        expectedResult.put("key2", "val2");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testWithKeyValueSeparatorOnString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        Map<String, String> result = mapSplitter.split("key1:val1,key2:val2");
        Map<String, String> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", "val1");
        expectedResult.put("key2", "val2");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testWithKeyValueSeparatorOnSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(Splitter.on(":"));
        Map<String, String> result = mapSplitter.split("key1:val1,key2:val2");
        Map<String, String> expectedResult = new LinkedHashMap<>();
        expectedResult.put("key1", "val1");
        expectedResult.put("key2", "val2");
        assertEquals(expectedResult, result);
    }
}
