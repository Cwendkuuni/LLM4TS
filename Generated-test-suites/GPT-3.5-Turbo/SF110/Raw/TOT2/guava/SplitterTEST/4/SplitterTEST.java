import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,carrot");
        assertEquals("apple", result.iterator().next());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("apple:banana:carrot");
        assertEquals(3, Lists.newArrayList(result).size());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("apple banana carrot");
        assertEquals("banana", Lists.newArrayList(result).get(1));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcde");
        assertEquals(3, Lists.newArrayList(result).size());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",")
                .omitEmptyStrings();
        Iterable<String> result = splitter.split("apple,,banana,,carrot");
        assertEquals(3, Lists.newArrayList(result).size());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",")
                .limit(2);
        Iterable<String> result = splitter.split("apple,banana,carrot");
        assertEquals(2, Lists.newArrayList(result).size());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",")
                .trimResults();
        Iterable<String> result = splitter.split(" apple , banana , carrot ");
        assertEquals("apple", Lists.newArrayList(result).get(0));
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        MapSplitter mapSplitter = Splitter.on(",")
                .withKeyValueSeparator("=");
        Map<String, String> resultMap = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", resultMap.get("key1"));
    }
}