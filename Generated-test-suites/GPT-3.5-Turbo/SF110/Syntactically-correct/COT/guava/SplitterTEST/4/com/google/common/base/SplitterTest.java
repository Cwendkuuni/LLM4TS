import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c,d");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abcd", sb.toString());
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",-"));
        Iterable<String> result = splitter.split("a-b,c");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abc", sb.toString());
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("&&");
        Iterable<String> result = splitter.split("a&&b&&c");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abc", sb.toString());

    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("[0-9]+"));
        Iterable<String> result = splitter.split("a1b2c3");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abc", sb.toString());

    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abc");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abc", sb.toString());

    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split(",a,,b,c,");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abc", sb.toString());

    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        Iterable<String> result = splitter.split("a,b,c,d");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("ab,c,d", sb.toString());

    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" a ,b, c , d ");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abc d", sb.toString());

    }

    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split(" a ,b, c , d ");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("a,b,c,d", sb.toString());

    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c,d");
        StringBuilder sb = new StringBuilder();
        for(String s : result) {
            sb.append(s);
        }
        assertEquals("abcd", sb.toString());

    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c,d");
        assertEquals(4, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
        assertEquals("d", result.get(3));

    }

    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));

    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));

    }

    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(Splitter.on("="));
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));

    }

}