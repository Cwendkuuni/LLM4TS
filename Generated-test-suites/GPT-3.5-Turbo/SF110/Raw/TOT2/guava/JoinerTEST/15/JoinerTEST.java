import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('!');
        assertNotNull(joiner);
        assertEquals("!", joiner.separator);
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("apple", "banana", "orange");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("apple,banana,orange", result.toString());
    }

    // Additional test cases for other methods can be added here...

}