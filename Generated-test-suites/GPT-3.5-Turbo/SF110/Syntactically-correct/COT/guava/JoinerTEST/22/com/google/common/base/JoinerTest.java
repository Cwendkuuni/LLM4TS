import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    
    @Test
    public void testOn() {
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
        List<String> parts = Arrays.asList("hello", "world");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        Iterator<String> iterator = parts.iterator();
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object first = "hello";
        Object second = "world";
        Object[] rest = {"goodbye"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        assertEquals("hello,world,goodbye", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilder() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilderWithIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        Iterator<String> iterator = parts.iterator();
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilderWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"hello", "world"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilderWithObjects() {
        Joiner joiner = Joiner.on(",");
        Object first = "hello";
        Object second = "world";
        Object[] rest = {"goodbye"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        assertEquals("hello,world,goodbye", result.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        String result = joiner.join(parts);
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        Iterator<String> iterator = parts.iterator();
        String result = joiner.join(iterator);
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"hello", "world"};
        String result = joiner.join(parts);
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        Object first = "hello";
        Object second = "world";
        Object[] rest = {"goodbye"};
        String result = joiner.join(first, second, rest);
        assertEquals("hello,world,goodbye", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",")
            .useForNull("null");
        Object[] parts = {"hello", null, "world"};
        String result = joiner.join(parts);
        assertEquals("hello,null,world", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",")
            .skipNulls();
        Object[] parts = {"hello", null, "world"};
        String result = joiner.join(parts);
        assertEquals("hello,world", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        Joiner joiner = Joiner.on(",")
            .skipNulls()
            .useForNull("null");
        // Exception should be thrown due to using both skipNulls() and useForNull()
        Object[] parts = {"hello", null, "world"};
        String result = joiner.join(parts);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",")
            .withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", "30");
        String result = joiner.join(map);
        assertEquals("name=John,age=30", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() {
        Joiner joiner = Joiner.on(",")
            .skipNulls()
            .withKeyValueSeparator("=");
        // Exception should be thrown due to using skipNulls() with MapJoiner
        Map<String, String> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", null);
        String result = joiner.join(map);
    }
}