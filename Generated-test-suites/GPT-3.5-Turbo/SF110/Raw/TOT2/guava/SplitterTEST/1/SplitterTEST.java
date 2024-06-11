import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals(3, Iterables.size(result));
        // Add more test cases for different scenarios
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on("|");
        Iterable<String> result = splitter.split("apple|banana|cherry");
        assertEquals(3, Iterables.size(result));
        // Add more test cases for different scenarios
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\d+");
        Iterable<String> result = splitter.split("apple123banana456cherry");
        assertEquals(3, Iterables.size(result));
        // Add more test cases for different scenarios
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(3);
        Iterable<String> result = splitter.split("applebananacherry");
        assertEquals(3, Iterables.size(result));
        // Add more test cases for different scenarios
    }

    // Add more test cases for other public methods and edge cases

}