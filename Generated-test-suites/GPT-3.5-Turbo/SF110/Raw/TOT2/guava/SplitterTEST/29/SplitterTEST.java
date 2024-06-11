import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class SplitterTest {
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        assertNotNull(splitter);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertNotNull(splitter);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',')
                            .omitEmptyStrings();
        assertNotNull(splitter);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',')
                            .limit(5);
        assertNotNull(splitter);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',')
                            .trimResults();
        assertNotNull(splitter);
    }
    
    @Test
    public void testTrimResultsWithCustomCharMatcher() {
        Splitter splitter = Splitter.on(',')
                            .trimResults(CharMatcher.WHITESPACE);
        assertNotNull(splitter);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        List<String> resultList = splitter.splitToList("a,b,c");
        assertEquals(3, resultList.size());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        MapSplitter mapSplitter = Splitter.on(',')
                                .withKeyValueSeparator('=');
        assertNotNull(mapSplitter);
    }
    
    @Test
    public void testMapSplitterSplit() {
        MapSplitter mapSplitter = Splitter.on(',')
                                .withKeyValueSeparator('=');
        Map<String, String> resultMap = mapSplitter.split("key1=value1,key2=value2");
        assertEquals(2, resultMap.size());
    }
    
}