import static org.junit.Assert.*;

import org.junit.Test;

public class SplitterTest {
    Splitter splitter;

    @Test
    public void testOnChar() {
        splitter = Splitter.on(' ');
        assertNotNull(splitter);
    }

    @Test
    public void testOnCharMatcher() {
        splitter = Splitter.on(CharMatcher.is('/'));
        assertNotNull(splitter);
    }

    @Test
    public void testOnString() {
        splitter = Splitter.on("/");
        assertNotNull(splitter);
    }

    @Test
    public void testOnPattern() {
        splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
    }

    @Test
    public void testFixedLength() {
        splitter = Splitter.fixedLength(2);
        assertNotNull(splitter);
    }

    @Test
    public void testOmitEmptyStrings() {
        splitter = Splitter.on(' ').omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        splitter = Splitter.on(' ').limit(2);
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResults() {
        splitter = Splitter.on(' ').trimResults();
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        splitter = Splitter.on(' ').trimResults(CharMatcher.DIGIT);
        assertNotNull(splitter);
    }

    @Test
    public void testSplit() {
        Iterable<String> result = Splitter.on(' ').split("Hello World");
        assertNotNull(result);
    }

    @Test
    public void testSplitToList() {
        List<String> result = Splitter.on(' ').splitToList("Hello World");
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(' ').withKeyValueSeparator("=");
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(' ').withKeyValueSeparator('=');
        assertNotNull(mapSplitter);
    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(' ').withKeyValueSeparator(Splitter.on('='));
        assertNotNull(mapSplitter);
    }
}