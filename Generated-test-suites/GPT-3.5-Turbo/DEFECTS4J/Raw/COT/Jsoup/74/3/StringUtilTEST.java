
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        assertEquals("a,b,c", StringUtil.join(Arrays.asList("a", "b", "c"), ","));
    }

    @Test
    public void testJoinIterator() {
        assertEquals("a,b,c", StringUtil.join(Arrays.asList("a", "b", "c").iterator(), ","));
    }

    @Test
    public void testJoinArray() {
        assertEquals("a,b,c", StringUtil.join(new String[]{"a", "b", "c"}, ","));
    }

    @Test
    public void testPadding() {
        assertEquals("          ", StringUtil.padding(10));
    }

    @Test
    public void testIsBlankNull() {
        assertEquals(true, StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmpty() {
        assertEquals(true, StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlankWhitespace() {
        assertEquals(true, StringUtil.isBlank(" "));
    }

    @Test
    public void testIsBlankNotWhitespace() {
        assertEquals(false, StringUtil.isBlank("abc"));
    }

    @Test
    public void testIsNumericNull() {
        assertEquals(false, StringUtil.isNumeric(null));
    }

    @Test
    public void testIsNumericEmpty() {
        assertEquals(false, StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericInvalidChar() {
        assertEquals(false, StringUtil.isNumeric("abc"));
    }

    @Test
    public void testIsNumericValid() {
        assertEquals(true, StringUtil.isNumeric("123"));
    }

    @Test
    public void testIsWhitespaceSpace() {
        assertEquals(true, StringUtil.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        assertEquals(true, StringUtil.isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewline() {
        assertEquals(true, StringUtil.isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespaceFormfeed() {
        assertEquals(true, StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        assertEquals(true, StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsWhitespaceNotWhitespace() {
        assertEquals(false, StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        assertEquals(true, StringUtil.isActuallyWhitespace(' '));
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        assertEquals(true, StringUtil.isActuallyWhitespace('\t'));
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        assertEquals(true, StringUtil.isActuallyWhitespace('\n'));
    }

    @Test
    public void testIsActuallyWhitespaceFormfeed() {
        assertEquals(true, StringUtil.isActuallyWhitespace('\f'));
    }

    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        assertEquals(true, StringUtil.isActuallyWhitespace('\r'));
    }

    @Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        assertEquals(true, StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testIsActuallyWhitespaceNotWhitespace() {
        assertEquals(false, StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        assertEquals("a b c", StringUtil.normaliseWhitespace("a  b   c"));
    }

    @Test
    public void testNormaliseWhitespaceWhitespaceCharacters() {
        assertEquals("a b c", StringUtil.normaliseWhitespace("a\tb\nc"));
    }

    @Test
    public void testIn() {
        assertEquals(true, StringUtil.in("a", "a", "b", "c"));
    }

    @Test
    public void testInNotIn() {
        assertEquals(false, StringUtil.in("d", "a", "b", "c"));
    }

    @Test
    public void testInSorted() {
        assertEquals(true, StringUtil.inSorted("a", new String[]{"a", "b", "c"}));
    }

    @Test
    public void testInSortedNotIn() {
        assertEquals(false, StringUtil.inSorted("d", new String[]{"a", "b", "c"}));
    }

    @Test
    public void testResolve() {
        try {
            assertEquals("http://example.com/foo", StringUtil.resolve(new URL("http://example.com"), "foo").toString());
        } catch (MalformedURLException e) {
            fail("MalformedURLException: " + e.getMessage());
        }
    }

    @Test
    public void testResolveInvalidURL() {
        assertEquals("", StringUtil.resolve("not a url", "foo"));
    }

    @Test
    public void testResolveURLStartsWithQuestionMark() {
        try {
            assertEquals("http://example.com/foo?bar", StringUtil.resolve(new URL("http://example.com"), "?bar").toString());
        } catch (MalformedURLException e) {
            fail("MalformedURLException: " + e.getMessage());
        }
    }

    @Test
    public void testResolveURLStartsWithDot() {
        try {
            assertEquals("http://example.com/foo", StringUtil.resolve("http://example.com/bar", "../foo"));
        } catch (MalformedURLException e) {
            fail("MalformedURLException: " + e.getMessage());
        }
    }

    @Test
    public void testStringBuilder() {
        assertNotNull(StringUtil.stringBuilder());
    }
}

