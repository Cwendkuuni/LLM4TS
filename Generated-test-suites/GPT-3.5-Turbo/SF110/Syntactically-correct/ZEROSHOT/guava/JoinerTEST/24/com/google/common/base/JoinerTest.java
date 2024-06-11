import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    
    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testOnString() {
        Joiner result = Joiner.on(",");
        assertEquals(",", result.separator);
    }
    
    @Test
    public void testOnChar() {
        Joiner result = Joiner.on(',');
        assertEquals(",", result.separator);
    }
    
    @Test
    public void testAppendToWithIterable() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("hello", "world"));
        assertEquals("hello,world", builder.toString());
    }
    
    @Test
    public void testAppendToWithIterator() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("hello", "world").iterator());
        assertEquals("hello,world", builder.toString());
    }
    
    @Test
    public void testAppendToWithObjectArray() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"hello", "world"});
        assertEquals("hello,world", builder.toString());
    }
    
    @Test
    public void testAppendToWithObjects() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello", "world", "!");
        assertEquals("hello,world,!", builder.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndIterable() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, Arrays.asList("hello", "world"));
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndIterator() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, Arrays.asList("hello", "world").iterator());
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndObjectArray() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, new Object[]{"hello", "world"});
        assertEquals("hello,world", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndObjects() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "hello", "world", "!");
        assertEquals("hello,world,!", result.toString());
    }
    
    @Test
    public void testJoinWithIterable() {
        String result = joiner.join(Arrays.asList("hello", "world"));
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinWithIterator() {
        String result = joiner.join(Arrays.asList("hello", "world").iterator());
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinWithObjectArray() {
        String result = joiner.join(new Object[]{"hello", "world"});
        assertEquals("hello,world", result);
    }
    
    @Test
    public void testJoinWithObjects() {
        String result = joiner.join("hello", "world", "!");
        assertEquals("hello,world,!", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner result = joiner.useForNull("null");
        assertEquals("null", result.toString(null));
        assertEquals("hello", result.toString("hello"));
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner result = joiner.skipNulls();
        StringBuilder builder = new StringBuilder();
        result.appendTo(builder, Arrays.asList("hello", null, "world"));
        assertEquals("hello,world", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAlreadySpecified() {
        joiner.useForNull("null").useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsAlreadySpecified() {
        joiner.skipNulls().skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMaps() {
        Joiner result = joiner.skipNulls();
        result.withKeyValueSeparator(":");
    }
    
    @Test
    public void testMapJoinerWithAppendableAndMap() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        mapJoiner.appendTo(builder, map);
        assertEquals("hello:world", builder.toString());
    }
    
    @Test
    public void testMapJoinerWithStringBuilderAndMap() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        StringBuilder result = mapJoiner.appendTo(builder, map);
        assertEquals("hello:world", result.toString());
    }
    
    @Test
    public void testMapJoinerWithIterableAndMap() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        String result = mapJoiner.join(map.entrySet());
        assertEquals("hello:world", result);
    }
    
    @Test
    public void testMapJoinerWithIteratorAndMap() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        String result = mapJoiner.join(map.entrySet().iterator());
        assertEquals("hello:world", result);
    }
    
    @Test
    public void testMapJoinerUseForNull() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Joiner.MapJoiner result = mapJoiner.useForNull("null");
        assertEquals("null", result.join(Collections.singletonMap("hello", null)));
        assertEquals("world", result.join(Collections.singletonMap("hello", "world")));
    }

}