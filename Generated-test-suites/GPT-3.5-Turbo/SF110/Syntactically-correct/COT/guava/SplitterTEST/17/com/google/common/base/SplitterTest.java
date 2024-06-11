
import org.junit.Assert;
import org.junit.Test;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;

public class SplitterTest {
    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        String input = "a,b,c,d";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
        Assert.assertEquals("c", result.get(2));
        Assert.assertEquals("d", result.get(3));
    }
    
    @Test
    public void testOnCharWithTrim() {
        Splitter splitter = Splitter.on(',').trimResults();
        String input = " a , b , c , d ";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
        Assert.assertEquals("c", result.get(2));
        Assert.assertEquals("d", result.get(3));
    }
    
    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(", ");
        String input = "a, b, c, d";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
        Assert.assertEquals("c", result.get(2));
        Assert.assertEquals("d", result.get(3));
    }
    
    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s"));
        String input = "a b c d";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
        Assert.assertEquals("c", result.get(2));
        Assert.assertEquals("d", result.get(3));
    }
    
    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "abcdef";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("ab", result.get(0));
        Assert.assertEquals("cd", result.get(1));
        Assert.assertEquals("ef", result.get(2));
    }
    
    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        String input = ",a,,b,";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
    }
    
    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(',').limit(2);
        String input = "a,b,c,d";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b,c,d", result.get(1));
    }
    
    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(',').trimResults();
        String input = " a , b , c , d ";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
        Assert.assertEquals("c", result.get(2));
        Assert.assertEquals("d", result.get(3));
    }
    
    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
        String input = ", a ,, b , c , d,";
        List<String> result = splitter.splitToList(input);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals("a", result.get(0));
        Assert.assertEquals("b", result.get(1));
        Assert.assertEquals("c", result.get(2));
        Assert.assertEquals("d", result.get(3));
    }
    
    @Test
    public void testSplitToMap() {
        Splitter.MapSplitter mapSplitter = Splitter.on(", ").withKeyValueSeparator(":");
        String input = "key1:value1, key2:value2, key3:value3";
        Map<String, String> result = mapSplitter.split(input);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("value1", result.get("key1"));
        Assert.assertEquals("value2", result.get("key2"));
        Assert.assertEquals("value3", result.get("key3"));
    }
}

