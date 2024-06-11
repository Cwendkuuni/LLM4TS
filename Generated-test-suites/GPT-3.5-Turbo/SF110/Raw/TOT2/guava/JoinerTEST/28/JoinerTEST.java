import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        assertEquals("x;y;z", joiner.join("x", "y", "z"));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        assertEquals("a,NA,b,NA,c", joiner.join("a", null, "b", null, "c"));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("a,b,c", joiner.join("a", null, "b", null, "c"));
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");
        joiner.appendTo(sb, iterable);
        assertEquals("apple,banana,cherry", sb.toString());
    }

    @Test
    public void testAppendToIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("dog", "cat", "bird").iterator();
        joiner.appendTo(sb, iterator);
        assertEquals("dog,cat,bird", sb.toString());
    }

    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        Object[] array = {"red", "green", "blue"};
        joiner.appendTo(sb, array);
        assertEquals("red,green,blue", sb.toString());
    }

    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(1, 2, 3));
    }

    @Test
    public void testJoinWithArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("x,y,z", joiner.join(new String[]{"x", "y", "z"}));
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        assertEquals("key1=value1,key2=value2,key3=value3", mapJoiner.join(map));
    }

}