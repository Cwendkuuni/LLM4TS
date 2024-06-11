import org.junit.*;
import static org.junit.Assert.*;

public class SplitterTest {
    private Splitter splitter;
    
    @Before
    public void setUp() {
        splitter = new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
                return new SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int start) {
                        // Custom implementation for testing
                        return 0;
                    }
                    
                    @Override
                    int separatorEnd(int separatorPosition) {
                        // Custom implementation for testing
                        return 0;
                    }
                };
            }
        });
    }
    
    @Test
    public void testOn_CharacterSeparator() {
        Splitter splitter = Splitter.on(',');
        
        Iterable<String> result = splitter.split("1,2,3,4,5");
        
        assertEquals("Incorrect number of elements", 5, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "5", get(result, 4));
    }
    
    @Test
    public void testOn_CharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        
        Iterable<String> result = splitter.split("1,2,3,4,5");
        
        assertEquals("Incorrect number of elements", 5, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "5", get(result, 4));
    }
    
    @Test
    public void testOn_StringSeparator() {
        Splitter splitter = Splitter.on(",");
        
        Iterable<String> result = splitter.split("1,2,3,4,5");
        
        assertEquals("Incorrect number of elements", 5, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "5", get(result, 4));
    }
    
    @Test
    public void testOn_PatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        
        Iterable<String> result = splitter.split("Hello   World");
        
        assertEquals("Incorrect number of elements", 2, size(result));
        assertEquals("Incorrect first element", "Hello", get(result, 0));
        assertEquals("Incorrect last element", "World", get(result, 1));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        
        Iterable<String> result = splitter.split("123456");
        
        assertEquals("Incorrect number of elements", 3, size(result));
        assertEquals("Incorrect first element", "12", get(result, 0));
        assertEquals("Incorrect last element", "56", get(result, 2));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        
        Iterable<String> result = splitter.split("1,,3,,5");
        
        assertEquals("Incorrect number of elements", 3, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "5", get(result, 2));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);
        
        Iterable<String> result = splitter.split("1,2,3,4,5");
        
        assertEquals("Incorrect number of elements", 3, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "3,4,5", get(result, 2));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        
        Iterable<String> result = splitter.split("  1  ,  2  ,  3  ");
        
        assertEquals("Incorrect number of elements", 3, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "3", get(result, 2));
    }
    
    @Test
    public void testTrimResults_CharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
        
        Iterable<String> result = splitter.split("  1  ,  2  ,  3  ");
        
        assertEquals("Incorrect number of elements", 3, size(result));
        assertEquals("Incorrect first element", "1", get(result, 0));
        assertEquals("Incorrect last element", "3", get(result, 2));
    }
    
    @Test
    public void testSplit() {
        Iterable<String> result = splitter.split("1,2,3,4,5");
        
        assertEquals("Incorrect number of elements", 0, size(result));
    }
    
    @Test
    public void testSplitToList() {
        List<String> result = splitter.splitToList("1,2,3,4,5");
        
        assertEquals("Incorrect number of elements", 0, result.size());
        assertTrue("Result should be empty", result.isEmpty());
    }
    
    @Test
    public void testWithKeyValueSeparator_OnCharacterSeparator() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(':');
        
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2,key3:value3");
        
        assertEquals("Incorrect number of elements", 3, result.size());
        assertEquals("Incorrect value for key1", "value1", result.get("key1"));
        assertEquals("Incorrect value for key2", "value2", result.get("key2"));
        assertEquals("Incorrect value for key3", "value3", result.get("key3"));
    }
    
    @Test
    public void testWithKeyValueSeparator_OnStringSeparator() {
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(":");
        
        Map<String, String> result = mapSplitter.split("key1:value1,key2:value2,key3:value3");
        
        assertEquals("Incorrect number of elements", 3, result.size());
        assertEquals("Incorrect value for key1", "value1", result.get("key1"));
        assertEquals("Incorrect value for key2", "value2", result.get("key2"));
        assertEquals("Incorrect value for key3", "value3", result.get("key3"));
    }
    
    private int size(Iterable<?> iterable) {
        int count = 0;
        for (Object element : iterable) {
            count++;
        }
        return count;
    }
    
    private String get(Iterable<String> iterable, int index) {
        int count = 0;
        for (String element : iterable) {
            if (count == index) {
                return element;
            }
            count++;
        }
        return null;
    }
}