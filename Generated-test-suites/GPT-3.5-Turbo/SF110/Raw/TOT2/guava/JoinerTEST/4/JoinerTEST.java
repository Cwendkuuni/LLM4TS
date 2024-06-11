import org.junit.*;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder result = new StringBuilder();
        Iterable<String> parts = Arrays.asList("one", "two", "three");
        
        try {
            joiner.appendTo(result, parts);
        } catch (IOException e) {
            fail("IOException should not be thrown here.");
        }
        
        assertEquals("one,two,three", result.toString());
    }
}