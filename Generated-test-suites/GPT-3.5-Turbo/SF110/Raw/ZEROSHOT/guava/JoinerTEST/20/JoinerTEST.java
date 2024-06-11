import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        List<String> list = Arrays.asList("one", "two", "three");
        joiner.appendTo(builder, list);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        List<String> list = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = list.iterator();
        joiner.appendTo(builder, iterator);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] array = {"one", "two", "three"};
        joiner.appendTo(builder, array);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object first = "one";
        Object second = "two";
        Object[] rest = {"three"};
        joiner.appendTo(builder, first, second, rest);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        List<String> list = Arrays.asList("one", "two", "three");
        joiner.appendTo(builder, list);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        List<String> list = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = list.iterator();
        joiner.appendTo(builder, iterator);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] array = {"one", "two", "three"};
        joiner.appendTo(builder, array);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjects() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object first = "one";
        Object second = "two";
        Object[] rest = {"three"};
        joiner.appendTo(builder, first, second, rest);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("one", "two", "three");
        String result = joiner.join(list);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = list.iterator();
        String result = joiner.join(iterator);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] array = {"one", "two", "three"};
        String result = joiner.join(array);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        Object first = "one";
        Object second = "two";
        Object[] rest = {"three"};
        String result = joiner.join(first, second, rest);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        String result = joiner.join("one", null, "three");
        assertEquals("one,null,three", result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join("one", null, "three");
        assertEquals("one,three", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithUseForNull() {
        Joiner joiner = Joiner.on(",").skipNulls().useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMapJoiner() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.withKeyValueSeparator("=").join(Collections.singletonMap("key", "value"));
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        assertNotNull(mapJoiner);
        assertEquals("=", mapJoiner.keyValueSeparator);
    }
    
    @Test
    public void testMapJoinerAppendToIterable() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        mapJoiner.appendTo(builder, map);
        assertEquals("one=1,two=2", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToStringBuilderIterable() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        mapJoiner.appendTo(builder, map);
        assertEquals("one=1,two=2", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinIterable() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        String result = mapJoiner.join(map);
        assertEquals("one=1,two=2", result);
    }
    
    @Test
    public void testMapJoinerAppendToIterator() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        mapJoiner.appendTo(builder, iterator);
        assertEquals("one=1,two=2", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToStringBuilderIterator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        mapJoiner.appendTo(builder, iterator);
        assertEquals("one=1,two=2", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinIterator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        String result = mapJoiner.join(iterator);
        assertEquals("one=1,two=2", result);
    }
    
    @Test
    public void testMapJoinerAppendToBuilder() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        mapJoiner.appendTo(builder, map);
        assertEquals("one=1,two=2", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinBuilder() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        String result = mapJoiner.join(map);
        assertEquals("one=1,two=2", result);
    }
    
    @Test
    public void testMapJoinerUseForNull() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=").useForNull("null");
        String result = mapJoiner.join(Collections.singletonMap("key", null));
        assertEquals("key=null", result);
    }
}