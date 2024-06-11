import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class SplitterTest {
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(' ');
        Iterable<String> result = splitter.split("Hello World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOnCharSeparator_EmptyString() {
        Splitter splitter = Splitter.on(' ');
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testOnCharSeparator_MultipleSeparators() {
        Splitter splitter = Splitter.on(' ');
        Iterable<String> result = splitter.split("Hello  World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOnSeparatorMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        Iterable<String> result = splitter.split("Hello,World;Goodbye");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOnSeparatorMatcher_NoSeparators() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        Iterable<String> result = splitter.split("Hello World");
        assertEquals("Hello World", result.iterator().next());
    }
    
    @Test
    public void testOnSeparatorMatcher_EmptyString() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("Hello,World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOnStringSeparator_EmptyString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testOnStringSeparator_NoSeparators() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("Hello World");
        assertEquals("Hello World", result.iterator().next());
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("Hello   World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOnPatternSeparator_EmptyString() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testOnPatternSeparator_NoSeparators() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("HelloWorld");
        assertEquals("HelloWorld", result.iterator().next());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("HelloWorld");
        assertEquals("Hel", result.iterator().next());
    }
    
    @Test
    public void testFixedLength_EmptyString() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testFixedLength_LessThanLength() {
        Splitter splitter = Splitter.fixedLength(10);
        Iterable<String> result = splitter.split("Hello");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,,World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testOmitEmptyStrings_NoEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Iterable<String> result = splitter.split("Hello,World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("Hello,World,Goodbye");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testLimit_LessThanLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split("Hello,World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(" Hello , World ");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testTrimResults_EmptyString() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testTrimResults_NoWhitespaceToTrim() {
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split("Hello,World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("Hello,World");
        assertEquals("Hello", result.iterator().next());
    }
    
    @Test
    public void testSplit_EmptyString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("");
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("Hello,World");
        assertEquals("Hello", result.get(0));
    }
    
    @Test
    public void testSplitToList_EmptyString() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on("=").withKeyValueSeparator(",");
        Map<String, String> result = mapSplitter.split("key1,value1,key2,value2");
        assertEquals("value1", result.get("key1"));
    }
    
    @Test
    public void testWithKeyValueSeparator_NoKeyValuePairs() {
        Splitter.MapSplitter mapSplitter = Splitter.on("=").withKeyValueSeparator(",");
        Map<String, String> result = mapSplitter.split("");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparator_DuplicateKeys() {
        Splitter.MapSplitter mapSplitter = Splitter.on("=").withKeyValueSeparator(",");
        assertThrows(IllegalArgumentException.class, () -> {
            mapSplitter.split("key1,value1,key1,value2");
        });
    }
    
}