import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testOnString() {
        Joiner joiner = Joiner.on(" ");
        assertEquals("Hello World", joiner.join("Hello", "World"));
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        assertEquals("1,2,3,4,5", joiner.join(1, 2, 3, 4, 5));
    }
    
    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(',').skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, null, 3, 4, null, 5));
        assertEquals("1,3,4,5", builder.toString());
    }
    
    @Test
    public void testAppendToIterator() {
        Joiner joiner = Joiner.on(',').useForNull("NULL");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, null, 4, 5).iterator());
        assertEquals("1,2,NULL,4,5", builder.toString());
    }
    
    @Test
    public void testAppendToObjectArray() {
        Joiner joiner = Joiner.on('/').useForNull("NULL");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[] { "a", 2, null, "d", "e" });
        assertEquals("a/2/NULL/d/e", builder.toString());
    }
    
    @Test
    public void testAppendToObjects() {
        Joiner joiner = Joiner.on(',').skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "Hello", null, "World");
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(':').useForNull("");
        StringBuilder builder = new StringBuilder("Numbers");
        joiner.appendTo(builder, Arrays.asList(1, 2, 3, 4, 5));
        assertEquals("Numbers:1:2:3:4:5", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on('/').useForNull("");
        StringBuilder builder = new StringBuilder();
        builder.append("Alpha");
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("Alpha/a/b/c", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(',').skipNulls();
        StringBuilder builder = new StringBuilder("Letters");
        joiner.appendTo(builder, new Object[] { "a", "b", "c" });
        assertEquals("Letters,a,b,c", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjects() {
        Joiner joiner = Joiner.on(':').skipNulls();
        StringBuilder builder = new StringBuilder();
        builder.append("Cars");
        joiner.appendTo(builder, "BMW", null, "Mercedes", "Audi");
        assertEquals("Cars:BMW:Mercedes:Audi", builder.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(',').skipNulls();
        assertEquals("1,2,3,4,5", joiner.join(Arrays.asList(1, 2, 3, 4, 5)));
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(',').useForNull("NULL");
        assertEquals("1,2,NULL,4,5", joiner.join(Arrays.asList(1, 2, null, 4, 5).iterator()));
    }
    
    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on('/').useForNull("NULL");
        assertEquals("a/2/NULL/d/e", joiner.join(new Object[] { "a", 2, null, "d", "e" }));
    }
    
    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(',').skipNulls();
        assertEquals("Hello,World", joiner.join("Hello", null, "World"));
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(',').useForNull("N/A");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "Apple", null, "Banana", null);
        assertEquals("Apple,N/A,Banana,N/A", builder.toString());
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(',').skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "Apple", null, "Banana", null);
        assertEquals("Apple,Banana", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToIterable() {
        Joiner.MapJoiner mapJoiner = Joiner.on(',').withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, createMap());
        assertEquals("Name=John,Age=30,City=New York", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToIterator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(':').withKeyValueSeparator("->");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, createMap().entrySet().iterator());
        assertEquals("Name->John:Age->30:City->New York", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToMap() {
        Joiner.MapJoiner mapJoiner = Joiner.on(',').withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, createMap());
        assertEquals("Name=John,Age=30,City=New York", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinIterable() {
        Joiner.MapJoiner mapJoiner = Joiner.on(':').withKeyValueSeparator("=");
        assertEquals("Name=John:Age=30:City=New York", mapJoiner.join(createMap().entrySet()));
    }
    
    @Test
    public void testMapJoinerJoinIterator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(',').withKeyValueSeparator(":");
        assertEquals("Name:John,Age:30,City:New York", mapJoiner.join(createMap().entrySet().iterator()));
    }
    
    private Map<String, Object> createMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Name", "John");
        map.put("Age", 30);
        map.put("City", "New York");
        return map;
    }
    
}