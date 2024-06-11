import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("a,b,c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(":;"));
        List<String> result = splitter.splitToList("a:b;c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        List<String> result = splitter.splitToList("a;b;c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\D+");
        List<String> result = splitter.splitToList("a1b2c3");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.splitToList("123456");
        assertEquals(Arrays.asList("12", "34", "56"), result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        List<String> result = splitter.splitToList("a::b:::c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        List<String> result = splitter.splitToList("a:b:c:d");
        assertEquals(Arrays.asList("a", "b:c:d"), result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList(" a ,  b  , c ");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('_'));
        List<String> result = splitter.splitToList("_a_,__b__,__c__");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("a:b:c");
        assertTrue(result instanceof Iterable);
        List<String> listResult = (List<String>) result;
        assertEquals(Arrays.asList("a", "b", "c"), listResult);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        List<String> result = splitter.splitToList("a:b:c");
        assertEquals(Arrays.asList("a", "b", "c"), result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        Map<String, String> result = mapSplitter.split("a:1,b:2,c:3");
        assertEquals(3, result.size());
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
        assertEquals("3", result.get("c"));
    }
}