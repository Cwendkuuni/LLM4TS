import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,orange");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnChar_EmptyString() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("");
        String[] expected = {""};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testOnChar_NullString() {
        Splitter splitter = Splitter.on(',');
        splitter.split(null);
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("apple,banana,orange");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnCharMatcher_EmptyString() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split("");
        String[] expected = {""};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testOnCharMatcher_NullString() {
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        splitter.split(null);
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("apple,banana,orange");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnString_EmptyString() {
        Splitter splitter = Splitter.on("");
        splitter.split("apple,banana,orange");
    }

    @Test(expected = NullPointerException.class)
    public void testOnString_NullString() {
        Splitter splitter = Splitter.on(",");
        splitter.split(null);
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[,;]");
        Iterable<String> result = splitter.split("apple,banana;orange");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testOnPattern_NullPattern() {
        Splitter splitter = Splitter.onPattern(null);
        splitter.split("apple,banana,orange");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnPattern_EmptyString() {
        Splitter splitter = Splitter.onPattern("");
        splitter.split("apple,banana,orange");
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("applebananaorange");
        String[] expected = {"app", "leb", "ana", "ora", "nge"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFixedLength_InvalidLength() {
        Splitter splitter = Splitter.fixedLength(0);
        splitter.split("apple,banana,orange");
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        splitter = splitter.omitEmptyStrings();
        Iterable<String> result = splitter.split("apple,,orange");
        String[] expected = {"apple", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",");
        splitter = splitter.limit(2);
        Iterable<String> result = splitter.split("apple,banana,orange");
        String[] expected = {"apple", "banana,orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimit_InvalidLimit() {
        Splitter splitter = Splitter.on(",");
        splitter = splitter.limit(0);
        splitter.split("apple,banana,orange");
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",");
        splitter = splitter.trimResults();
        Iterable<String> result = splitter.split(" apple , banana , orange ");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testTrimResults_CustomTrimmer() {
        Splitter splitter = Splitter.on(",");
        splitter = splitter.trimResults(CharMatcher.anyOf("[]"));
        Iterable<String> result = splitter.split("[apple],[banana],[orange]");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("apple,banana,orange");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testSplit_NullSequence() {
        Splitter splitter = Splitter.on(",");
        splitter.split(null);
    }

    @Test
    public void testSplitEmptyString() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("");
        String[] expected = {""};
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("apple,banana,orange");
        String[] expected = {"apple", "banana", "orange"};
        assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testSplitToList_NullSequence() {
        Splitter splitter = Splitter.on(",");
        splitter.splitToList(null);
    }

    @Test
    public void testWithKeyValueSeparator_String() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        String sequence = "key1:value1,key2:value2";
        Map<String, String> result = mapSplitter.split(sequence);
        assertTrue(result.containsKey("key1"));
        assertEquals("value1", result.get("key1"));
        assertTrue(result.containsKey("key2"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testWithKeyValueSeparator_Char() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator(':');
        String sequence = "key1:value1,key2:value2";
        Map<String, String> result = mapSplitter.split(sequence);
        assertTrue(result.containsKey("key1"));
        assertEquals("value1", result.get("key1"));
        assertTrue(result.containsKey("key2"));
        assertEquals("value2", result.get("key2"));
    }

    @Test(expected = NullPointerException.class)
    public void testWithKeyValueSeparator_NullSplitter() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator((Splitter) null);
        mapSplitter.split("key1:value1,key2:value2");
    }

}