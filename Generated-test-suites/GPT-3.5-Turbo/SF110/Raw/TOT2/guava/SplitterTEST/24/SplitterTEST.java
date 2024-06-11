import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("apple,banana,cherry");
        assertEquals("apple", result.iterator().next());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(";");
        Iterable<String> result = splitter.split("apple;banana;cherry");
        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s).append(",");
        }
        assertEquals("apple,banana,cherry,", sb.toString());
    }

    @Test
    public void testOnPattern() {
        Pattern pattern = Pattern.compile("\\s+");
        Splitter splitter = Splitter.on(pattern);
        Iterable<String> result = splitter.split("apple banana cherry");
        int count = 0;
        for (String s : result) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcdef");
        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s).append(",");
        }
        assertEquals("ab,cd,ef,", sb.toString());
    }

    // Additional test cases for edge cases and exceptions can be added here

}