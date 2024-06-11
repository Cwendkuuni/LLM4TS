import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(',');

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testOnCharMatcher() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(CharMatcher.is(','));

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testOnString() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(",");

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testOnPattern() {
        String input = "Hello|World";
        Splitter splitter = Splitter.on(Pattern.compile("\\|"));

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testOnPatternString() {
        String input = "Hello|World";
        Splitter splitter = Splitter.onPattern("\\|");

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testFixedLength() {
        String input = "Hello,World";
        Splitter splitter = Splitter.fixedLength(5);

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals(",Worl", result.iterator().next());
        assertEquals("d", result.iterator().next());
    }

    @Test
    public void testOmitEmptyStrings() {
        String input = "Hello,,World";
        Splitter splitter = Splitter.on(",").omitEmptyStrings();

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testLimit() {
        String input = "Hello,World,Java";
        Splitter splitter = Splitter.on(",").limit(2);

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World,Java", result.iterator().next());
    }

    @Test
    public void testTrimResults() {
        String input = "  Hello  ,  World  ";
        Splitter splitter = Splitter.on(",").trimResults();

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        String input = "  Hello  ,  World  ";
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.WHITESPACE);

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testSplit() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(",");

        Iterable<String> result = splitter.split(input);

        assertEquals("Hello", result.iterator().next());
        assertEquals("World", result.iterator().next());
    }

    @Test
    public void testSplitToList() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(",");

        List<String> result = splitter.splitToList(input);

        assertEquals("Hello", result.get(0));
        assertEquals("World", result.get(1));
    }

    @Test
    public void testWithKeyValueSeparatorString() {
        String input = "key1=value1,key2=value2";
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");

        Map<String, String> result = mapSplitter.split(input);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        String input = "key1=value1,key2=value2";
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator('=');

        Map<String, String> result = mapSplitter.split(input);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        String input = "key1=value1,key2=value2";
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(Splitter.on("="));

        Map<String, String> result = mapSplitter.split(input);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

}