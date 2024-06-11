import org.junit.*;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testAppendTo_withIterable_shouldReturnCorrectResult() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        StringBuilder builder = new StringBuilder();
        
        StringBuilder result = joiner.appendTo(builder, numbers);
        
        assertEquals("1,2,3,4,5", result.toString());
    }

    @Test
    public void testAppendTo_withIterator_shouldReturnCorrectResult() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Iterator<Integer> iterator = numbers.iterator();
        StringBuilder builder = new StringBuilder();
        
        StringBuilder result = joiner.appendTo(builder, iterator);
        
        assertEquals("1,2,3,4,5", result.toString());
    }

    @Test
    public void testAppendTo_withObjectArray_shouldReturnCorrectResult() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {1, 2, 3, 4, 5};
        StringBuilder builder = new StringBuilder();
        
        StringBuilder result = joiner.appendTo(builder, parts);
        
        assertEquals("1,2,3,4,5", result.toString());
    }

    @Test
    public void testAppendTo_withObjects_shouldReturnCorrectResult() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object first = 1;
        Object second = 2;
        Object[] rest = {3, 4, 5};
        StringBuilder builder = new StringBuilder();
        
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        
        assertEquals("1,2,3,4,5", result.toString());
    }

    @Test
    public void testJoin_withIterable_shouldReturnCorrectResult() {
        Joiner joiner = Joiner.on(",");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        String result = joiner.join(numbers);
        
        assertEquals("1,2,3,4,5", result);
    }

    @Test
    public void testJoin_withIterator_shouldReturnCorrectResult() {
        Joiner joiner = Joiner.on(",");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Iterator<Integer> iterator = numbers.iterator();
        
        String result = joiner.join(iterator);
        
        assertEquals("1,2,3,4,5", result);
    }

    @Test
    public void testJoin_withObjectArray_shouldReturnCorrectResult() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {1, 2, 3, 4, 5};
        
        String result = joiner.join(parts);
        
        assertEquals("1,2,3,4,5", result);
    }

    @Test
    public void testJoin_withObjects_shouldReturnCorrectResult() {
        Joiner joiner = Joiner.on(",");
        Object first = 1;
        Object second = 2;
        Object[] rest = {3, 4, 5};
        
        String result = joiner.join(first, second, rest);
        
        assertEquals("1,2,3,4,5", result);
    }

    @Test
    public void testUseForNull_shouldReturnJoinerWithNullText() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        
        assertEquals("NULL", joiner.toString(null));
        assertEquals("1,NULL,3,4,5", joiner.join(1, null, 3, 4, 5));
    }

    @Test
    public void testSkipNulls_shouldReturnJoinerSkippingNullValues() throws IOException {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<Integer> numbers = Arrays.asList(1, null, 3, null, 5);
        StringBuilder builder = new StringBuilder();
        
        StringBuilder result = joiner.appendTo(builder, numbers);
        
        assertEquals("1,3,5", result.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_shouldThrowUnsupportedOperationException_whenUseForNullCalled() {
        Joiner joiner = Joiner.on(",").skipNulls();
        
        joiner.useForNull("NULL");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_shouldThrowUnsupportedOperationException_whenWithKeyValueSeparatorCalled() {
        Joiner joiner = Joiner.on(",").skipNulls();
        
        joiner.withKeyValueSeparator(":");
    }

    @Test
    public void testWithKeyValueSeparator_shouldReturnMapJoinerWithCorrectKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        
        assertEquals("1:2,3:4,5:6", mapJoiner.join(Map.of(1, 2, 3, 4, 5, 6).entrySet()));
    }
}