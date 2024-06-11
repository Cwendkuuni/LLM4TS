import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Iterator;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("a", "b", "c");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<String> parts = Arrays.asList("a", "b", "c").iterator();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void testJoiningArray() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"a", "b", "c"};
        String result = joiner.join(parts);
        assertEquals("a,b,c", result);
    }

    // Add more test cases for other methods as needed

}