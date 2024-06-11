import org.junit.Assert;
import org.junit.Test;
import java.util.regex.Pattern;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Assert.assertEquals("a", splitter.split("a,b,c").iterator().next());
        Assert.assertEquals("b", splitter.split("a,b,c").iterator().next());
        Assert.assertEquals("c", splitter.split("a,b,c").iterator().next());
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf("_-"));
        Assert.assertEquals("ab", splitter.split("a_b-c").iterator().next());
        Assert.assertEquals("c", splitter.split("a_b-c").iterator().next());
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on("=");
        Assert.assertEquals("a", splitter.split("a=b").iterator().next());
        Assert.assertEquals("b", splitter.split("a=b").iterator().next());
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        Assert.assertEquals("a", splitter.split("a   b   c").iterator().next());
        Assert.assertEquals("b", splitter.split("a   b   c").iterator().next());
        Assert.assertEquals("c", splitter.split("a   b   c").iterator().next());
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        Assert.assertEquals("ab", splitter.split("abc").iterator().next());
        Assert.assertEquals("bc", splitter.split("abc").iterator().next());
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        Assert.assertEquals("a", splitter.split(",a,,b,").iterator().next());
        Assert.assertEquals("b", splitter.split(",a,,b,").iterator().next());
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        Assert.assertEquals("a", splitter.split("a,b,c").iterator().next());
        Assert.assertEquals("b,c", splitter.split("a,b,c").iterator().next());
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        Assert.assertEquals("a", splitter.split("  a  ,  b  ").iterator().next());
        Assert.assertEquals("b", splitter.split("  a  ,  b  ").iterator().next());
    }

    @Test
    public void testTrimResultsWithMatcher() {
        Splitter splitter = Splitter.on(",").trimResults(CharMatcher.is('_'));
        Assert.assertEquals("a", splitter.split("__a__,__b__").iterator().next());
        Assert.assertEquals("b", splitter.split("__a__,__b__").iterator().next());
    }

    @Test
    public void testSplit() {
        Splitter splitter = Splitter.on(",");
        Assert.assertEquals("a", splitter.split("a,b,c").iterator().next());
        Assert.assertEquals("b", splitter.split("a,b,c").iterator().next());
        Assert.assertEquals("c", splitter.split("a,b,c").iterator().next());
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        Assert.assertEquals("a", splitter.splitToList("a,b,c").get(0));
        Assert.assertEquals("b", splitter.splitToList("a,b,c").get(1));
        Assert.assertEquals("c", splitter.splitToList("a,b,c").get(2));
    }

    @Test
    public void testWithKeyValueSeparator() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        Assert.assertEquals("a", mapSplitter.split("a=1,b=2,c=3").get("a"));
        Assert.assertEquals("2", mapSplitter.split("a=1,b=2,c=3").get("b"));
        Assert.assertEquals("3", mapSplitter.split("a=1,b=2,c=3").get("c"));
    }
}