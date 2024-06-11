import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    private Joiner joiner;
    
    @Before
    public void setup() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterable() throws IOException {
        List<String> inputList = new ArrayList<>();
        inputList.add("apple");
        inputList.add("banana");
        inputList.add("cherry");
        
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, inputList);
        
        assertEquals("apple,banana,cherry", stringBuilder.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndIterator() throws IOException {
        List<String> inputList = new ArrayList<>();
        inputList.add("apple");
        inputList.add("banana");
        inputList.add("cherry");
        
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, inputList.iterator());
        
        assertEquals("apple,banana,cherry", stringBuilder.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndObjectArray() throws IOException {
        String[] inputArray = {"apple", "banana", "cherry"};
        
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, inputArray);
        
        assertEquals("apple,banana,cherry", stringBuilder.toString());
    }
    
    @Test
    public void testAppendTo_withAppendableAndObjects() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        joiner.appendTo(stringBuilder, "apple", "banana", "cherry");
        
        assertEquals("apple,banana,cherry", stringBuilder.toString());
    }
    
    @Test
    public void testJoin_withIterable() {
        List<String> inputList = new ArrayList<>();
        inputList.add("apple");
        inputList.add("banana");
        inputList.add("cherry");
        
        String joinedString = joiner.join(inputList);
        
        assertEquals("apple,banana,cherry", joinedString);
    }
    
    @Test
    public void testJoin_withIterator() {
        List<String> inputList = new ArrayList<>();
        inputList.add("apple");
        inputList.add("banana");
        inputList.add("cherry");
        
        String joinedString = joiner.join(inputList.iterator());
        
        assertEquals("apple,banana,cherry", joinedString);
    }
    
    @Test
    public void testJoin_withObjectArray() {
        String[] inputArray = {"apple", "banana", "cherry"};
        
        String joinedString = joiner.join(inputArray);
        
        assertEquals("apple,banana,cherry", joinedString);
    }
    
    @Test
    public void testJoin_withObjects() {
        String joinedString = joiner.join("apple", "banana", "cherry");
        
        assertEquals("apple,banana,cherry", joinedString);
    }
    
    @Test
    public void testUseForNull_withNullText() {
        Joiner joinerWithNullText = joiner.useForNull("null");
        
        List<String> inputList = new ArrayList<>();
        inputList.add("apple");
        inputList.add(null);
        inputList.add("cherry");
        
        String joinedString = joinerWithNullText.join(inputList);
        
        assertEquals("apple,null,cherry", joinedString);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testUseForNull_withUseForNullAlreadySpecified() {
        joiner.useForNull("null").useForNull("null");
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner skipNullsJoiner = joiner.skipNulls();
        
        List<String> inputList = new ArrayList<>();
        inputList.add("apple");
        inputList.add(null);
        inputList.add("cherry");
        
        StringBuilder stringBuilder = new StringBuilder();
        skipNullsJoiner.appendTo(stringBuilder, inputList.iterator());
        
        assertEquals("apple,cherry", stringBuilder.toString());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testSkipNulls_withUseForNullAlreadySpecified() {
        joiner.skipNulls().useForNull("null");
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testSkipNulls_withMaps() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }
    
    @Test
    public void testMapJoiner_AppendTo_withAppendableAndMap() throws IOException {
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("apple", 1);
        inputMap.put("banana", 2);
        inputMap.put("cherry", 3);
        
        StringBuilder stringBuilder = new StringBuilder();
        joiner.withKeyValueSeparator(":").appendTo(stringBuilder, inputMap);
        
        assertEquals("apple:1,banana:2,cherry:3", stringBuilder.toString());
    }
    
    @Test
    public void testMapJoiner_AppendTo_withStringBuilderAndMap() {
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("apple", 1);
        inputMap.put("banana", 2);
        inputMap.put("cherry", 3);
        
        StringBuilder stringBuilder = joiner.withKeyValueSeparator(":").appendTo(new StringBuilder(), inputMap);
        
        assertEquals("apple:1,banana:2,cherry:3", stringBuilder.toString());
    }
    
    @Test
    public void testMapJoiner_Join_withMap() {
        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("apple", 1);
        inputMap.put("banana", 2);
        inputMap.put("cherry", 3);
        
        String joinedString = joiner.withKeyValueSeparator(":").join(inputMap);
        
        assertEquals("apple:1,banana:2,cherry:3", joinedString);
    }
    
    @Test
    public void testMapJoiner_AppendTo_withIterableEntries() {
        List<Map.Entry<String, Integer>> inputList = new ArrayList<>();
        inputList.add(Map.entry("apple", 1));
        inputList.add(Map.entry("banana", 2));
        inputList.add(Map.entry("cherry", 3));
        
        String joinedString = joiner.withKeyValueSeparator(":").join(inputList);
        
        assertEquals("apple:1,banana:2,cherry:3", joinedString);
    }
    
    @Test
    public void testMapJoiner_AppendTo_withIteratorEntries() {
        List<Map.Entry<String, Integer>> inputList = new ArrayList<>();
        inputList.add(Map.entry("apple", 1));
        inputList.add(Map.entry("banana", 2));
        inputList.add(Map.entry("cherry", 3));
        
        String joinedString = joiner.withKeyValueSeparator(":").join(inputList.iterator());
        
        assertEquals("apple:1,banana:2,cherry:3", joinedString);
    }
    
    @Test
    public void testMapJoiner_Join_withEntries() {
        List<Map.Entry<String, Integer>> inputList = new ArrayList<>();
        inputList.add(Map.entry("apple", 1));
        inputList.add(Map.entry("banana", 2));
        inputList.add(Map.entry("cherry", 3));
        
        String joinedString = joiner.withKeyValueSeparator(":").join(inputList.iterator());
        
        assertEquals("apple:1,banana:2,cherry:3", joinedString);
    }
}