import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOnSeparator() {
        Joiner joiner = Joiner.on(",");

        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("1.1,2.2,3.3", joiner.join(Arrays.asList(1.1, 2.2, 3.3)));
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(',');

        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("1.1,2.2,3.3", joiner.join(Arrays.asList(1.1, 2.2, 3.3)));
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", stringBuilder.toString());

    }

    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendToArray() throws IOException {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, new Object[]{1, 2, 3});
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendToObjectVariadic() throws IOException {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, 1, 2, 3);
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderArray() {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, new Object[]{1, 2, 3});
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendToStringBuilderObjectVariadic() {
        Joiner joiner = Joiner.on(",");

        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, 1, 2, 3);
        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");

        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
        assertEquals("1.1,2.2,3.3", joiner.join(Arrays.asList(1.1, 2.2, 3.3)));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");

        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3).iterator()));
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
        assertEquals("1.1,2.2,3.3", joiner.join(Arrays.asList(1.1, 2.2, 3.3).iterator()));
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");

        assertEquals("1,2,3", joiner.join(new Object[]{1, 2, 3}));
        assertEquals("a,b,c", joiner.join(new Object[]{"a", "b", "c"}));
        assertEquals("1.1,2.2,3.3", joiner.join(new Object[]{1.1, 2.2, 3.3}));
    }

    @Test
    public void testJoinVariadic() {
        Joiner joiner = Joiner.on(",");

        assertEquals("1,2,3", joiner.join(1, 2, 3));
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
        assertEquals("1.1,2.2,3.3", joiner.join(1.1, 2.2, 3.3));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");

        assertEquals("1,null,3", joiner.join(Arrays.asList(1, null, 3)));
        assertEquals("a,null,c", joiner.join(Arrays.asList("a", null, "c")));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();

        assertEquals("1,3", joiner.join(Arrays.asList(1, null, 3)));
        assertEquals("a,c", joiner.join(Arrays.asList("a", null, "c")));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsUseForNull() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");

        joiner.join(Arrays.asList(1, null, 3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").skipNulls();

        joiner.withKeyValueSeparator(":");
    }

    @Test
    public void testMapJoinerAppendTo() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");

        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, Map.of("key1", "value1", "key2", "value2"));
        assertEquals("key1:value1,key2:value2", stringBuilder.toString());
    }

    @Test
    public void testMapJoinerAppendTo2() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");

        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, Map.ofEntries(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2"),
                Map.entry("key3", "value3")
        ));
        assertEquals("key1:value1,key2:value2,key3:value3", stringBuilder.toString());
    }

    @Test
    public void testMapJoinerJoin() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");

        assertEquals("key1:value1,key2:value2", mapJoiner.join(Map.of("key1", "value1", "key2", "value2")));
        assertEquals("key1:value1,key2:value2,key3:value3", mapJoiner.join(Map.ofEntries(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2"),
                Map.entry("key3", "value3")
        )));
    }

    @Test
    public void testMapJoinerAppendToStringBuilder() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");

        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, Map.of("key1", "value1", "key2", "value2"));
        assertEquals("key1:value1,key2:value2", stringBuilder.toString());
    }

    @Test
    public void testMapJoinerAppendToStringBuilder2() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");

        StringBuilder stringBuilder = new StringBuilder();
        mapJoiner.appendTo(stringBuilder, Map.ofEntries(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2"),
                Map.entry("key3", "value3")
        ));
        assertEquals("key1:value1,key2:value2,key3:value3", stringBuilder.toString());
    }

    @Test
    public void testMapJoinerJoinToStringBuilder() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");

        assertEquals("key1:value1,key2:value2", mapJoiner.join(Map.of("key1", "value1", "key2", "value2")));
        assertEquals("key1:value1,key2:value2,key3:value3", mapJoiner.join(Map.ofEntries(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2"),
                Map.entry("key3", "value3")
        )));
    }

}