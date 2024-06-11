import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
        assertEquals(";", joiner.separator);
    }
    
    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("one", "two", "three");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertNotNull(result);
        assertEquals("one,two,three", result.toString());
    }
    
    @Test
    public void testAppendToArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        String[] parts = {"four", "five", "six"};
        StringBuilder result = joiner.appendTo(builder, parts);
        assertNotNull(result);
        assertEquals("four,five,six", result.toString());
    }
    
    // Additional cases for edge testing and exceptions can be added here
    
}