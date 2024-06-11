import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {
    
    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testOn_StringSeparator() {
        Joiner j = Joiner.on(",");
        assertNotNull(j);
    }
    
    @Test
    public void testOn_CharSeparator() {
        Joiner j = Joiner.on(',');
        assertNotNull(j);
    }
    
    @Test
    public void testAppendTo_Iterable() throws IOException {
        Appendable appendable = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        Appendable result = joiner.appendTo(appendable, parts);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_Iterator() throws IOException {
        Appendable appendable = new StringBuilder();
        Iterator<Integer> parts = Arrays.asList(1, 2, 3).iterator();
        Appendable result = joiner.appendTo(appendable, parts);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_ObjectArray() throws IOException {
        Appendable appendable = new StringBuilder();
        Object[] parts = {1, 2, 3};
        Appendable result = joiner.appendTo(appendable, parts);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_ObjectParams() throws IOException {
        Appendable appendable = new StringBuilder();
        Object first = 1;
        Object second = 2;
        Object[] rest = {3};
        Appendable result = joiner.appendTo(appendable, first, second, rest);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Iterable() {
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_Iterator() {
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> parts = Arrays.asList(1, 2, 3).iterator();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_ObjectArray() {
        StringBuilder builder = new StringBuilder();
        Object[] parts = {1, 2, 3};
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilder_ObjectParams() {
        StringBuilder builder = new StringBuilder();
        Object first = 1;
        Object second = 2;
        Object[] rest = {3};
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testJoin_Iterable() {
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        String result = joiner.join(parts);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoin_Iterator() {
        Iterator<Integer> parts = Arrays.asList(1, 2, 3).iterator();
        String result = joiner.join(parts);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoin_ObjectArray() {
        Object[] parts = {1, 2, 3};
        String result = joiner.join(parts);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoin_ObjectParams() {
        Object first = 1;
        Object second = 2;
        Object[] rest = {3};
        String result = joiner.join(first, second, rest);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testUseForNull() {
        String nullText = "null";
        Joiner modifiedJoiner = joiner.useForNull(nullText);
        assertNotNull(modifiedJoiner);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner modifiedJoiner = joiner.skipNulls();
        assertNotNull(modifiedJoiner);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_ThrowsUnsupportedOperationException() {
        String nullText = "null";
        Joiner modifiedJoiner = joiner.useForNull(nullText);
        modifiedJoiner.useForNull(nullText);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_ThrowsUnsupportedOperationException() {
        Joiner modifiedJoiner = joiner.skipNulls();
        modifiedJoiner.skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_ThrowsUnsupportedOperationExceptionWithMaps() {
        Joiner modifiedJoiner = joiner.skipNulls();
        modifiedJoiner.withKeyValueSeparator(":");
    }
    
    @Test
    public void testMapJoiner_AppendTo_Appendable() throws IOException {
        Appendable appendable = new StringBuilder();
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        appendable = joiner.withKeyValueSeparator(":").appendTo(appendable, map);
        assertEquals("key1:value1,key2:value2", appendable.toString());
    }
    
    @Test
    public void testMapJoiner_AppendTo_StringBuilder() {
        StringBuilder builder = new StringBuilder();
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        builder = joiner.withKeyValueSeparator(":").appendTo(builder, map);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_Join() {
        Map<Object, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String result = joiner.withKeyValueSeparator(":").join(map);
        assertEquals("key1:value1,key2:value2", result);
    }
    
    @Test
    public void testMapJoiner_AppendTo_Iterable() throws IOException {
        Appendable appendable = new StringBuilder();
        Iterable<Map.Entry<Object,Object>> entries = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2")
        );
        appendable = joiner.withKeyValueSeparator(":").appendTo(appendable, entries);
        assertEquals("key1:value1,key2:value2", appendable.toString());
    }
    
    @Test
    public void testMapJoiner_AppendTo_Iterator() throws IOException {
        Appendable appendable = new StringBuilder();
        Iterator<Map.Entry<Object,Object>> entries = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2")
        ).iterator();
        appendable = joiner.withKeyValueSeparator(":").appendTo(appendable, entries);
        assertEquals("key1:value1,key2:value2", appendable.toString());
    }
    
    @Test
    public void testMapJoiner_AppendTo_StringBuilder_Iterable() {
        StringBuilder builder = new StringBuilder();
        Iterable<Map.Entry<Object,Object>> entries = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2")
        );
        builder = joiner.withKeyValueSeparator(":").appendTo(builder, entries);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_AppendTo_StringBuilder_Iterator() {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<Object,Object>> entries = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2")
        ).iterator();
        builder = joiner.withKeyValueSeparator(":").appendTo(builder, entries);
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_Join_Iterable() {
        Iterable<Map.Entry<Object,Object>> entries = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2")
        );
        String result = joiner.withKeyValueSeparator(":").join(entries);
        assertEquals("key1:value1,key2:value2", result);
    }
    
    @Test
    public void testMapJoiner_Join_Iterator() {
        Iterator<Map.Entry<Object,Object>> entries = Arrays.asList(
            new AbstractMap.SimpleEntry<>("key1", "value1"),
            new AbstractMap.SimpleEntry<>("key2", "value2")
        ).iterator();
        String result = joiner.withKeyValueSeparator(":").join(entries);
        assertEquals("key1:value1,key2:value2", result);
    }
    
    @Test
    public void testMapJoiner_UseForNull() {
        String nullText = "null";
        Joiner.MapJoiner modifiedMapJoiner = joiner.withKeyValueSeparator(":").useForNull(nullText);
        assertNotNull(modifiedMapJoiner);
    }
    
    @Test
    public void testToString() {
        Object obj = "Test";
        CharSequence result = joiner.toString(obj);
        assertEquals("Test", result);
    }
    
    @Test
    public void testIterable() {
        Object first = 1;
        Object second = 2;
        Object[] rest = {3};
        Iterable<Object> iterable = Joiner.iterable(first, second, rest);
        assertNotNull(iterable);
    }
}