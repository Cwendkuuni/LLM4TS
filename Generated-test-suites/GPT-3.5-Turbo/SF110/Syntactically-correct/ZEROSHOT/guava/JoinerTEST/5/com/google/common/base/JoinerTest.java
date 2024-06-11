import static org.junit.Assert.*;

import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testJoin_withIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("Hello", "World"));
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withIterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("Hello", "World").iterator());
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new String[]{"Hello", "World"});
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withObjects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("Hello", "World");
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("Null");
        String result = joiner.join("Hello", null, "World");
        assertEquals("Hello,Null,World", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("Hello", null, "World");
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        String result = mapJoiner.join(Collections.singletonMap("key", "value"));
        assertEquals("key=value", result);
    }

}