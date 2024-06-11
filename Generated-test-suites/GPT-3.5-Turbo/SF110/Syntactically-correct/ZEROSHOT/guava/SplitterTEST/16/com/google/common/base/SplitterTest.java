import com.google.common.base.Splitter;
import org.junit.Assert;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(':');
        String input = "one:two:three";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        String input = "one,two;three";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(",");
        String input = "one,two,three";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "one two   three";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "123456";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"12", "34", "56"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",");
        String input = "one,,two,,three";
        Iterable<String> result = splitter.omitEmptyStrings().split(input);
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(":").limit(2);
        String input = "one:two:three";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"one", "two:three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        String input = " one , two , three ";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"one", "two", "three"};
        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(":");
        String input = "one:two:three";
        java.util.List<String> result = splitter.splitToList(input);
        java.util.List<String> expected = java.util.Arrays.asList("one", "two", "three");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        String input = "key1:value1,key2:value2,key3:value3";
        java.util.Map<String, String> result = mapSplitter.split(input);
        java.util.Map<String, String> expected = new java.util.LinkedHashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        expected.put("key3", "value3");
        Assert.assertEquals(expected, result);
    }

}