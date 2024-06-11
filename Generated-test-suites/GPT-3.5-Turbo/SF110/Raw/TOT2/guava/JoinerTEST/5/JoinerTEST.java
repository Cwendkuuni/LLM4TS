import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class JoinerTest {
    
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
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
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"a", "b", "c"};
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("a,b,c", result.toString());
    }
    
    // Add more test cases for other methods
    
}