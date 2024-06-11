import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOn_CharSeparator() {
        Splitter splitter = Splitter.on(',');
        
        Iterable<String> result = splitter.split("A,B,C");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testOn_CharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(":;"));
        
        Iterable<String> result = splitter.split("A:B;C");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testOn_StringSeparator() {
        Splitter splitter = Splitter.on("-");
        
        Iterable<String> result = splitter.split("A-B-C");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testOn_PatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\d+");
        
        Iterable<String> result = splitter.split("A1B2C3");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        
        Iterable<String> result = splitter.split("ABCDEF");
        
        assertEquals("AB", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        
        Iterable<String> result = splitter.split("A,,B,,C");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        
        Iterable<String> result = splitter.split("A,B,C");
        
        assertEquals("A,B", result.iterator().next());
        assertEquals(2, Iterables.size(result));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        
        Iterable<String> result = splitter.split("A , B , C ");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testTrimResults_CharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.DIGIT);
        
        Iterable<String> result = splitter.split("A1 , B2 , C3 ");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        
        Iterable<String> result = splitter.split("A,B,C");
        
        assertEquals("A", result.iterator().next());
        assertEquals(3, Iterables.size(result));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        
        List<String> result = splitter.splitToList("A,B,C");
        
        assertEquals("A", result.get(0));
        assertEquals(3, result.size());
    }
    
    @Test
    public void testWithKeyValueSeparator_StringSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        
        Map<String, String> resultMap = mapSplitter.split("A=1,B=2,C=3");
        
        assertEquals("1", resultMap.get("A"));
        assertEquals(3, resultMap.size());
    }
    
    @Test
    public void testWithKeyValueSeparator_CharSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        
        Map<String, String> resultMap = mapSplitter.split("A:1,B:2,C:3");
        
        assertEquals("1", resultMap.get("A"));
        assertEquals(3, resultMap.size());
    }
    
    @Test
    public void testWithKeyValueSeparator_SplitterSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(Splitter.on(':'));
        
        Map<String, String> resultMap = mapSplitter.split("A:1,B:2,C:3");
        
        assertEquals("1", resultMap.get("A"));
        assertEquals(3, resultMap.size());
    }
    
    @Test
    public void testMapSplitter_InvalidEntry() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        
        try {
            mapSplitter.split("A");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Chunk [A] is not a valid entry", e.getMessage());
        }
    }

}