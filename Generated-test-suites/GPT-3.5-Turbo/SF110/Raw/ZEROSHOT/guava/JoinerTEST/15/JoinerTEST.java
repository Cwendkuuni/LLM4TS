import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    
    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterator() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        joiner.appendTo(builder, parts.iterator());
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterable() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        joiner.appendTo(builder, parts);
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndArray() throws IOException {
        StringBuilder builder = new StringBuilder();
        String[] parts = {"Hello", "World"};
        joiner.appendTo(builder, parts);
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndObjects() throws IOException {
        StringBuilder builder = new StringBuilder();
        String first = "Hello";
        String second = "World";
        String[] rest = {"Java", "Test"};
        joiner.appendTo(builder, first, second, rest);
        assertEquals("Hello,World,Java,Test", builder.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterator() {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        joiner.appendTo(builder, parts.iterator());
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterable() {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("Hello", "World");
        joiner.appendTo(builder, parts);
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndArray() {
        StringBuilder builder = new StringBuilder();
        String[] parts = {"Hello", "World"};
        joiner.appendTo(builder, parts);
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndObjects() {
        StringBuilder builder = new StringBuilder();
        String first = "Hello";
        String second = "World";
        String[] rest = {"Java", "Test"};
        joiner.appendTo(builder, first, second, rest);
        assertEquals("Hello,World,Java,Test", builder.toString());
    }
    
    @Test
    public void testJoin_withIterator() {
        List<String> parts = Arrays.asList("Hello", "World");
        String result = joiner.join(parts.iterator());
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withIterable() {
        List<String> parts = Arrays.asList("Hello", "World");
        String result = joiner.join(parts);
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withArray() {
        String[] parts = {"Hello", "World"};
        String result = joiner.join(parts);
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withObjects() {
        String first = "Hello";
        String second = "World";
        String[] rest = {"Java", "Test"};
        String result = joiner.join(first, second, rest);
        assertEquals("Hello,World,Java,Test", result);
    }
    
    @Test
    public void testUseForNull() {
        String nullText = "null";
        Joiner nullJoiner = joiner.useForNull(nullText);
        String result = nullJoiner.join("Hello", null, "World");
        assertEquals("Hello,null,World", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_twice() {
        String nullText = "null";
        Joiner nullJoiner = joiner.useForNull(nullText);
        nullJoiner.useForNull(nullText);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls() {
        joiner.skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_twice() {
        joiner.skipNulls().skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testWithKeyValueSeparator() {
        joiner.withKeyValueSeparator(":");
    }
    
    @Test
    public void testMapJoiner_appendTo_withAppendableAndMap() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        mapJoiner.appendTo(builder, map);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withStringBuilderAndMap() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        mapJoiner.appendTo(builder, map);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_join_withMap() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String result = mapJoiner.join(map);
        assertEquals("key1:value1,key2:value2", result);
    }
    
    @Test
    public void testMapJoiner_appendTo_withIterable() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        mapJoiner.appendTo(builder, entries);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withIterator() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        mapJoiner.appendTo(builder, entries.iterator());
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withStringBuilderAndIterable() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        mapJoiner.appendTo(builder, entries);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withStringBuilderAndIterator() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        mapJoiner.appendTo(builder, entries.iterator());
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_join_withIterable() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        String result = mapJoiner.join(entries);
        assertEquals("key1:value1,key2:value2", result);
    }
    
    @Test
    public void testMapJoiner_join_withIterator() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        List<Map.Entry<String, String>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("key1", "value1"));
        entries.add(new AbstractMap.SimpleEntry<>("key2", "value2"));
        String result = mapJoiner.join(entries.iterator());
        assertEquals("key1:value1,key2:value2", result);
    }
    
    @Test
    public void testMapJoiner_useForNull() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":").useForNull("null");
        Map<String, String> map = new HashMap<>();
        map.put("key1", null);
        map.put("key2", "value2");
        String result = mapJoiner.join(map);
        assertEquals("key1:null,key2:value2", result);
    }
}