import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import com.google.common.base.*;

public class SplitterTest {
    @Test
    public void testOn_char() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c,d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        Iterable<String> result = splitter.split("a,b;c;d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOn_string() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("a;b;c;d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOn_pattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Iterable<String> result = splitter.split("hello   world");
        List<String> expected = Arrays.asList("hello", "world");
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("hello");
        List<String> expected = Arrays.asList("he", "ll", "o");
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("a,,b,,,c");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("a,b,c,d");
        List<String> expected = Arrays.asList("a", "b,c,d");
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" a , b , c ");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c,d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c,d");
        List<String> expected = Arrays.asList("a", "b", "c", "d");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator_string() {
        Splitter.MapSplitter mapSplitter = Splitter.on(";").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1;key2=value2");
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator_char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(";").withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1;key2=value2");
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator_Splitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(";").withKeyValueSeparator(Splitter.on("="));
        Map<String, String> result = mapSplitter.split("key1=value1;key2=value2");
        Map<String, String> expected = new LinkedHashMap<String, String>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, result);
    }
}