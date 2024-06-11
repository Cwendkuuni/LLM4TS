
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

public class JoinerTest {
    
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(',');
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
    }
    
    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(new Object[]{"a", "b", "c"}));
    }
    
    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join(Arrays.asList("a", null, "c")));
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "c"));
        assertEquals("a,c", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsUseForNull() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals("key1:value1,key2:value2", joiner.join(map));
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
}

