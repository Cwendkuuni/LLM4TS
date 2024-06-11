import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn_StringSeparator() {
        Joiner joiner = Joiner.on("-");
        assertNotNull(joiner);
    }

    @Test
    public void testOn_CharSeparator() {
        Joiner joiner = Joiner.on('-');
        assertNotNull(joiner);
    }

    @Test
    public void testAppendTo_Iterable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("one", "two", "three");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("one-two-three", result.toString());
    }

    // Additional edge case and exception handling scenarios testing can be added here

    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"a", "b", "c"};
        String result = joiner.join(parts);
        assertEquals("a,b,c", result);
    }

    // Additional edge case and exception handling scenarios testing can be added here

    // More test methods can be added for other public methods

}