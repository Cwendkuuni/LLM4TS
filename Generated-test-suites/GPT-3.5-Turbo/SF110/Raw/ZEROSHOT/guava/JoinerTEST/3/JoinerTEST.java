import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class JoinerTest {
    
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
        assertEquals(";", joiner.separator);
    }
    
    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        List<String> parts = new ArrayList<>();
        parts.add("abc");
        parts.add("def");
        parts.add("ghi");
        
        joiner.appendTo(builder, parts);
        
        assertEquals("abc-def-ghi", builder.toString());
    }
    
    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        List<String> parts = new ArrayList<>();
        parts.add("abc");
        parts.add("def");
        parts.add("ghi");
        
        joiner.appendTo(builder, parts.iterator());
        
        assertEquals("abc-def-ghi", builder.toString());
    }
    
    @Test
    public void testAppendToWithObjectArray() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        String[] parts = {"abc", "def", "ghi"};
        
        joiner.appendTo(builder, parts);
        
        assertEquals("abc-def-ghi", builder.toString());
    }
    
    @Test
    public void testAppendToWithObjects() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"abc", "def", "ghi"};
        
        joiner.appendTo(builder, parts);
        
        assertEquals("abc-def-ghi", builder.toString());
    }
    
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on("-");
        List<String> parts = new ArrayList<>();
        parts.add("abc");
        parts.add("def");
        parts.add("ghi");
        
        String result = joiner.join(parts);
        
        assertEquals("abc-def-ghi", result);
    }
    
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on("-");
        List<String> parts = new ArrayList<>();
        parts.add("abc");
        parts.add("def");
        parts.add("ghi");
        
        String result = joiner.join(parts.iterator());
        
        assertEquals("abc-def-ghi", result);
    }
    
    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on("-");
        String[] parts = {"abc", "def", "ghi"};
        
        String result = joiner.join(parts);
        
        assertEquals("abc-def-ghi", result);
    }
    
    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on("-");
        Object[] parts = {"abc", "def", "ghi"};
        
        String result = joiner.join(parts);
        
        assertEquals("abc-def-ghi", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on("-").useForNull("null");
        StringBuilder builder = new StringBuilder();
        List<String> parts = new ArrayList<>();
        parts.add("abc");
        parts.add(null);
        parts.add("def");
        
        joiner.appendTo(builder, parts);
        
        assertEquals("abc-null-def", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAlreadySpecified() {
        Joiner joiner = Joiner.on("-").useForNull("null");
        joiner.useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsAlreadySpecified() {
        Joiner joiner = Joiner.on("-").skipNulls();
        joiner.useForNull("null");
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on("-").skipNulls();
        StringBuilder builder = new StringBuilder();
        List<String> parts = new ArrayList<>();
        parts.add(null);
        parts.add("abc");
        parts.add(null);
        parts.add("def");
        
        joiner.appendTo(builder, parts.iterator());
        
        assertEquals("abc-def", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMaps() {
        Joiner joiner = Joiner.on("-").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        
        joiner.withKeyValueSeparator(":").appendTo(new StringBuilder(), map);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on("-").withKeyValueSeparator(":");
        assertNotNull(joiner);
        assertEquals("-", joiner.separator);
        assertEquals(":", joiner.keyValueSeparator);
    }
}