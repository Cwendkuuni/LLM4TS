
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList("apple,banana,cherry");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnCharWithEmptyString() {
        Splitter splitter = Splitter.on(',');
        
        List<String> expected = Arrays.asList("", "", "");
        List<String> actual = splitter.splitToList(",,");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnCharWithTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList(" apple , banana , cherry ");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("::");
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList("apple::banana::cherry");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnStringWithEmptyString() {
        Splitter splitter = Splitter.on("::");
        
        List<String> expected = Arrays.asList("", "", "");
        List<String> actual = splitter.splitToList("::::");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\d+"));
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList("apple123banana456cherry");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOnPatternWithEmptyString() {
        Splitter splitter = Splitter.on(Pattern.compile("\\d+"));
        
        List<String> expected = Arrays.asList("", "", "");
        List<String> actual = splitter.splitToList("123456");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        
        List<String> expected = Arrays.asList("ab", "cd", "ef");
        List<String> actual = splitter.splitToList("abcdef");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFixedLengthWithEmptyString() {
        Splitter splitter = Splitter.fixedLength(2);
        
        List<String> expected = Arrays.asList("", "", "");
        List<String> actual = splitter.splitToList("  ");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList(",apple,banana,,cherry,");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        
        List<String> expected = Arrays.asList("apple", "banana,cherry");
        List<String> actual = splitter.splitToList("apple,banana,cherry");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList(" apple , banana , cherry ");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTrimResultsWithTrimChars() {
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(':'));
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList(":apple,:banana:,:cherry:");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',');
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList("apple,banana,cherry");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSplitToListWithEmptyString() {
        Splitter splitter = Splitter.on(',');
        
        List<String> expected = Arrays.asList("", "", "");
        List<String> actual = splitter.splitToList(",,");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSplitToListWithTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        
        List<String> expected = Arrays.asList("apple", "banana", "cherry");
        List<String> actual = splitter.splitToList(" apple , banana , cherry ");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on(',').withKeyValueSeparator(':');
        
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("apple", "red");
        expected.put("banana", "yellow");
        expected.put("cherry", "red");
        
        Map<String, String> actual = splitter.split("apple:red,banana:yellow,cherry:red");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMapSplitterSplit() {
        Splitter outerSplitter = Splitter.on(',').trimResults();
        Splitter entrySplitter = Splitter.on(':').trimResults();
        
        Splitter.MapSplitter mapSplitter = new Splitter.MapSplitter(outerSplitter, entrySplitter);
        
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("apple", "red");
        expected.put("banana", "yellow");
        expected.put("cherry", "red");
        
        Map<String, String> actual = mapSplitter.split("apple : red , banana : yellow , cherry : red");
        
        assertEquals(expected, actual);
    }
}
