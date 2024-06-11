
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JoinerTest {
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(", ");
        assertEquals("hello, world", joiner.join("hello", "world"));
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        assertEquals("hello,world", joiner.join("hello", "world"));
    }
    
    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("hello", "world");
        joiner.appendTo(builder, iterable);
        assertEquals("hello, world", builder.toString());
    }
    
    @Test
    public void testAppendToIterator() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("hello", "world").iterator();
        joiner.appendTo(builder, iterator);
        assertEquals("hello, world", builder.toString());
    }
    
    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Object[] array = {"hello", "world"};
        joiner.appendTo(builder, array);
        assertEquals("hello, world", builder.toString());
    }
    
    @Test
    public void testAppendToObjectsWithRest() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "hello", "world", "!!");
        assertEquals("hello, world, !!", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("hello", "world");
        StringBuilder result = joiner.appendTo(builder, iterable);
        assertEquals("hello, world", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("hello", "world").iterator();
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("hello, world", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        Object[] array = {"hello", "world"};
        StringBuilder result = joiner.appendTo(builder, array);
        assertEquals("hello, world", result.toString());
    }
    
    @Test
    public void testAppendToStringBuilderWithRest() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "hello", "world", "!!");
        assertEquals("hello, world, !!", result.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(", ");
        Iterable<String> iterable = Arrays.asList("hello", "world");
        assertEquals("hello, world", joiner.join(iterable));
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(", ");
        Iterator<String> iterator = Arrays.asList("hello", "world").iterator();
        assertEquals("hello, world", joiner.join(iterator));
    }
    
    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(", ");
        Object[] array = {"hello", "world"};
        assertEquals("hello, world", joiner.join(array));
    }
    
    @Test
    public void testJoinWithRest() {
        Joiner joiner = Joiner.on(", ");
        assertEquals("hello, world, !!", joiner.join("hello", "world", "!!"));
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(", ").useForNull("null");
        assertEquals("hello, null, world", joiner.join("hello", null, "world"));
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(", ").skipNulls();
        assertEquals("hello, world", joiner.join("hello", null, "world"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        Joiner joiner = Joiner.on(", ").skipNulls().useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(", ").skipNulls().withKeyValueSeparator(":");
    }
    
    @Test
    public void testMapJoinerAppendToWithMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
        mapJoiner.appendTo(builder, map);
        assertEquals("one:1, two:2, three:3", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinWithMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
        Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
        assertEquals("one:1, two:2, three:3", mapJoiner.join(map));
    }
    
    @Test
    public void testMapJoinerUseForNull() {
        Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":").useForNull("null");
        Map<String, Integer> map = Map.of("one", 1, "two", null, "three", 3);
        assertEquals("one:1, two:null, three:3", mapJoiner.join(map));
    }
}
