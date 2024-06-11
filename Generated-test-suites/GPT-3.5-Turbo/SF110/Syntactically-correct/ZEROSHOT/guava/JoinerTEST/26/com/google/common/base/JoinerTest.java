import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
        assertEquals(";", joiner.separator);
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");

        Iterable<Integer> numbers = Arrays.asList(1, 2, 3);
        String result = joiner.join(numbers);
        
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");

        List<Integer> numbers = Arrays.asList(1, 2, 3);
        String result = joiner.join(numbers.iterator());
        
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");

        Integer[] numbers = {1, 2, 3};
        String result = joiner.join(numbers);
        
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinVarArgs() {
        Joiner joiner = Joiner.on(",");

        String result = joiner.join("a", "b", "c");
        
        assertEquals("a,b,c", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        
        List<String> list = Arrays.asList("a", null, "c");
        String result = joiner.join(list);
        
        assertEquals("a,null,c", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        
        List<String> list = Arrays.asList("a", null, "c");
        String result = joiner.join(list);
        
        assertEquals("a,c", result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");
        
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        
        String result = joiner.join(map);
        
        assertEquals("a=1,b=2,c=3", result);
    }

}