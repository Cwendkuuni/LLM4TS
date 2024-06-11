import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JoinerTest {
    
    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testOn_withStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testOn_withCharSeparator() {
        Joiner joiner = Joiner.on(':');
        assertNotNull(joiner);
        assertEquals(":", joiner.separator);
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterable() {
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3);
        StringBuilder result = joiner.appendTo(builder, iterable);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterator() {
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndArray() {
        StringBuilder builder = new StringBuilder();
        Object[] array = {1, 2, 3};
        StringBuilder result = joiner.appendTo(builder, array);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndObjects() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, 1, "2", 3.0);
        assertEquals("1,2,3.0", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterable() {
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3);
        StringBuilder result = joiner.appendTo(builder, iterable);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterator() {
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        StringBuilder result = joiner.appendTo(builder, iterator);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndArray() {
        StringBuilder builder = new StringBuilder();
        Object[] array = {1, 2, 3};
        StringBuilder result = joiner.appendTo(builder, array);
        assertEquals("1,2,3", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndObjects() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, 1, "2", 3.0);
        assertEquals("1,2,3.0", result.toString());
    }
    
    @Test
    public void testJoin_withIterable() {
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3);
        String result = joiner.join(iterable);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoin_withIterator() {
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        String result = joiner.join(iterator);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoin_withArray() {
        Object[] array = {1, 2, 3};
        String result = joiner.join(array);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoin_withObjects() {
        String result = joiner.join(1, "2", 3.0);
        assertEquals("1,2,3.0", result);
    }
    
    @Test
    public void testUseForNull_withNullText() {
        Joiner joiner = this.joiner.useForNull("null");
        assertNotNull(joiner);
    }
    
    @Test
    public void testSkipNulls_withNonNullValues() {
        String result = joiner.skipNulls().join(1, "2", 3.0);
        assertEquals("1,2,3.0", result);
    }
    
    @Test
    public void testSkipNulls_withNullValues() {
        String result = joiner.skipNulls().join(null, "2", 3.0);
        assertEquals("2,3.0", result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_useForNullException() {
        joiner.skipNulls().useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_skipNullsException() {
        joiner.skipNulls().skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withKeyValueSeparatorException() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }
    
    @Test
    public void testWithKeyValueSeparator_withSeparator() {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        assertNotNull(mapJoiner);
        assertEquals("=", mapJoiner.keyValueSeparator);
    }
    
    @Test
    public void testMapJoiner_appendTo_withAppendableAndMap() {
        StringBuilder builder = new StringBuilder();
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        StringBuilder result = joiner.withKeyValueSeparator(":").appendTo(builder, map);
        assertEquals("A:1,B:2,C:3", result.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withStringBuilderAndMap() {
        StringBuilder builder = new StringBuilder();
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        StringBuilder result = joiner.withKeyValueSeparator(":").appendTo(builder, map);
        assertEquals("A:1,B:2,C:3", result.toString());
    }
    
    @Test
    public void testMapJoiner_join_withMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        String result = joiner.withKeyValueSeparator(":").join(map);
        assertEquals("A:1,B:2,C:3", result);
    }
    
    @Test
    public void testMapJoiner_appendTo_withIterableEntries() {
        StringBuilder builder = new StringBuilder();
        Iterable<Map.Entry<String, Integer>> entries = map.entrySet();
        StringBuilder result = joiner.withKeyValueSeparator(":").appendTo(builder, entries);
        assertEquals("A:1,B:2,C:3", result.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withIteratorEntries() {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, Integer>> entries = map.entrySet().iterator();
        StringBuilder result = joiner.withKeyValueSeparator(":").appendTo(builder, entries);
        assertEquals("A:1,B:2,C:3", result.toString());
    }
    
    @Test
    public void testMapJoiner_join_withIterableEntries() {
        Iterable<Map.Entry<String, Integer>> entries = map.entrySet();
        String result = joiner.withKeyValueSeparator(":").join(entries);
        assertEquals("A:1,B:2,C:3", result);
    }
    
    @Test
    public void testMapJoiner_join_withIteratorEntries() {
        Iterator<Map.Entry<String, Integer>> entries = map.entrySet().iterator();
        String result = joiner.withKeyValueSeparator(":").join(entries);
        assertEquals("A:1,B:2,C:3", result);
    }
    
    @Test
    public void testMapJoiner_useForNull_withNullText() {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":").useForNull("null");
        assertNotNull(mapJoiner);
    }
}