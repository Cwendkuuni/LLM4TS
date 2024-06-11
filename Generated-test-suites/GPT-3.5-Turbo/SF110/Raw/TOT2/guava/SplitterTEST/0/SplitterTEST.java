import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("x;y;z");
        assertEquals(Arrays.asList("x", "y", "z"), result);
    }
    
    @Test
    public void testOnPattern() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern);
        Iterable<String> result = splitter.split("Hello   World");
        assertEquals(Arrays.asList("Hello", "World"), result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("12345");
        assertEquals(Arrays.asList("12", "34", "5"), result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.omitEmptyStrings().split("a,,b,,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.limit(2).split("a,b,c");
        assertEquals(Arrays.asList("a", "b,c"), result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.trimResults().split("  a  ,  b  ,  c  ");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("x,y,z");
        assertEquals(Arrays.asList("x", "y", "z"), result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        assertEquals(2, result.size());
        assertTrue(result.containsKey("key1"));
        assertTrue(result.containsKey("key2"));
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
    
    // Add more test cases for edge cases and error scenarios
    
}