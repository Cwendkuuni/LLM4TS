import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtilTest {
    
    @Test
    public void testJoinCollection() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        String joinedString = StringUtil.join(strings, " ");
        Assert.assertEquals("Hello World !", joinedString);
    }
    
    @Test
    public void testJoinIterator() {
        Collection<String> strings = Arrays.asList("Hello", "World", "!");
        Iterator<String> iterator = strings.iterator();
        String joinedString = StringUtil.join(iterator, " ");
        Assert.assertEquals("Hello World !", joinedString);
    }
    
    @Test
    public void testJoinArray() {
        String[] strings = {"Hello", "World", "!"};
        String joinedString = StringUtil.join(strings, " ");
        Assert.assertEquals("Hello World !", joinedString);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }
    
    @Test
    public void testPadding() {
        String paddedString = StringUtil.padding(5);
        Assert.assertEquals("     ", paddedString);
    }
    
    @Test
    public void testIsBlankNullString() {
        boolean isBlank = StringUtil.isBlank(null);
        Assert.assertTrue(isBlank);
    }
    
    @Test
    public void testIsBlankEmptyString() {
        boolean isBlank = StringUtil.isBlank("");
        Assert.assertTrue(isBlank);
    }
    
    @Test
    public void testIsBlankWhitespaceString() {
        boolean isBlank = StringUtil.isBlank("   ");
        Assert.assertTrue(isBlank);
    }
    
    @Test
    public void testIsBlankNonBlankString() {
        boolean isBlank = StringUtil.isBlank("Hello");
        Assert.assertFalse(isBlank);
    }
    
    @Test
    public void testIsNumericNullString() {
        boolean isNumeric = StringUtil.isNumeric(null);
        Assert.assertFalse(isNumeric);
    }
    
    @Test
    public void testIsNumericEmptyString() {
        boolean isNumeric = StringUtil.isNumeric("");
        Assert.assertFalse(isNumeric);
    }
    
    @Test
    public void testIsNumericNonNumericString() {
        boolean isNumeric = StringUtil.isNumeric("Hello");
        Assert.assertFalse(isNumeric);
    }
    
    @Test
    public void testIsNumericNumericString() {
        boolean isNumeric = StringUtil.isNumeric("123");
        Assert.assertTrue(isNumeric);
    }
    
    @Test
    public void testIsWhitespaceSpace() {
        boolean isWhitespace = StringUtil.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsWhitespaceTab() {
        boolean isWhitespace = StringUtil.isWhitespace('\t');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsWhitespaceNewline() {
        boolean isWhitespace = StringUtil.isWhitespace('\n');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsWhitespaceFormFeed() {
        boolean isWhitespace = StringUtil.isWhitespace('\f');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsWhitespaceCarriageReturn() {
        boolean isWhitespace = StringUtil.isWhitespace('\r');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsActuallyWhitespaceSpace() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsActuallyWhitespaceTab() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace('\t');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsActuallyWhitespaceNewline() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace('\n');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsActuallyWhitespaceFormFeed() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace('\f');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace('\r');
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        boolean isWhitespace = StringUtil.isActuallyWhitespace(160);
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testNormaliseWhitespace() {
        String normalizedString = StringUtil.normaliseWhitespace("Hello  World\t!");
        Assert.assertEquals("Hello World !", normalizedString);
    }
    
    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        StringUtil.appendNormalisedWhitespace(sb, "Hello  World\t!", false);
        Assert.assertEquals("Hello World !", sb.toString());
    }
    
    @Test
    public void testIn() {
        String[] haystack = {"Hello", "World", "!"};
        boolean isIn = StringUtil.in("Hello", haystack);
        Assert.assertTrue(isIn);
    }
    
    @Test
    public void testInSorted() {
        String[] haystack = {"!", "Hello", "World"};
        boolean isIn = StringUtil.inSorted("Hello", haystack);
        Assert.assertTrue(isIn);
    }
    
    @Test
    public void testResolve() throws MalformedURLException {
        URL base = new URL("http://example.com");
        String resolvedUrl = StringUtil.resolve(base, "/foo");
        Assert.assertEquals("http://example.com/foo", resolvedUrl);
    }
    
    @Test
    public void testResolveInvalidUrl() {
        String resolvedUrl = StringUtil.resolve("http://example.com", "foobar");
        Assert.assertEquals("", resolvedUrl);
    }
    
    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        Assert.assertEquals(0, sb.length());
    }

}