import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on("-");
        assertEquals("a-b-c", joiner.join("a", "b", "c"));
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on('-');
        assertEquals("a-b-c", joiner.join("a", "b", "c"));
    }

    @Test
    public void testAppendToStringBuilder() {
        StringBuilder builder = new StringBuilder();
        Joiner joiner = Joiner.on("-");
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a-b-c", builder.toString());
    }

    @Test
    public void testAppendToAppendable() {
        StringBuilder builder = new StringBuilder();
        Appendable appendable = builder;
        Joiner joiner = Joiner.on("-");
        try {
            joiner.appendTo(appendable, "a", "b", "c");
        } catch (Exception e) {
            fail("An exception occurred");
        }
        assertEquals("a-b-c", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on("-");
        Iterable<String> iterable = Arrays.asList("a", "b", "c");
        assertEquals("a-b-c", joiner.join(iterable));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on("-");
        Iterator<String> iterator = Arrays.asList("a", "b", "c").iterator();
        assertEquals("a-b-c", joiner.join(iterator));
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on("-");
        String[] array = {"a", "b", "c"};
        assertEquals("a-b-c", joiner.join(array));
    }

    @Test
    public void testJoinWithNulls() {
        Joiner joiner = Joiner.on("-").useForNull("null");
        assertEquals("a-null-c", joiner.join("a", null, "c"));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on("-").skipNulls();
        assertEquals("a-c", joiner.join("a", null, "c"));
    }

    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals("key1:value1,key2:value2", joiner.join(map));
    }
}