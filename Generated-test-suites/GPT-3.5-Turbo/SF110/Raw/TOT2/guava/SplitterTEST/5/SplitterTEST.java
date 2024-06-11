import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.*;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertEquals(3, Iterables.size(result));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("=");
        Iterable<String> result = splitter.split("key1=value1;key2=value2");
        assertEquals(2, Iterables.size(result));
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Iterable<String> result = splitter.split("hello    world");
        assertEquals(2, Iterables.size(result));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("123456");
        assertEquals(3, Iterables.size(result));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("apple, ,banana ,,orange");
        assertEquals(3, Iterables.size(result));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split("apple,banana,orange");
        assertEquals(2, Iterables.size(result));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(" apple , banana , orange ");
        assertEquals("apple", Iterables.get(result, 0));
        assertEquals("banana", Iterables.get(result, 1));
        assertEquals("orange", Iterables.get(result, 2));
    }

    // Add more test cases for other methods as needed

}