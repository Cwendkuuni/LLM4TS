
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class JoinerTest {
    
    @Test
    public void testOnWithSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOnWithCharSeparator() {
        Joiner joiner = Joiner.on(' ');
        assertNotNull(joiner);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        assertNotNull(joiner);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertNotNull(joiner);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        assertNotNull(mapJoiner);
    }
    
    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts.iterator());
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {"one", "two", "three"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToWithObjectParams() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "one", "two", "three");
        assertEquals("one,two,three", builder.toString());
    }
    
    @Test
    public void testAppendToWithNull() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("one", null, "three");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts.iterator());
        assertEquals("one,NULL,three", builder.toString());
    }
    
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("one", "two", "three");
        String result = joiner.join(parts);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("one", "two", "three");
        String result = joiner.join(parts.iterator());
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {"one", "two", "three"};
        String result = joiner.join(parts);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinWithObjectParams() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("one", "two", "three");
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoinWithNull() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("one", null, "three");
        String result = joiner.join(parts.iterator());
        assertEquals("one,NULL,three", result);
    }
    
    @Test
    public void testUseForNullWithNullText() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String result = joiner.join("one", null, "three");
        assertEquals("one,NULL,three", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullWithExistingUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        joiner.useForNull("NULL");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullWithExistingSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.useForNull("NULL");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithExistingUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        joiner.skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithWithMaps() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator("=");
    }
    
    @Test
    public void testMapJoinerAppendToWithMap() throws IOException {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map);
        assertEquals("one=1,two=2,three=3", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToWithMapEntries() throws IOException {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map.entrySet());
        assertEquals("one=1,two=2,three=3", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map.entrySet().iterator());
        assertEquals("one=1,two=2,three=3", builder.toString());
    }
    
    @Test
    public void testMapJoinerAppendToWithNull() throws IOException {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", null);
        map.put("three", 3);
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, map.entrySet().iterator());
        assertEquals("one=1,two=NULL,three=3", builder.toString());
    }
    
    @Test
    public void testMapJoinerJoinWithMap() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        String result = mapJoiner.join(map);
        assertEquals("one=1,two=2,three=3", result);
    }
    
    @Test
    public void testMapJoinerJoinWithMapEntries() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        String result = mapJoiner.join(map.entrySet());
        assertEquals("one=1,two=2,three=3", result);
    }
    
    @Test
    public void testMapJoinerJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        String result = mapJoiner.join(map.entrySet().iterator());
        assertEquals("one=1,two=2,three=3", result);
    }
    
    @Test
    public void testMapJoinerJoinWithNull() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", null);
        map.put("three", 3);
        String result = mapJoiner.join(map.entrySet().iterator());
        assertEquals("one=1,two=NULL,three=3", result);
    }
}
