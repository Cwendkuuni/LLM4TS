import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {
    
    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testAppendToIterable() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.appendTo(new StringBuilder(), Arrays.asList(1, 2, 3)).toString();
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testAppendToIterator() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.appendTo(new StringBuilder(), Arrays.asList(1, 2, 3).iterator()).toString();
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testAppendToArray() throws Exception {
        Joiner joiner = Joiner.on(",");
        String result = joiner.appendTo(new StringBuilder(), 1, 2, 3).toString();
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(1, 2, 3);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinWithNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join(Arrays.asList(1, null, 3));
        assertEquals("1,3", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String result = joiner.join(Arrays.asList(1, null, 3));
        assertEquals("1,null,3", result);
    }
    
    @Test
    public void testMapJoiner() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");
        String result = joiner.join(Arrays.asList(1, 2, 3), Arrays.asList("a", "b", "c"));
        assertEquals("1=a,2=b,3=c", result);
    }
    
    @Test
    public void testMapJoinerWithNulls() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=").skipNulls();
        String result = joiner.join(
            Arrays.asList(1, null, 3),
            Arrays.asList("a", null, "c")
        );
        assertEquals("1=a,3=c", result);
    }
    
    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("null");
        String result = joiner.join(
            Arrays.asList(1, null, 3),
            Arrays.asList("a", null, "c")
        );
        assertEquals("1=a,null=c", result);
    }
    
    // add more tests for other methods
    
}