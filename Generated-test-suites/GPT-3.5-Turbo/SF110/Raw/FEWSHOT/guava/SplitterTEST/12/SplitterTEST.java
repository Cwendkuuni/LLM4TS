import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcde");
        assertEquals("ab", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,,,b,,c");
        assertTrue(result.iterator().hasNext());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
        splitter = splitter.omitEmptyStrings();
        result = splitter.split("a,,,b,,c");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c,d,e");
        assertEquals("a", result.iterator().next());
        assertEquals(5, result.spliterator().getExactSizeIfKnown());
        splitter = splitter.limit(2);
        result = splitter.split("a,b,c,d,e");
        assertEquals("a", result.iterator().next());
        assertEquals(2, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("  a  ,  b  ,  c  ");
        assertEquals("  a  ", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
        splitter = splitter.trimResults();
        result = splitter.split("  a  ,  b  ,  c  ");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testTrimResultsCharMatcher() {
        CharMatcher trimmer = CharMatcher.is(' ');
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("  a  ,  b  ,  c  ");
        assertEquals("  a  ", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
        splitter = splitter.trimResults(trimmer);
        result = splitter.split("  a  ,  b  ,  c  ");
        assertEquals("a", result.iterator().next());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        assertTrue(result.iterator().hasNext());
        assertEquals(3, result.spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        assertTrue(!result.isEmpty());
        assertEquals(3, result.size());
    }
    
    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertTrue(!result.isEmpty());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
    
    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertTrue(!result.isEmpty());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
    
    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter keyValueSplitter = Splitter.on("=");
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(keyValueSplitter);
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertTrue(!result.isEmpty());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }
}