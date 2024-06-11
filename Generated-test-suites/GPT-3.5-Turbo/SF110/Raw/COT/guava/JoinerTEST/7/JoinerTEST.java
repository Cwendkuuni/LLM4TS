
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {
    
    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertEquals("joiner separator should be \",\"", ",", joiner.separator);
    }
    
    @Test
    public void testAppendTo_withIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("builder should contain \"a,b,c\"", "a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_withIterator() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("builder should contain \"a,b,c\"", "a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_withObjectArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[] {"a", "b", "c"});
        assertEquals("builder should contain \"a,b,c\"", "a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_withObjects() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("builder should contain \"a,b,c\"", "a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendTo_withNullObjects() throws Exception {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", null, "c");
        assertEquals("builder should contain \"a,null,c\"", "a,null,c", builder.toString());
    }
    
    @Test
    public void testJoin_withIterable() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(Arrays.asList("a", "b", "c"));
        assertEquals("joined string should be \"a,b,c\"", "a,b,c", joined);
    }
    
    @Test
    public void testJoin_withIterator() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(Arrays.asList("a", "b", "c").iterator());
        assertEquals("joined string should be \"a,b,c\"", "a,b,c", joined);
    }
    
    @Test
    public void testJoin_withObjectArray() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(new Object[] {"a", "b", "c"});
        assertEquals("joined string should be \"a,b,c\"", "a,b,c", joined);
    }
    
    @Test
    public void testJoin_withObjects() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join("a", "b", "c");
        assertEquals("joined string should be \"a,b,c\"", "a,b,c", joined);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", null, "c");
        assertEquals("builder should contain \"a,null,c\"", "a,null,c", builder.toString());
    }
    
    @Test
    public void testSkipNulls() throws Exception {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", null, "c");
        assertEquals("builder should contain \"a,c\"", "a,c", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").skipNulls().withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Collections.singletonMap("key", null));
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Collections.singletonMap("key", "value"));
        assertEquals("builder should contain \"key:value\"", "key:value", builder.toString());
    }
}
