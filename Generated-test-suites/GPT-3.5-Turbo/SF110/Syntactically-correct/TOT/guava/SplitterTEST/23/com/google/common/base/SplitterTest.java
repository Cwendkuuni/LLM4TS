import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        String input = "apple,banana,cherry,";
        List<String> expected = List.of("apple", "banana", "cherry");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(':'));
        String input = "key1:value1:key2:value2";
        List<String> expected = List.of("key1", "value1", "key2", "value2");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(",");
        String input = "key1,value1,value2";
        List<String> expected = List.of("key1", "value1", "value2");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "apple  banana  cherry";
        List<String> expected = List.of("apple", "banana", "cherry");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        String input = "1234567890";
        List<String> expected = List.of("123", "456", "789", "0");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',')
                .omitEmptyStrings();
        String input = "apple,banana,,cherry,,";
        List<String> expected = List.of("apple", "banana", "cherry");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        String input = "apple,banana,cherry";
        List<String> expected = List.of("apple", "banana,cherry");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        String input = " apple , banana , cherry ";
        List<String> expected = List.of("apple", "banana", "cherry");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testTrimResultsWithCustomMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
        String input = " apple , banana , cherry ";
        List<String> expected = List.of("apple ", " banana ", " cherry ");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testSplitToIterable() {
        Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
        String input = " apple , banana , , cherry , ";
        Iterable<String> expected = List.of("apple", "banana", "cherry");
        assertEquals(expected, splitter.split(input));
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
        String input = "apple,banana,cherry";
        List<String> expected = List.of("apple", "banana", "cherry");
        assertEquals(expected, splitter.splitToList(input));
    }

    @Test
    public void testMapSplitter() {
        Splitter outerSplitter = Splitter.on(';');
        Splitter entrySplitter = Splitter.on(':');
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        String input = "key1:value1;key2:value2";
        Map<String, String> expected = Map.of("key1", "value1", "key2", "value2");
        assertEquals(expected, mapSplitter.split(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterInvalidKey() {
        Splitter outerSplitter = Splitter.on(';');
        Splitter entrySplitter = Splitter.on(':');
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        String input = "key1;value1;key2:value2";
        mapSplitter.split(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapSplitterDuplicateKey() {
        Splitter outerSplitter = Splitter.on(';');
        Splitter entrySplitter = Splitter.on(':');
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        String input = "key1:value1;key1:value2";
        mapSplitter.split(input);
    }
}