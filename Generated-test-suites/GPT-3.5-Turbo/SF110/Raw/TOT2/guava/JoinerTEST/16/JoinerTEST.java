import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("hello", "world");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("hello,world", result.toString());
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner newJoiner = joiner.useForNull("null");
        assertNotNull(newJoiner);
    }

    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {null, "hello", null, "world"};
        StringBuilder result = joiner.skipNulls().appendTo(builder, parts);
        assertEquals("hello,world", result.toString());
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}