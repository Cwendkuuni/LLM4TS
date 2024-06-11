import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Splitter;

public class SplitterTest {
    
    @Test
    public void testOn_Char() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testOn_String() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("x;y;z");
        assertEquals(Arrays.asList("x", "y", "z"), result);
    }
    
    @Test
    public void testOn_Pattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("apple orange  banana");
        assertEquals(Arrays.asList("apple", "orange", "banana"), result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("123");
        assertEquals(Arrays.asList("12", "3"), result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('.').omitEmptyStrings();
        Iterable<String> result = splitter.split(".hello..world.");
        assertEquals(Arrays.asList("hello", "world"), result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("apple,orange,banana");
        assertEquals(Arrays.asList("apple", "orange,banana"), result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split("  apple , orange ,  banana  ");
        assertEquals(Arrays.asList("apple", "orange", "banana"), result);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.fixedLength(1);
        List<String> result = splitter.splitToList("abcd");
        assertEquals(Arrays.asList("a", "b", "c", "d"), result);
    }
    
    @Test
    public void testMapSplitter() {
        Splitter outerSplitter = Splitter.on(';');
        Splitter entrySplitter = Splitter.on('=');
        
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        Map<String, String> resultMap = mapSplitter.split("key1=value1;key2=value2");
        
        assertEquals("value1", resultMap.get("key1"));
        assertEquals("value2", resultMap.get("key2"));
    }
}