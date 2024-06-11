
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("/");
        List<String> result = splitter.splitToList("path/to/file");
        assertEquals(3, result.size());
        assertEquals("path", result.get(0));
        assertEquals("to", result.get(1));
        assertEquals("file", result.get(2));
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s");
        List<String> result = splitter.splitToList("hello world");
        assertEquals(2, result.size());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.splitToList("abcde");
        assertEquals(2, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("de", result.get(1));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        List<String> result = splitter.splitToList("a,,,b,,,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b,c", result.get(1));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList(" a , b , c ");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
}