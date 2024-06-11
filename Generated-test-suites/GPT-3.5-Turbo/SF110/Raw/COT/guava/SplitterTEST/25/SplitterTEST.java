
import org.junit.Test;
import static org.junit.Assert.*;

public class SplitterTest {
    
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on('-');
        Iterable<String> result = splitter.split("abc-def-ghi");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.is('_'));
        Iterable<String> result = splitter.split("abc_def_ghi");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("::");
        Iterable<String> result = splitter.split("abc::def::ghi");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("-"));
        Iterable<String> result = splitter.split("abc-def-ghi");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testOnPatternString() {
        Splitter splitter = Splitter.onPattern("::");
        Iterable<String> result = splitter.split("abc::def::ghi");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Iterable<String> result = splitter.split("abcdef");
        String[] expected = {"ab", "cd", "ef"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on('-').omitEmptyStrings();
        Iterable<String> result = splitter.split("abc--def-");
        String[] expected = {"abc", "def"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on('-').limit(2);
        Iterable<String> result = splitter.split("abc-def-ghi");
        String[] expected = {"abc", "def-ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on('-').trimResults();
        Iterable<String> result = splitter.split("  abc   -   def   -   ghi  ");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testTrimResultsCharMatcher() {
        Splitter splitter = Splitter.on('-').trimResults(CharMatcher.WHITESPACE);
        Iterable<String> result = splitter.split("  abc   -   def   -   ghi  ");
        String[] expected = {"abc", "def", "ghi"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(':');
        Iterable<String> result = splitter.split("a:b:c");
        String[] expected = {"a", "b", "c"};
        int i = 0;
        for (String s : result) {
            assertEquals(expected[i], s);
            i++;
        }
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(':');
        List<String> result = splitter.splitToList("a:b:c");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter outerSplitter = Splitter.on(',').omitEmptyStrings();
        Splitter entrySplitter = Splitter.on('=').omitEmptyStrings();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter outerSplitter = Splitter.on(',').omitEmptyStrings();
        Splitter entrySplitter = Splitter.on('=').omitEmptyStrings();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparatorSplitter() {
        Splitter outerSplitter = Splitter.on(',').omitEmptyStrings();
        Splitter entrySplitter = Splitter.on('=').omitEmptyStrings();
        Splitter.MapSplitter mapSplitter = outerSplitter.withKeyValueSeparator(entrySplitter);
        
        Map<String, String> result = mapSplitter.split("key1=value1,key2=value2,key3=value3");
        Map<String, String> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        assertEquals(expected, result);
    }

}

