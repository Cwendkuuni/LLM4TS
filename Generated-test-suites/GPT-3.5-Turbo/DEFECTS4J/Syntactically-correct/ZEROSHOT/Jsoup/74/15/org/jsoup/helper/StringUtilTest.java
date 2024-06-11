import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import org.jsoup.helper.StringUtil;
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String joinedString = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World,!", joinedString);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");
        String joinedString = StringUtil.join(strings.iterator(), ",");
        Assert.assertEquals("Hello,World,!", joinedString);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String joinedString = StringUtil.join(strings, ",");
        Assert.assertEquals("Hello,World,!", joinedString);
    }

    @Test
    public void testPadding() {
        String paddedString = StringUtil.padding(5);
        Assert.assertEquals("     ", paddedString);
    }

    @Test
    public void testIsBlank() {
        boolean isBlank = StringUtil.isBlank("");
        Assert.assertTrue(isBlank);
    }

    @Test
    public void testIsNumeric() {
        boolean isNumeric = StringUtil.isNumeric("123");
        Assert.assertTrue(isNumeric);
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = StringUtil.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testNormaliseWhitespace() {
        String normalisedString = StringUtil.normaliseWhitespace("Hello  World!");
        Assert.assertEquals("Hello World!", normalisedString);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "       Hello       World!       ", true);
        Assert.assertEquals(" Hello World! ", sb.toString());
    }

    @Test
    public void testIn() {
        boolean isIn = StringUtil.in("Hello", "Hello", "World", "!");
        Assert.assertTrue(isIn);
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"Hello", "World", "!"};
        boolean isIn = StringUtil.inSorted("Hello", haystack);
        Assert.assertTrue(isIn);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String resolvedUrl = StringUtil.resolve(base, "/path");
        Assert.assertEquals("http://www.example.com/path", resolvedUrl);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "http://www.example.com";
        String resolvedUrl = StringUtil.resolve(baseUrl, "/path");
        Assert.assertEquals("http://www.example.com/path", resolvedUrl);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder stringBuilder = StringUtil.stringBuilder();
        stringBuilder.append("Hello");
        Assert.assertEquals("Hello", stringBuilder.toString());
    }
}