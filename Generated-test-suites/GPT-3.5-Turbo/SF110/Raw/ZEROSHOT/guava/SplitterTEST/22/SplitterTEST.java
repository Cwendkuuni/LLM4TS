import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        String input = "a,b,c";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(", "));
        String input = "a,b c";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("/");
        String input = "a/b/c";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\d+");
        String input = "a1b2c3";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "abcde";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"ab", "cd", "e"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        String input = "a,,b";
        Iterable<String> result = splitter.omitEmptyStrings().split(input);
        String[] expected = {"a", "b"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        String input = "a,b,c";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b,c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        String input = " a , b , c ";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        String input = "a,b,c";
        List<String> result = splitter.splitToList(input);
        String[] expected = {"a", "b", "c"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = 
                Splitter.on(",").withKeyValueSeparator(":");
        String input = "key1:value1,key2:value2";
        Map<String, String> result = mapSplitter.split(input);
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

}