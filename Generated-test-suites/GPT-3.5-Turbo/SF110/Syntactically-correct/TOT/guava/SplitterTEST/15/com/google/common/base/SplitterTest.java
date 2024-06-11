import static org.junit.Assert.*;

import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        
        Iterable<String> result1 = splitter.split("abc-def-ghi");
        assertEquals("abc", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("123-456-789");
        assertEquals("123", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("-blue-red-green-");
        assertEquals("", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        
        Iterable<String> result1 = splitter.split("apple,orange;banana");
        assertEquals("apple", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("carrot,onion,cucumber");
        assertEquals("carrot", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split(",,carrot;;onion;,cucumber");
        assertEquals("", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(", ");
        
        Iterable<String> result1 = splitter.split("hello, world");
        assertEquals("hello", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("Java,Python,Ruby");
        assertEquals("Java", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split(",   no spaces before this");
        assertEquals("", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        
        Iterable<String> result1 = splitter.split("Hello   world");
        assertEquals("Hello", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("Java      Python   Ruby");
        assertEquals("Java", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("     No spaces before this");
        assertEquals("", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        
        Iterable<String> result1 = splitter.split("abcdefghi");
        assertEquals("abc", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("12345678");
        assertEquals("123", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("abc");
        assertFalse(result3.iterator().hasNext());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        
        splitter = splitter.omitEmptyStrings();
        
        Iterable<String> result1 = splitter.split("apple,orange,,banana");
        assertEquals("apple", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("carrot,onion,,cucumber");
        assertEquals("carrot", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("no,empty,,strings");
        assertFalse(result3.iterator().hasNext());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        
        Iterable<String> result1 = splitter.split("apple,orange,banana");
        assertEquals("apple", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("carrot,onion,cucumber");
        assertEquals("carrot", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("one,two,three,four");
        assertEquals("one", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        
        Iterable<String> result1 = splitter.split("  apple  , orange  ,  banana  ");
        assertEquals("apple", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("  carrot  ,  onion,  cucumber ");
        assertEquals("carrot", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("   no  ,  spaces ,   around  ");
        assertEquals("no", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        
        Iterable<String> result1 = splitter.split("apple,orange,banana");
        assertEquals("apple", result1.iterator().next());
        
        Iterable<String> result2 = splitter.split("carrot,onion,cucumber");
        assertEquals("carrot", result2.iterator().next());
        
        Iterable<String> result3 = splitter.split("no,empty,strings");
        assertEquals("no", result3.iterator().next());
        
        Iterable<String> result4 = splitter.split("");
        assertFalse(result4.iterator().hasNext());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        
        List<String> result1 = splitter.splitToList("apple,orange,banana");
        assertEquals("apple", result1.get(0));
        
        List<String> result2 = splitter.splitToList("carrot,onion,cucumber");
        assertEquals("carrot", result2.get(0));
        
        List<String> result3 = splitter.splitToList("no,empty,strings");
        assertEquals("no", result3.get(0));
        
        List<String> result4 = splitter.splitToList("");
        assertTrue(result4.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        
        String input1 = "key1:value1, key2:value2, key3:value3";
        assertEquals("value1", mapSplitter.split(input1).get("key1"));
        
        String input2 = "key1:value1, key2:value2, key3:value3";
        assertEquals("value2", mapSplitter.split(input2).get("key2"));
        
        String input3 = "key1:value1, key2:value2, key3:value3";
        assertEquals("value3", mapSplitter.split(input3).get("key3"));
        
        String input4 = "noKey, asValue!";
        assertNull(mapSplitter.split(input4).get("noKey"));
    }
}