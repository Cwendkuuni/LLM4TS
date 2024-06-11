import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    // Tests for the "on" method
    @Test
    public void testOnWithSeparatorString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOnWithSeparatorChar() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }
    
    // Tests for the "appendTo" method
    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("apple", "banana", "orange");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("apple,banana,orange", result.toString());
    }
    
    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        Iterator<String> iterator = list.iterator();
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = { "apple", "banana", "orange" };
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("apple,banana,orange", result.toString());
    }
    
    @Test
    public void testAppendToWithVarArgs() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "apple", "banana", "orange");
        assertEquals("apple,banana,orange", result.toString());
    }
    
    // Tests for the "join" method
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        Iterable<String> parts = Arrays.asList("apple", "banana", "orange");
        String result = joiner.join(parts);
        assertEquals("apple,banana,orange", result);
    }
    
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        Iterator<String> iterator = list.iterator();
        String result = joiner.join(iterator);
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = { "apple", "banana", "orange" };
        String result = joiner.join(parts);
        assertEquals("apple,banana,orange", result);
    }
    
    @Test
    public void testJoinWithVarArgs() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("apple", "banana", "orange");
        assertEquals("apple,banana,orange", result);
    }
    
    // Tests for the "useForNull" method
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner nullJoiner = joiner.useForNull("NULL");
        assertNotNull(nullJoiner);
    }
    
    // Tests for the "skipNulls" method
    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(null);
        list.add("world");
        Iterator<String> iterator = list.iterator();
        StringBuilder result = joiner.skipNulls().appendTo(builder, iterator);
        assertEquals("hello,world", result.toString());
    }
    
    // Tests for the "withKeyValueSeparator" method
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
    }

}