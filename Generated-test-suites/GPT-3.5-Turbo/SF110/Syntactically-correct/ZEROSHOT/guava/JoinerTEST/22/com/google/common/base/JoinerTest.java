import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    @Test
    public void testJoin_withIterable_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("Hello", "World", "!");
        String result = joiner.join(parts);
        assertEquals("Hello,World,!", result);
    }

    @Test
    public void testJoin_withIterator_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on(" ");
        List<String> parts = Arrays.asList("Hello", "World", "!");
        String result = joiner.join(parts.iterator());
        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoin_withArray_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on(" ");
        String[] parts = {"Hello", "World", "!"};
        String result = joiner.join(parts);
        assertEquals("Hello World !", result);
    }

    @Test
    public void testJoin_withObjects_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on(", ");
        String result = joiner.join("Hello", "World", "!");
        assertEquals("Hello, World, !", result);
    }

    @Test
    public void testJoin_withNullObjects_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("Hello", null, "World");
        assertEquals("Hello,World", result);
    }

    @Test
    public void testAppendTo_withIterable_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("Hello", "World", "!");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("Hello,World,!", result.toString());
    }

    @Test
    public void testAppendTo_withIterator_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("Hello", "World", "!");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts.iterator());
        assertEquals("Hello,World,!", result.toString());
    }

    @Test
    public void testAppendTo_withArray_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"Hello", "World", "!"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("Hello,World,!", result.toString());
    }

    @Test
    public void testAppendTo_withObjects_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "Hello", "World", "!");
        assertEquals("Hello,World,!", result.toString());
    }
    
    // Additional tests can be added here

}