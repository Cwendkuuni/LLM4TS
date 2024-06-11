import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(' ');
        assertEquals("[Hello, world!]", splitter.split("Hello world!").toString());
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.whitespace());
        assertEquals("[Hello, world!]", splitter.split("Hello world!").toString());
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        assertEquals("[Hello, world!]", splitter.split("Hello,world!").toString());
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        assertEquals("[Hello, world!]", splitter.split("Hello     world!").toString());
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(5);
        assertEquals("[Hello, world!]", splitter.split("Hello world!").toString());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(' ').omitEmptyStrings();
        assertEquals("[Hello, world!]", splitter.split("Hello  world!").toString());
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(' ').limit(2);
        assertEquals("[Hello, world!]", splitter.split("Hello  world!").toString());
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertEquals("[Hello, world!]", splitter.split("Hello , world!").toString());
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is('!'));
        assertEquals("[Hello, world]", splitter.split("Hello , world!").toString());
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        assertEquals("[Hello, world!]", splitter.split("Hello,world!").toString());
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        assertEquals("[Hello, world!]", splitter.splitToList("Hello,world!").toString());
    }
    
    @Test
    public void testWithKeyValueSeparatorOnChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        assertEquals("{Hello= world!}", mapSplitter.split("Hello: world!").toString());
    }
    
    @Test
    public void testWithKeyValueSeparatorOnString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator("=");
        assertEquals("{Hello= world!}", mapSplitter.split("Hello= world!").toString());
    }
    
    @Test
    public void testWithKeyValueSeparatorOnSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(Splitter.on('='));
        assertEquals("{Hello= world!}", mapSplitter.split("Hello= world!").toString());
    }
}