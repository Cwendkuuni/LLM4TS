import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on('?');
        assertNotNull(joiner);
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", sb.toString());
    }

    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = joiner.appendTo(new StringBuilder(), new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", sb.toString());
    }

    // Additional test cases for other methods can be added here

}