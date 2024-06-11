import org.junit.*;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(';');
        Iterable<String> result = splitter.split("Hello;World;");
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",."));
        Iterable<String> result = splitter.split("Hello,World.123");
        String[] expected = {"Hello", "World", "123"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(", ");
        Iterable<String> result = splitter.split("Hello, World, 123");
        String[] expected = {"Hello", "World", "123"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("Hello   World    123");
        String[] expected = {"Hello", "World", "123"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("HelloWorld123");
        String[] expected = {"Hel", "loW", "orl", "d12", "3"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,,World,");
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("Hello,World,123");
        String[] expected = {"Hello", "World,123"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" Hello , World ");
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is('_'));
        Iterable<String> result = splitter.split(" _Hello_ , _World_ ");
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,World,123");
        String[] expected = {"Hello", "World", "123"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        List<String> result = splitter.splitToList("Hello,World,123");
        String[] expected = {"Hello", "World", "123"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

}