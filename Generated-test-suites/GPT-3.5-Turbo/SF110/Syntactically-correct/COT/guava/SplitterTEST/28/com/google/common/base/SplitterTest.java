
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Map;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');

        List<String> result = splitter.splitToList("a,b,c,d,e");

        assertEquals(5, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
        assertEquals("d", result.get(3));
        assertEquals("e", result.get(4));
    }

    @Test
    public void testOnCharMatcherSeparator() {
        Splitter splitter = Splitter.on(CharMatcher.is('|'));

        List<String> result = splitter.splitToList("hello|world|java");

        assertEquals(3, result.size());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
        assertEquals("java", result.get(2));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("::");

        List<String> result = splitter.splitToList("apple::banana::cherry");

        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));

        List<String> result = splitter.splitToList("hello     world  java");

        assertEquals(3, result.size());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
        assertEquals("java", result.get(2));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);

        List<String> result = splitter.splitToList("abcdefg");

        assertEquals(3, result.size());
        assertEquals("abc", result.get(0));
        assertEquals("def", result.get(1));
        assertEquals("g", result.get(2));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(':').omitEmptyStrings();

        List<String> result = splitter.splitToList(":apple::banana:cherry:");

        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(3);

        List<String> result = splitter.splitToList("a,b,c,d,e,f");

        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c,d,e,f", result.get(2));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();

        List<String> result = splitter.splitToList(" a , b , c , d , e ");

        assertEquals(5, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
        assertEquals("d", result.get(3));
        assertEquals("e", result.get(4));
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is('_'));

        List<String> result = splitter.splitToList("_a_,_b_");

        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(':');

        Iterable<String> result = splitter.split("apple:banana:cherry");

        assertNotNull(result);
        assertTrue(result instanceof Iterable);

        int count = 0;
        for (String str : result) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(':');

        List<String> result = splitter.splitToList("apple:banana:cherry");

        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("cherry", result.get(2));
    }

    @Test
    public void testWithKeyValueSeparatorOnCharSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').withKeyValueSeparator(',');

        Map<String, String> result = mapSplitter.split("key1,value1:key2,value2:key3,value3");

        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparatorOnStringSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on("->").withKeyValueSeparator(":");

        Map<String, String> result = mapSplitter.split("key1:value1->key2:value2->key3:value3");

        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    @Test
    public void testWithKeyValueSeparatorOnSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(':').trimResults().omitEmptyStrings().withKeyValueSeparator(Splitter.on(','));

        Map<String, String> result = mapSplitter.split("key1,value1:key2,value2:key3,value3");

        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }
}

