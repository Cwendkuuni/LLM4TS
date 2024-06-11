import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Splitter;

public class SplitterTest {
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.whitespace());
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(':'));
        assertNotNull(splitter);
        // Add more test cases here
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("a,b,c");
        assertNotNull(result);
        // Add more test cases here
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList("d,e,f");
        assertNotNull(result);
        // Add more test cases here
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator('=');
        assertNotNull(mapSplitter);
        // Add more test cases here
    }
}