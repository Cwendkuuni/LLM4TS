import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testAppendTo() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "A", "B", "C");
        assertEquals("A,B,C", result.toString());
    }
    
    @Test
    public void testAppendTo_withIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("A", "B", "C");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("A,B,C", result.toString());
    }
    
    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("A", "B", "C");
        assertEquals("A,B,C", result);
    }
    
    @Test
    public void testJoin_withIterable() {
        Joiner joiner = Joiner.on(",");
        Iterable<String> parts = Arrays.asList("A", "B", "C");
        String result = joiner.join(parts);
        assertEquals("A,B,C", result);
    }
    
    @Test
    public void testJoin_withObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {"A", "B", "C"};
        String result = joiner.join(parts);
        assertEquals("A,B,C", result);
    }
    
    @Test
    public void testJoin_withVariousTypes() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("A", 1, 3.14, true);
        assertEquals("A,1,3.14,true", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String result = joiner.join("A", null, "C");
        assertEquals("A,NULL,C", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("A", null, "C");
        assertEquals("A,C", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_afterSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("NULL");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        mapJoiner.skipNulls();
    }
    
    @Test
    public void testMapJoiner_appendTo() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        
        StringBuilder result = mapJoiner.appendTo(builder, map);
        assertEquals("A=1,B=2,C=3", result.toString());
    }
    
    @Test
    public void testMapJoiner_join() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        
        String result = mapJoiner.join(map);
        assertEquals("A=1,B=2,C=3", result);
    }
    
    // Add more test cases to cover edge cases and exception handling scenarios
    
}

