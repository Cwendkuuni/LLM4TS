import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testOnWithStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOnWithCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }
    
    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("a", "b", "c");
        
        assertEquals("a-b-c", joiner.appendTo(builder, parts).toString());
    }
    
    @Test
    public void testAppendToArray() {
        Joiner joiner = Joiner.on("/");
        StringBuilder builder = new StringBuilder();
        String[] parts = {"x", "y", "z"};
        
        assertEquals("x/y/z", joiner.appendTo(builder, parts).toString());
    }
    
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(";");
        Iterable<Integer> parts = Arrays.asList(1, 2, 3, 4);
        
        assertEquals("1;2;3;4", joiner.join(parts));
    }
    
    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on("*");
        Integer[] parts = {5, 6, 7};
        
        assertEquals("5*6*7", joiner.join(parts));
    }
    
    @Test(expected = IOException.class)
    public void testAppendToIOException() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> parts = Arrays.asList(1, 2).iterator();
        
        joiner.appendTo(builder, parts);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on("-");
        String nullText = "NA";
        Joiner newJoiner = joiner.useForNull(nullText);
        
        assertEquals("a-NA-c", newJoiner.join("a", null, "c"));
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("x", null, "z");
        
        assertEquals("x,z", joiner.skipNulls().appendTo(builder, parts).toString());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(":").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        
        assertEquals("key1:value1,key3:value3", joiner.withKeyValueSeparator("=").join(map));
    }
}