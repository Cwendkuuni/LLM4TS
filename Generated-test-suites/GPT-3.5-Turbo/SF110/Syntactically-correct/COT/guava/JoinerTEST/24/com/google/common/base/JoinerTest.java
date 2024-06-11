
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    @Test
    public void testOn_String() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOn_Char() {
        Joiner joiner = Joiner.on(',');
        assertNotNull(joiner);
    }
    
    @Test
    public void testAppendTo_Appendable_Iterable() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_Appendable_Iterator() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        Iterator<String> iterator = parts.iterator();
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_Appendable_ObjectArray() throws IOException {
        StringBuilder builder = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, "Hello", "World");
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_Appendable_Objects() throws IOException {
        StringBuilder builder = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, "Hello", "World", "Foo", "Bar");
        assertEquals("Hello,World,Foo,Bar", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Iterable() {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Iterator() {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        Iterator<String> iterator = parts.iterator();
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_ObjectArray() {
        StringBuilder builder = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, "Hello", "World");
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Objects() {
        StringBuilder builder = new StringBuilder();
        Joiner joiner = Joiner.on(",");
        StringBuilder result = joiner.appendTo(builder, "Hello", "World", "Foo", "Bar");
        assertEquals("Hello,World,Foo,Bar", result.toString());
    }
    
    @Test
    public void testJoin_Iterable() {
        List<String> parts = Arrays.asList("Hello", "World");
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(parts);
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_Iterator() {
        List<String> parts = Arrays.asList("Hello", "World");
        Iterator<String> iterator = parts.iterator();
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(iterator);
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("Hello", "World");
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_Objects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("Hello", "World", "Foo", "Bar");
        assertEquals("Hello,World,Foo,Bar", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String result = joiner.join("Hello", null, "World");
        assertEquals("Hello,NULL,World", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("Hello", null, "World");
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String result = joiner.join(map);
        assertEquals("key1=value1,key2=value2", result);
    }
}
