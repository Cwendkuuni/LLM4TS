import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.*;
import com.google.common.base.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("1,2,3");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("1,2,3");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("key:value");
        
        assertEquals("key", result.iterator().next());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("hello world");
        
        assertEquals("hello", result.iterator().next());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("1234");
        
        assertEquals("12", result.iterator().next());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("1,,3");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("1,2,3");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split("  1  ,  2  ,  3  ");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
        Iterable<String> result = splitter.split("  1  ,  2  ,  3  ");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',')
            .omitEmptyStrings()
            .trimResults();
        
        Iterable<String> result = splitter.split(" 1 , 2 , 3 ");
        
        assertEquals("1", result.iterator().next());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',')
            .omitEmptyStrings()
            .trimResults();
        
        List<String> result = splitter.splitToList(" 1 , 2 , 3 ");
        
        assertEquals("1", result.get(0));
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator('=');
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        
        assertEquals("value1", result.get("key1"));
    }
	
}