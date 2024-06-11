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
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
        assertEquals(";", joiner.separator);
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(new StringBuilder(), Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", result.toString());
    }
    
    // More test cases for other methods can be added here

}