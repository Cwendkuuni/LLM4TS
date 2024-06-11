
import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {
    @Test
    public void testOnChar() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOnCharMatcher() {
        String input = "Hello World";
        Splitter splitter = Splitter.on(CharMatcher.WHITESPACE);
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOnString() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOnPattern() {
        String input = "Hello World";
        Splitter splitter = Splitter.onPattern("\\s");
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testFixedLength() {
        String input = "HelloWorld";
        Splitter splitter = Splitter.fixedLength(5);
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testOmitEmptyStrings() {
        String input = "Hello,,World";
        Splitter splitter = Splitter.on(",");
        splitter = splitter.omitEmptyStrings();
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testLimit() {
        String input = "Hello,World,!";
        Splitter splitter = Splitter.on(",").limit(2);
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World,!"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testTrimResults() {
        String input = " Hello, World ";
        Splitter splitter = Splitter.on(",").trimResults();
        Iterable<String> result = splitter.split(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testSplitToList() {
        String input = "Hello,World";
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList(input);
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, result.toArray());
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        String input = "key1=value1,key2=value2";
        Splitter splitter = Splitter.on(",").withKeyValueSeparator("=");
        Map<String, String> result = splitter.split(input);
        String value1 = result.get("key1");
        String value2 = result.get("key2");
        assertEquals("value1", value1);
        assertEquals("value2", value2);
    }
}
