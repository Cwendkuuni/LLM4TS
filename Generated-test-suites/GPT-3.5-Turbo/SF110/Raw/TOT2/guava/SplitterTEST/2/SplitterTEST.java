import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertNotNull(splitter);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResultsWithTrimmer() {
        CharMatcher trimmer = CharMatcher.anyOf(" /");
        Splitter splitter = Splitter.on(',').trimResults(trimmer);
        assertNotNull(splitter);
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        // Check result
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        // Check result
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(":");
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparator_Char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter keyValueSplitter = Splitter.on(':');
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(keyValueSplitter);
        assertNotNull(mapSplitter);
    }

    // Additional tests for error scenarios and edge cases

}