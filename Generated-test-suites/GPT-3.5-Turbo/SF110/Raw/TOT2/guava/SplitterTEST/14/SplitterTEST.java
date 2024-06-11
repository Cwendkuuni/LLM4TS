import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(':');
        Iterable<String> result = splitter.split("apple:orange:banana");
        assertEquals(Arrays.asList("apple", "orange", "banana"), result);
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("pineapple;grape;melon");
        assertEquals(Arrays.asList("pineapple", "grape", "melon"), result);
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Iterable<String> result = splitter.split("apple orange  banana");
        assertEquals(Arrays.asList("apple", "orange", "banana"), result);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcdef");
        assertEquals(Arrays.asList("ab", "cd", "ef"), result);
    }

    // Add more test cases for all public methods as needed

}