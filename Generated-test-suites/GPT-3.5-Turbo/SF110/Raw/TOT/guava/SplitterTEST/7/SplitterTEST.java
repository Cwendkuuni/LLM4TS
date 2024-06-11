import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    // Test cases for on(char separator) method
    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(':');
        assertEquals("a", splitter.split("a:b:c").iterator().next());
        assertEquals("b", splitter.split("a:b:c").iterator().next());
        assertEquals("c", splitter.split("a:b:c").iterator().next());
    }

    // Test cases for on(CharMatcher separatorMatcher) method
    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        assertEquals("a", splitter.split("a,b,c").iterator().next());
        assertEquals("b", splitter.split("a,b,c").iterator().next());
        assertEquals("c", splitter.split("a,b,c").iterator().next());
    }

    // Test cases for on(String separator) method
    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(",");
        assertEquals("a", splitter.split("a,b,c").iterator().next());
        assertEquals("b", splitter.split("a,b,c").iterator().next());
        assertEquals("c", splitter.split("a,b,c").iterator().next());
    }

    // Test cases for on(Pattern separatorPattern) method
    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s"));
        assertEquals("a", splitter.split("a b c").iterator().next());
        assertEquals("b", splitter.split("a b c").iterator().next());
        assertEquals("c", splitter.split("a b c").iterator().next());
    }

    // Test cases for onPattern(String separatorPattern) method
    @Test
    public void testOnPatternStringSeparator() {
        Splitter splitter = Splitter.onPattern("\\s");
        assertEquals("a", splitter.split("a b c").iterator().next());
        assertEquals("b", splitter.split("a b c").iterator().next());
        assertEquals("c", splitter.split("a b c").iterator().next());
    }

    // Test cases for fixedLength(int length) method
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertEquals("ab", splitter.split("abc").iterator().next());
        assertEquals("bc", splitter.split("abc").iterator().next());
    }

    // Test cases for omitEmptyStrings() method
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        assertEquals("a", splitter.split("a,,b,c,,d").iterator().next());
        assertEquals("b", splitter.split("a,,b,c,,d").iterator().next());
        assertEquals("c", splitter.split("a,,b,c,,d").iterator().next());
        assertEquals("d", splitter.split("a,,b,c,,d").iterator().next());
    }

    // Test cases for limit(int limit) method
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        assertEquals("a", splitter.split("a,b,c").iterator().next());
        assertEquals("b,c", splitter.split("a,b,c").iterator().next());
        assertEquals("c", splitter.split("a,b,c").iterator().next());
    }

    // Test cases for trimResults() method
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        assertEquals("a", splitter.split(" a , ").iterator().next());
        assertEquals("b", splitter.split(" a , b ").iterator().next());
        assertEquals("c", splitter.split(" a , b , c ").iterator().next());
    }

    // Test cases for trimResults(CharMatcher trimmer) method
    @Test
    public void testTrimResultsWithCharMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is(' '));
        assertEquals("a", splitter.split(" a , ").iterator().next());
        assertEquals("b", splitter.split(" a , b ").iterator().next());
        assertEquals("c", splitter.split(" a , b , c ").iterator().next());
    }

    // Test cases for split(CharSequence sequence) method
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(":");
        assertEquals("a", splitter.split("a:b:c").iterator().next());
        assertEquals("b", splitter.split("a:b:c").iterator().next());
        assertEquals("c", splitter.split("a:b:c").iterator().next());
    }

    // Test cases for splitToList(CharSequence sequence) method
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        assertEquals("a", splitter.splitToList("a:b:c").get(0));
        assertEquals("b", splitter.splitToList("a:b:c").get(1));
        assertEquals("c", splitter.splitToList("a:b:c").get(2));
    }

    // Test cases for withKeyValueSeparator(String separator) method
    @Test
    public void testWithKeyValueSeparatorWithString() {
        Splitter splitter = Splitter.on(":").withKeyValueSeparator("=");
        assertEquals("a", splitter.split("key1=value1:key2=value2").get("key1"));
        assertEquals("value1", splitter.split("key1=value1:key2=value2").get("value1"));
        assertEquals("key2", splitter.split("key1=value1:key2=value2").get("key2"));
        assertEquals("value2", splitter.split("key1=value1:key2=value2").get("value2"));
    }

    // Test cases for withKeyValueSeparator(char separator) method
    @Test
    public void testWithKeyValueSeparatorWithChar() {
        Splitter splitter = Splitter.on(":").withKeyValueSeparator('=');
        assertEquals("a", splitter.split("key1=value1:key2=value2").get("key1"));
        assertEquals("value1", splitter.split("key1=value1:key2=value2").get("value1"));
        assertEquals("key2", splitter.split("key1=value1:key2=value2").get("key2"));
        assertEquals("value2", splitter.split("key1=value1:key2=value2").get("value2"));
    }

    // Test cases for withKeyValueSeparator(Splitter keyValueSplitter) method
    @Test
    public void testWithKeyValueSeparatorWithSplitter() {
        Splitter keyValueSplitter = Splitter.on("=").limit(2);
        Splitter splitter = Splitter.on(":").withKeyValueSeparator(keyValueSplitter);
        assertEquals("a", splitter.split("key1=value1:key2=value2").get("key1"));
        assertEquals("value1", splitter.split("key1=value1:key2=value2").get("value1"));
        assertEquals("key2", splitter.split("key1=value1:key2=value2").get("key2"));
        assertEquals("value2", splitter.split("key1=value1:key2=value2").get("value2"));
    }

    // Test inner class MapSplitter
    @Test
    public void testMapSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(":").withKeyValueSeparator("=");
        assertEquals("value1", mapSplitter.split("key1=value1:key2=value2").get("key1"));
        assertEquals("value2", mapSplitter.split("key1=value1:key2=value2").get("key2"));
    }

}