import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
        assertEquals(3, splitter.split("a,b,c").spliterator().estimateSize());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
        assertEquals(2, splitter.split("hello;world").spliterator().estimateSize());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
        assertEquals(4, splitter.split("hello  world   java    ").spliterator().estimateSize());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        assertNotNull(splitter);
        assertEquals(5, splitter.split("abcdefg").spliterator().estimateSize());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
        assertEquals(2, splitter.split("a,,b,c").spliterator().estimateSize());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        assertNotNull(splitter);
        assertEquals(2, splitter.split("a,b,c").spliterator().estimateSize());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
        assertEquals("a", splitter.split(" a, b, c").iterator().next());
    }

    @Test
    public void testSplitToList() {
        List<String> list = Splitter.on(',').splitToList("a,b,c");
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals("b", list.get(1));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',')
                .withKeyValueSeparator(':');
        assertNotNull(mapSplitter);
        Map<String, String> map = mapSplitter.split("key1:val1,key2:val2");
        assertNotNull(map);
        assertEquals(2, map.size());
        assertTrue(map.containsKey("key1"));
        assertEquals("val2", map.get("key2"));
    }

    @Test
    public void testWithInvalidEntry() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',')
                .withKeyValueSeparator(':');
        Map<String, String> map = mapSplitter.split("key1:val1,key2val2");
        assertEquals(1, map.size());
    }
}