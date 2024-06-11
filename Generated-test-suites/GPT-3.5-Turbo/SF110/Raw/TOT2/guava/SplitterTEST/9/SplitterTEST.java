import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        assertNotNull(splitter);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        assertNotNull(splitter);
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        assertNotNull(splitter);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator("=");
        assertNotNull(mapSplitter);
    }

    // Additional test cases for edge cases and exception handling scenarios should be added here

}