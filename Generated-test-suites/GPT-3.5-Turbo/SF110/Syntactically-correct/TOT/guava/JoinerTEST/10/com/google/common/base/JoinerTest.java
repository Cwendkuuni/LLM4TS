import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn_Separator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testOn_charSeparator() {
        Joiner joiner = Joiner.on(':');
        assertNotNull(joiner);
        assertEquals(":", joiner.separator);
    }

    @Test
    public void testAppendTo_withIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_withIterator() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_withObjectArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_withObjects() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilder() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testJoin_withIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", result);
    }

    @Test
    public void testJoin_withIterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", result);
    }

    @Test
    public void testJoin_withObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", result);
    }

    @Test
    public void testJoin_withObjects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    public void testUseForNull() throws Exception {
        Joiner joiner = Joiner.on(",").useForNull("null");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", null, "c"});
        assertEquals("a,null,c", builder.toString());
    }

    @Test
    public void testSkipNulls() throws Exception {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", null, "c"});
        assertEquals("a,c", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_twice() throws Exception {
        Joiner joiner = Joiner.on(",").useForNull("null").useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_twice() throws Exception {
        Joiner joiner = Joiner.on(",").skipNulls().skipNulls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMaps() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
    }

    @Test
    public void testMapJoiner() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Map.of("key1", "value1", "key2", "value2"));
        assertEquals("key1:value1,key2:value2", builder.toString());
    }

    @Test
    public void testMapJoiner_useForNull() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":").useForNull("null");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Map.of("key1", "value1", "key2", null));
        assertEquals("key1:value1,key2:null", builder.toString());
    }

    @Test
    public void testMapJoiner_appendTo_withIterable() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2")
        ));
        assertEquals("key1:value1,key2:value2", builder.toString());
    }

    @Test
    public void testMapJoiner_appendTo_withIterator() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2")
        ).iterator());
        assertEquals("key1:value1,key2:value2", builder.toString());
    }

    @Test
    public void testMapJoiner_join_withIterable() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        String result = mapJoiner.join(Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2")
        ));
        assertEquals("key1:value1,key2:value2", result);
    }

    @Test
    public void testMapJoiner_join_withIterator() throws Exception {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        String result = mapJoiner.join(Arrays.asList(
                Map.entry("key1", "value1"),
                Map.entry("key2", "value2")
        ).iterator());
        assertEquals("key1:value1,key2:value2", result);
    }
    
}