import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(";");
        assertEquals("a;b;c", joiner.join("a", "b", "c"));
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on('-');
        assertEquals("a-b-c", joiner.join("a", "b", "c"));
    }
    
    @Test
    public void testAppendToWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", sb.toString());
    }
    
    @Test
    public void testAppendToWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", sb.toString());
    }
    
    @Test
    public void testAppendToWithArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Object[] {"a", "b", "c"});
        assertEquals("a,b,c", sb.toString());
    }
    
    @Test
    public void testAppendToWithObjects() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "a", "b", "c");
        assertEquals("a,b,c", sb.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilder() {
        Joiner joiner = Joiner.on(",");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", sb.toString());
    }
    
    @Test
    public void testAppendToWithMapJoiner() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Collections.singletonMap("key", "value"));
        assertEquals("key:value", sb.toString());
    }
    
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
    }
    
    @Test
    public void testJoinWithArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(new Object[] {"a", "b", "c"}));
    }
    
    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }
    
    @Test
    public void testJoinWithMapJoiner() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        assertEquals("key:value", joiner.join(Collections.singletonMap("key", "value")));
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertEquals("a,null,c", joiner.join("a", null, "c"));
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("a,c", joiner.join("a", null, "c"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator(":");
    }
}