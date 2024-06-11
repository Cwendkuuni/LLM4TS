import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SplitterTest {
    
    @Test
    public void testSplitterOnChar() {
        Splitter splitter = Splitter.on(',');
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo,bar,baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.splitToList("foo, bar, baz"));
        assertEquals(Arrays.asList("foo", "", "baz"), splitter.splitToList("foo,,baz"));
        assertEquals(Arrays.asList("", " bar", " baz"), splitter.splitToList(", bar, baz"));
    }
    
    @Test
    public void testSplitterOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf("-|"));
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo|bar-baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.splitToList("foo|- bar|- baz"));
        assertEquals(Arrays.asList("foo", " baz"), splitter.splitToList("foo|-|-| baz"));
        assertEquals(Arrays.asList("", " bar", " baz"), splitter.splitToList("|-| bar|-| baz"));
    }
    
    @Test
    public void testSplitterOnString() {
        Splitter splitter = Splitter.on(";");
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo;bar;baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.splitToList("foo; bar; baz"));
        assertEquals(Arrays.asList("foo", " baz"), splitter.splitToList("foo;; baz"));
        assertEquals(Arrays.asList("", " bar", " baz"), splitter.splitToList("; bar; baz"));
    }
    
    @Test
    public void testSplitterOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo bar baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.splitToList("foo   bar   baz"));
        assertEquals(Arrays.asList("foo", " baz"), splitter.splitToList("foo   bar   baz"));
        assertEquals(Arrays.asList("", " bar", " baz"), splitter.splitToList("   bar   baz"));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertEquals(Arrays.asList("fo", "ob", "ar", "ba", "z"), splitter.splitToList("foobarbaz"));
        assertEquals(Arrays.asList("fo", "o ", "ba", "r ", "ba", "z"), splitter.splitToList("fo o bar baz"));
        assertEquals(Arrays.asList("fo", " b", "az"), splitter.splitToList("fo ba z"));
        assertEquals(Arrays.asList(" b", "ar", " b", "az"), splitter.splitToList(" bar baz"));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo,bar,baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.splitToList("foo,bar,,baz,,"));
        assertEquals(Arrays.asList("foo", "baz"), splitter.splitToList(",,,foo,baz,"));
        assertEquals(Arrays.asList(" bar", " baz"), splitter.splitToList(",, , bar , baz"));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        assertEquals(Arrays.asList("foo", "bar,baz"), splitter.splitToList("foo,bar,baz"));
        assertEquals(Arrays.asList("foo", " bar,baz"), splitter.splitToList("foo, bar, baz"));
        assertEquals(Arrays.asList("foo", "baz"), splitter.splitToList("foo, baz"));
        assertEquals(Arrays.asList(" bar", " baz"), splitter.splitToList(" bar, baz"));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo, bar, baz"));
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList(" foo , bar , baz "));
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("  foo  ,  bar  ,  baz  "));
        assertEquals(Arrays.asList(" bar", " baz"), splitter.splitToList("   bar   ,   baz   "));
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(',');
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.split("foo,bar,baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.split("foo, bar, baz"));
        assertEquals(Arrays.asList("foo", " baz"), splitter.split("foo, baz"));
        assertEquals(Arrays.asList(" bar", " baz"), splitter.split(" bar, baz"));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        assertEquals(Arrays.asList("foo", "bar", "baz"), splitter.splitToList("foo,bar,baz"));
        assertEquals(Arrays.asList("foo", " bar", " baz"), splitter.splitToList("foo, bar, baz"));
        assertEquals(Arrays.asList("foo", " baz"), splitter.splitToList("foo, baz"));
        assertEquals(Arrays.asList(" bar", " baz"), splitter.splitToList(" bar, baz"));
    }
    
    @Test
    public void testWithKeyValueSeparatorOnChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator('=');
        Map<String, String> map = mapSplitter.split("key1=value1:key2=value2:key3=value3");
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        assertEquals("value3", map.get("key3"));
    }
    
    @Test
    public void testWithKeyValueSeparatorOnString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator("=");
        Map<String, String> map = mapSplitter.split("key1=value1:key2=value2:key3=value3");
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        assertEquals("value3", map.get("key3"));
    }
    
    @Test
    public void testWithKeyValueSeparatorOnSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(Splitter.on('='));
        Map<String, String> map = mapSplitter.split("key1=value1:key2=value2:key3=value3");
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        assertEquals("value3", map.get("key3"));
    }
}