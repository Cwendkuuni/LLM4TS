import org.junit.Test;
import com.google.common.base.Splitter;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class SplitterTest {

    @Test
    public void testOn(char separator) {
        Splitter splitter = Splitter.on(separator);
        List<String> result = splitter.splitToList("Hello,World");
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
    }

    @Test
    public void testOn_CharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is(';'));
        List<String> result = splitter.splitToList("apple;banana;cherry");
        assertTrue(result.contains("apple"));
        assertTrue(result.contains("banana"));
        assertTrue(result.contains("cherry"));
    }

    @Test
    public void testOn_String() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("John,Doe");
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }

    @Test
    public void testOn_Pattern() {
        Splitter splitter = Splitter.onPattern("\\s");
        List<String> result = splitter.splitToList("Hello World");
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        List<String> result = splitter.splitToList("123456789");
        assertTrue(result.contains("123"));
        assertTrue(result.contains("456"));
        assertTrue(result.contains("789"));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        List<String> result = splitter.splitToList("apple,,banana,,cherry");
        assertEquals(result.size(), 3);
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        List<String> result = splitter.splitToList("apple,banana,cherry");
        assertEquals(result.size(), 2);
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        List<String> result = splitter.splitToList(" apple , banana , cherry ");
        assertTrue(result.contains("apple"));
        assertTrue(result.contains("banana"));
        assertTrue(result.contains("cherry"));
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("John,Doe");
        assertTrue(result.iterator().hasNext());
        assertEquals(result.iterator().next(), "John");
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("John,Doe");
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2");
        assertEquals(result.get("key1"), "value1");
        assertEquals(result.get("key2"), "value2");
    }

}