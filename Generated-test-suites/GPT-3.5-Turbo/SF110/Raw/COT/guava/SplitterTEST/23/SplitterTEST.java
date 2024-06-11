
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class SplitterTest {
    
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(':');
        List<String> result = splitter.splitToList("one:two:three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        List<String> result = splitter.splitToList("one,two;three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("::");
        List<String> result = splitter.splitToList("one::two::three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        List<String> result = splitter.splitToList("one two   three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        List<String> result = splitter.splitToList("abcdef");
        
        // Assert
        // ...
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(":").omitEmptyStrings();
        List<String> result = splitter.splitToList("one::two:::three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        List<String> result = splitter.splitToList("one:two:three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(":").trimResults();
        List<String> result = splitter.splitToList("  one  :  two  :  three  ");
        
        // Assert
        // ...
    }
    
    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(":").trimResults(CharMatcher.is('_'));
        List<String> result = splitter.splitToList("__one__:__two__:__three__");
        
        // Assert
        // ...
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        Iterable<String> result = splitter.split("one:two:three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        List<String> result = splitter.splitToList("one:two:three");
        
        // Assert
        // ...
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
        
        // Assert
        // ...
    }
}
