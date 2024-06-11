import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        String[] expected = { "a", "b", "c" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(':'));
        Iterable<String> result = splitter.split("a:b:c");
        String[] expected = { "a", "b", "c" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("|");
        Iterable<String> result = splitter.split("a|b|c");
        String[] expected = { "a", "b", "c" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Iterable<String> result = splitter.split("hello   world");
        String[] expected = { "hello", "world" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("123456");
        String[] expected = { "12", "34", "56" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("a,,b,");
        String[] expected = { "a", "b" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("a,b,c,d,e");
        String[] expected = { "a", "b,c,d,e" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split("  a  ,  b  ,  c  ");
        String[] expected = { "a", "b", "c" };
        int i = 0;
        for(String str : result) {
            assertEquals(expected[i], str);
            i++;
        }
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        String[] expected = { "a", "b", "c" };
        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result.get(i));
        }
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter splitter = Splitter.on(",").withKeyValueSeparator(":");
        Map<String, String> result = splitter.split("a:1,b:2,c:3");
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
        assertEquals("3", result.get("c"));
    }
}