import static org.junit.Assert.*;
import org.junit.*;

public class SplitterTest {

    @Test
    public void testSplit_WithCharSeparator() {
        String input = "Hello,World,!";
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplit_WithCharMatcherSeparator() {
        String input = "Hello,World,!";
        Splitter splitter = Splitter.on(CharMatcher.is(','));
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplit_WithStringSeparator() {
        String input = "Hello,World,!";
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplit_WithPatternSeparator() {
        String input = "Hello,World,!";
        Splitter splitter = Splitter.on(Pattern.compile(","));
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplit_WithFixedLength() {
        String input = "HelloWorld!";
        Splitter splitter = Splitter.fixedLength(5);
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        String input = "Hello,,World,!,,";
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testLimit() {
        String input = "Hello,World,!,Test";
        Splitter splitter = Splitter.on(',').limit(2);
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World,!,Test"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testTrimResults() {
        String input = "   Hello  ,  World   ,   !   ";
        Splitter splitter = Splitter.on(',').trimResults();
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testTrimResults_WithCharMatcher() {
        String input = "   Hello  ,  World   ,   !   ";
        Splitter splitter = Splitter.on(',').trimResults(CharMatcher.WHITESPACE);
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplitToList() {
        String input = "Hello,World,!";
        Splitter splitter = Splitter.on(',');
        List<String> result = splitter.splitToList(input);
        String[] expected = {"Hello", "World", "!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testWithKeyValueSeparator_WithStringSeparator() {
        String input = "key1=value1,key2=value2,key3=value3";
        Splitter splitter = Splitter.on(',').withKeyValueSeparator('=');
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(':');
        Map<String, String> result = mapSplitter.split(input);
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator_WithCharSeparator() {
        String input = "key1:value1,key2:value2,key3:value3";
        Splitter splitter = Splitter.on(':').withKeyValueSeparator('=');
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(',');
        Map<String, String> result = mapSplitter.split(input);
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator_WithSplitter() {
        String input = "key1=value1,key2=value2,key3=value3";
        Splitter splitter = Splitter.on(',').withKeyValueSeparator('=');
        Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator(Splitter.on(':'));
        Map<String, String> result = mapSplitter.split(input);
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }
}