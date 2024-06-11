import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(new StringBuilder(), new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", result.toString());
    }

    // Add more test cases for other methods

}