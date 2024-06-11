import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    
    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterable() throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(sb, parts);
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterator() throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(sb, parts.iterator());
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndObjectArray() throws IOException {
        StringBuilder sb = new StringBuilder();
        String[] parts = {"one", "two", "three"};
        joiner.appendTo(sb, parts);
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndObjects() throws IOException {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "one", "two", "three");
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterable() {
        StringBuilder sb = new StringBuilder();
        List<String> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(sb, parts);
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterator() {
        StringBuilder sb = new StringBuilder();
        List<String> parts = Arrays.asList("one", "two", "three");
        joiner.appendTo(sb, parts.iterator());
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndObjectArray() {
        StringBuilder sb = new StringBuilder();
        String[] parts = {"one", "two", "three"};
        joiner.appendTo(sb, parts);
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndObjects() {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "one", "two", "three");
        assertEquals("one,two,three", sb.toString());
    }
    
    @Test
    public void testJoin_withIterable() {
        List<String> parts = Arrays.asList("one", "two", "three");
        String result = joiner.join(parts);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_withIterator() {
        List<String> parts = Arrays.asList("one", "two", "three");
        String result = joiner.join(parts.iterator());
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_withObjectArray() {
        String[] parts = {"one", "two", "three"};
        String result = joiner.join(parts);
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testJoin_withObjects() {
        String result = joiner.join("one", "two", "three");
        assertEquals("one,two,three", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner nullJoiner = joiner.useForNull("null");
        StringBuilder sb = new StringBuilder();
        List<String> parts = Arrays.asList("one", null, "three");
        nullJoiner.appendTo(sb, parts);
        assertEquals("one,null,three", sb.toString());
    }
    
    @Test
    public void testSkipNulls() {
        Joiner skipNullsJoiner = joiner.skipNulls();
        StringBuilder sb = new StringBuilder();
        List<String> parts = Arrays.asList("one", null, "three");
        skipNullsJoiner.appendTo(sb, parts);
        assertEquals("one,three", sb.toString());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        mapJoiner.appendTo(sb, map);
        assertEquals("one:1,two:2,three:3", sb.toString());
    }
}