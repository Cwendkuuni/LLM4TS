import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testAppendTo_withIterable_shouldReturnAppendable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("a", "b", "c");
        
        StringBuilder result = joiner.appendTo(builder, iterable);
        
        assertNotNull(result);
        assertEquals("a,b,c", result.toString());
    }
    
    @Test
    public void testAppendTo_withIterator_shouldReturnAppendable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("a", "b", "c").iterator();
        
        StringBuilder result = joiner.appendTo(builder, iterator);
        
        assertNotNull(result);
        assertEquals("a,b,c", result.toString());
    }
    
    @Test
    public void testAppendTo_withArray_shouldReturnAppendable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        String[] array = {"a", "b", "c"};
        
        StringBuilder result = joiner.appendTo(builder, array);
        
        assertNotNull(result);
        assertEquals("a,b,c", result.toString());
    }
    
    @Test
    public void testAppendTo_withNulls_shouldReturnAppendable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] array = {null, "b", null, "c"};
        
        StringBuilder result = joiner.appendTo(builder, array);
        
        assertNotNull(result);
        assertEquals(",b,,c", result.toString());
    }
    
    @Test
    public void testJoin_withIterable_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        Iterable<String> iterable = Arrays.asList("a", "b", "c");
        
        String result = joiner.join(iterable);
        
        assertEquals("a,b,c", result);
    }
    
    @Test
    public void testJoin_withIterator_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterator = Arrays.asList("a", "b", "c").iterator();
        
        String result = joiner.join(iterator);
        
        assertEquals("a,b,c", result);
    }
    
    @Test
    public void testJoin_withArray_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"a", "b", "c"};
        
        String result = joiner.join(array);
        
        assertEquals("a,b,c", result);
    }
    
    @Test
    public void testJoin_withNulls_shouldReturnString() {
        Joiner joiner = Joiner.on(",");
        Object[] array = {null, "b", null, "c"};
        
        String result = joiner.join(array);
        
        assertEquals(",b,,c", result);
    }
    
    @Test
    public void testUseForNull_withNullText_shouldReturnNewJoiner() {
        Joiner joiner = Joiner.on(",");
        String nullText = "NULL";
        
        Joiner result = joiner.useForNull(nullText);
        
        assertNotNull(result);
        assertEquals(nullText, result.toString(null));
    }
    
    @Test
    public void testSkipNulls_withNulls_shouldReturnNewJoiner() {
        Joiner joiner = Joiner.on(",");
        Object[] array = {null, "b", null, "c"};
        
        Joiner result = joiner.skipNulls();
        
        assertNotNull(result);
        assertEquals("b,c", result.join(array));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMaps_shouldThrowUnsupportedOperationException() {
        Joiner joiner = Joiner.on(",");
        
        joiner.skipNulls();
    }
    
    @Test
    public void testWithKeyValueSeparator_withKeyValueSeparator_shouldReturnMapJoiner() {
        Joiner joiner = Joiner.on(",");
        String keyValueSeparator = "=";
        
        Joiner.MapJoiner result = joiner.withKeyValueSeparator(keyValueSeparator);
        
        assertNotNull(result);
        assertEquals(keyValueSeparator, result.keyValueSeparator);
    }

}