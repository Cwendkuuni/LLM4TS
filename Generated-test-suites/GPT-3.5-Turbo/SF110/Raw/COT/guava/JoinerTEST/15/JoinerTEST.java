
import org.junit.Test;

import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {"hello", "world"};
        String result = joiner.join(parts);
        assertEquals("hello,world", result);
    }

    @Test
    public void testJoinWithNulls() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String[] parts = {"hello", null, "world"};
        String result = joiner.join(parts);
        assertEquals("hello,null,world", result);
    }

    @Test
    public void testJoinWithEmptyParts() {
        Joiner joiner = Joiner.on(",");
        String[] parts = {};
        String result = joiner.join(parts);
        assertEquals("", result);
    }

    @Test
    public void testJoinWithNullParts() {
        Joiner joiner = Joiner.on(",");
        String[] parts = null;
        String result = joiner.join(parts);
        assertEquals("", result);
    }

    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        String result = joiner.join(parts);
        assertEquals("hello,world", result);
    }

    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("hello", "world");
        String result = joiner.join(parts.iterator());
        assertEquals("hello,world", result);
    }

    @Test
    public void testJoinWithMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        String result = mapJoiner.join(map);
        assertEquals("one:1,two:2", result);
    }

    @Test
    public void testJoinWithMapEntries() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        String result = mapJoiner.join(map.entrySet());
        assertEquals("one:1,two:2", result);
    }
}

