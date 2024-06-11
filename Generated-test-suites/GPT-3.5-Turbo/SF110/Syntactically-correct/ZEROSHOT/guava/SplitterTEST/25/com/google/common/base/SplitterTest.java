import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        String input = "1,2,3,4,5";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf("-+"));
        String input = "1-2+3-4+5";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("-");
        String input = "1-2-3-4-5";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("[a-z]");
        String input = "1a2b3c4d5";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "12345";
        List<String> expected = Arrays.asList("12", "34", "5");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        String input = ",1,,2,,3,";
        List<String> expected = Arrays.asList("1", "2", "3");
        List<String> result = splitter.omitEmptyStrings().splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",");
        String input = "1,2,3,4,5";
        List<String> expected = Arrays.asList("1", "2,3,4,5");
        List<String> result = splitter.limit(2).splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",");
        String input = "   1 , 2  , 3   , 4  ,  5   ";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.trimResults().splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",");
        String input = "   1 , 2  , 3   , 4  ,  5   ";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.trimResults(CharMatcher.WHITESPACE).splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        String input = "1,2,3,4,5";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        Iterable<String> result = splitter.split(input);
        assertEquals(expected, new ArrayList<>(result));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        String input = "1,2,3,4,5";
        List<String> expected = Arrays.asList("1", "2", "3", "4", "5");
        List<String> result = splitter.splitToList(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on(",");
        String input = "key1=value1,key2=value2,key3=value3";
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        Map<String, String> result = splitter.withKeyValueSeparator("=").split(input);
        assertEquals(expected, result);
    }
}