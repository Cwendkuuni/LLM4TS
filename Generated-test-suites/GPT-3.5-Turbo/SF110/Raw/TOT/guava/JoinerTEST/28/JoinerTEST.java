import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class JoinerTest {

    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @After
    public void tearDown() {
        joiner = null;
    }
    
    @Test
    public void testJoin_withIterable_shouldReturnCorrectString() {
        List<String> input = Arrays.asList("Hello", "World", "!");
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withIterator_shouldReturnCorrectString() {
        List<String> input = Arrays.asList("Hello", "World", "!");
        Iterator<String> inputIterator = input.iterator();
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(inputIterator);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withObjectArray_shouldReturnCorrectString() {
        Object[] input = {"Hello", "World", "!"};
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withObjects_shouldReturnCorrectString() {
        Object input1 = "Hello";
        Object input2 = "World";
        Object[] inputRest = {"!"};
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(input1, input2, inputRest);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withNullIterable_shouldReturnEmptyString() {
        Iterable<String> input = null;
        String expectedOutput = "";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withNullIterator_shouldReturnEmptyString() {
        Iterator<String> input = null;
        String expectedOutput = "";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withNullObjectArray_shouldReturnEmptyString() {
        Object[] input = null;
        String expectedOutput = "";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testJoin_withNullObjects_shouldReturnEmptyString() {
        Object input1 = null;
        Object input2 = null;
        Object[] inputRest = null;
        String expectedOutput = "";
        
        String result = joiner.join(input1, input2, inputRest);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testUseForNull_withNullText_shouldReplaceNullsInJoin() {
        String nullText = "NULL";
        joiner = joiner.useForNull(nullText);
        List<String> input = Arrays.asList("Hello", null, "World", "!");
        String expectedOutput = "Hello,NULL,World,!";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testUseForNull_withAlreadySpecifiedUseForNull_shouldThrowException() {
        String nullText = "NULL";
        joiner = joiner.useForNull(nullText);
        
        joiner.useForNull(nullText);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testUseForNull_withAlreadySpecifiedSkipNulls_shouldThrowException() {
        joiner = joiner.skipNulls();
        
        String nullText = "NULL";
        joiner.useForNull(nullText);
    }
    
    @Test
    public void testSkipNulls_withIterable_shouldSkipNullValues() {
        joiner = joiner.skipNulls();
        
        List<String> input = Arrays.asList("Hello", null, "World", null, "!");
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testSkipNulls_withIterator_shouldSkipNullValues() {
        joiner = joiner.skipNulls();
        
        List<String> input = Arrays.asList("Hello", null, "World", null, "!");
        Iterator<String> inputIterator = input.iterator();
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(inputIterator);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testSkipNulls_withObjectArray_shouldSkipNullValues() {
        joiner = joiner.skipNulls();
        
        Object[] input = {"Hello", null, "World", null, "!"};
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testSkipNulls_withObjects_shouldSkipNullValues() {
        joiner = joiner.skipNulls();
        
        Object input1 = "Hello";
        Object input2 = null;
        Object[] inputRest = {"World", null, "!"};
        String expectedOutput = "Hello,World,!";
        
        String result = joiner.join(input1, input2, inputRest);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testSkipNulls_withAlreadySpecifiedSkipNulls_shouldThrowException() {
        joiner = joiner.skipNulls();
        
        joiner.skipNulls();
    }
    
    @Test (expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner_shouldThrowException() {
        joiner = joiner.skipNulls();
        
        String keyValueSeparator = "=";
        joiner.withKeyValueSeparator(keyValueSeparator);
    }
    
    @Test
    public void testWithKeyValueSeparator_withValidKeyValueSeparator_shouldReturnMapJoiner() {
        String keyValueSeparator = "=";
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(keyValueSeparator);
        
        assertNotNull(mapJoiner);
        assertEquals(keyValueSeparator, mapJoiner.keyValueSeparator);
    }
    
    @Test
    public void testMapJoiner_appendTo_withMap_shouldReturnCorrectString() {
        joiner = joiner.withKeyValueSeparator("=");
        
        Map<String, Integer> input = new HashMap<>();
        input.put("Hello", 5);
        input.put("World", 6);
        String expectedOutput = "Hello=5,World=6";
        
        StringBuilder result = new StringBuilder();
        mapJoiner.appendTo(result, input);
        
        assertEquals(expectedOutput, result.toString());
    }
    
    @Test
    public void testMapJoiner_appendTo_withEntries_shouldReturnCorrectString() {
        joiner = joiner.withKeyValueSeparator("=");
        
        List<Map.Entry<String, Integer>> input = new ArrayList<>();
        input.add(new AbstractMap.SimpleEntry<>("Hello", 5));
        input.add(new AbstractMap.SimpleEntry<>("World", 6));
        String expectedOutput = "Hello=5,World=6";
        
        StringBuilder result = new StringBuilder();
        mapJoiner.appendTo(result, input);
        
        assertEquals(expectedOutput, result.toString());
    }
    
    @Test
    public void testMapJoiner_join_withMap_shouldReturnCorrectString() {
        joiner = joiner.withKeyValueSeparator("=");
        
        Map<String, Integer> input = new HashMap<>();
        input.put("Hello", 5);
        input.put("World", 6);
        String expectedOutput = "Hello=5,World=6";
        
        String result = mapJoiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testMapJoiner_join_withEntries_shouldReturnCorrectString() {
        joiner = joiner.withKeyValueSeparator("=");
        
        List<Map.Entry<String, Integer>> input = new ArrayList<>();
        input.add(new AbstractMap.SimpleEntry<>("Hello", 5));
        input.add(new AbstractMap.SimpleEntry<>("World", 6));
        String expectedOutput = "Hello=5,World=6";
        
        String result = mapJoiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testMapJoiner_useForNull_withNullText_shouldReplaceNullsInJoin() {
        joiner = joiner.withKeyValueSeparator("=").useForNull("NULL");
        
        Map<String, Integer> input = new HashMap<>();
        input.put("Hello", null);
        input.put("World", 6);
        String expectedOutput = "Hello=NULL,World=6";
        
        String result = mapJoiner.join(input);
        
        assertEquals(expectedOutput, result);
    }
}