import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner newJoiner = joiner.useForNull("NULL");
        assertNotNull(newJoiner);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner newJoiner = joiner.skipNulls();
        assertNotNull(newJoiner);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        assertNotNull(mapJoiner);
    }
    
    @Test
    public void testAppendToIteratable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        String result = joiner.appendTo(builder, Arrays.asList("apple", "banana", "cherry")).toString();
        assertEquals("apple,banana,cherry", result);
    }
    
    @Test
    public void testAppendToArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        String result = joiner.appendTo(builder, new String[]{"apple", "banana", "cherry"}).toString();
        assertEquals("apple,banana,cherry", result);
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("apple", "banana", "cherry"));
        assertEquals("apple,banana,cherry", result);
    }
    
    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new String[]{"apple", "banana", "cherry"});
        assertEquals("apple,banana,cherry", result);
    }
}