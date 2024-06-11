import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        Splitter splitter = Splitter.on(',');
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a,b,c"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a,b,c,"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList(",a,b,c"));
        Assert.assertEquals(Arrays.asList("", "", "a", "b", "c"), splitter.splitToList(",,a,b,c"));
    }

    @Test
    public void testOnStringSeparator() {
        Splitter splitter = Splitter.on(", ");
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a, b, c"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a, b, c, "));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList(", a, b, c"));
        Assert.assertEquals(Arrays.asList("", "a", "b", "c"), splitter.splitToList(", , a, b, c"));
    }

    @Test
    public void testOnPatternSeparator() {
        Splitter splitter = Splitter.onPattern("\\s+");
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a b c"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a b c "));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList(" a b c"));
        Assert.assertEquals(Arrays.asList("", "a", "b", "c"), splitter.splitToList("  a b c"));
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Assert.assertEquals(Arrays.asList("ab", "cd", "ef", "g"), splitter.splitToList("abcdefg"));
        Assert.assertEquals(Arrays.asList("ab", "cd", "ef"), splitter.splitToList("abcdef"));
        Assert.assertEquals(Arrays.asList("ab", "cd"), splitter.splitToList("abcd"));
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a, , b, c"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a,,b,c"));
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Assert.assertEquals(Arrays.asList("a", "b,c"), splitter.splitToList("a,b,c"));
        Assert.assertEquals(Arrays.asList("a", "b,c,d"), splitter.splitToList("a,b,c,d"));
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList("a, b , c"));
        Assert.assertEquals(Arrays.asList("a", "b", "c"), splitter.splitToList(" a , b , c "));
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Iterable<String> result = splitter.split("a,b,c");
        Iterator<String> iterator = result.iterator();
        Assert.assertEquals("a", iterator.next());
        Assert.assertEquals("b", iterator.next());
        Assert.assertEquals("c", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        List<String> result = splitter.splitToList("a,b,c");
        Assert.assertEquals(Arrays.asList("a", "b", "c"), result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter splitter = Splitter.on(";").withKeyValueSeparator("=");
        Map<String, String> result = splitter.split("key1=value1;key2=value2");
        Assert.assertEquals("value1", result.get("key1"));
        Assert.assertEquals("value2", result.get("key2"));
    }
}