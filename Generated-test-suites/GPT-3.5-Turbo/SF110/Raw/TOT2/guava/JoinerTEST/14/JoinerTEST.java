import static org.junit.Assert.*;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String result = joiner.join("a", null, "b", "c");
        assertEquals("a,null,b,c", result);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("a", null, "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String result = mapJoiner.join(map);
        assertEquals("key1=value1,key2=value2", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullException() {
        Joiner joiner = Joiner.on(",");
        joiner.useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsException() {
        Joiner joiner = Joiner.on(",");
        joiner.skipNulls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithKeyValueSeparatorException() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        mapJoiner.withKeyValueSeparator(":");
    }

}