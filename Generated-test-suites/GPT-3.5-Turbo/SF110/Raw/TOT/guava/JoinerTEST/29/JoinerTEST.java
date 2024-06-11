
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOn_String() {
        Joiner joiner = Joiner.on(" ");
        String result = joiner.join("Hello", "World");
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testOn_Char() {
        Joiner joiner = Joiner.on(',');
        String result = joiner.join("apple", "banana", "orange");
        assertEquals("apple,banana,orange", result);
    }
    
    @Test
    public void testAppendTo_Iterable() {
        Joiner joiner = Joiner.on(", ");
        StringBuilder builder = new StringBuilder();
        String result = joiner.appendTo(builder, Arrays.asList("one", "two", "three")).toString();
        assertEquals("one, two, three", result);
    }
    
    @Test
    public void testAppendTo_Iterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = Arrays.asList("a", "b", "c").iterator();
        String result = joiner.appendTo(builder, iterator).toString();
        assertEquals("a,b,c", result);
    }
    
    @Test
    public void testAppendTo_ObjectArray() {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        String[] parts = {"one", "two", "three"};
        String result = joiner.appendTo(builder, parts).toString();
        assertEquals("one-two-three", result);
    }
    
    @Test
    public void testAppendTo_Objects() {
        Joiner joiner = Joiner.on("/");
        StringBuilder builder = new StringBuilder();
        String result = joiner.appendTo(builder, "a", "b", "c").toString();
        assertEquals("a/b/c", result);
    }
    
    @Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(" ");
        String result = joiner.join(Arrays.asList("Hello", "World"));
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testJoin_Iterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<?> iterator = Arrays.asList("apple", "banana", "orange").iterator();
        String result = joiner.join(iterator);
        assertEquals("apple,banana,orange", result);
    }
    
    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on("-");
        String[] parts = {"one", "two", "three"};
        String result = joiner.join(parts);
        assertEquals("one-two-three", result);
    }
    
    @Test
    public void testJoin_Objects() {
        Joiner joiner = Joiner.on("/");
        String result = joiner.join("a", "b", "c");
        assertEquals("a/b/c", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String result = joiner.join("a", null, "c");
        assertEquals("a,NULL,c", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("a", null, "c");
        assertEquals("a,c", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_Exception() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator(":");
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String result = mapJoiner.appendTo(builder, map).toString();
        assertEquals("key1=value1,key2=value2", result);
    }
}
