import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,orange,banana");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("=");
        Iterable<String> result = splitter.split("key1=value1");
        assertEquals("value1", result.iterator().next());
    }

    @Test
    public void testOnPatternSeparator() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern);
        Iterable<String> result = splitter.split("hello world");
        assertEquals("world", result.iterator().next());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcdef");
        assertEquals("cd", result.iterator().next());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',');
        splitter = splitter.omitEmptyStrings();
        Iterable<String> result = splitter.split("apple,,banana");
        assertEquals(2, Lists.newArrayList(result).size());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',');
        splitter = splitter.limit(2);
        Iterable<String> result = splitter.split("apple,orange,banana");
        assertEquals(2, Lists.newArrayList(result).size());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" apple , orange , banana ");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(',').withKeyValueSeparator("=");
        Map<String, String> resultMap = mapSplitter.split("key1=value1,key2=value2");
        assertEquals("value1", resultMap.get("key1"));
    }
}