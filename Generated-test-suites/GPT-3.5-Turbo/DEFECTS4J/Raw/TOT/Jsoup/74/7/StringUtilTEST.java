import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String result = StringUtil.join(strings, ", ");

        assertEquals("Hello, World, !", result);
    }

    @Test
    public void testJoinEmptyCollection() {
        Collection<String> strings = new ArrayList<>();

        String result = StringUtil.join(strings, ", ");

        assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("!");

        String result = StringUtil.join(strings.iterator(), ", ");

        assertEquals("Hello, World, !", result);
    }

    @Test
    public void testJoinEmptyIterator() {
        List<String> strings = new ArrayList<>();

        String result = StringUtil.join(strings.iterator(), ", ");

        assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"Hello", "World", "!"};

        String result = StringUtil.join(strings, ", ");

        assertEquals("Hello, World, !", result);
    }

    @Test
    public void testJoinEmptyArray() {
        String[] strings = new String[]{};

        String result = StringUtil.join(strings, ", ");

        assertEquals("", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testPaddingZeroWidth() {
        String result = StringUtil.padding(0);

        assertEquals("", result);
    }

    @Test
    public void testPaddingFirst21Widths() {
        String[] expectedResults = {
            "", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ",
            "         ", "          ", "           ", "            ", "             ", "              ",
            "               ", "                ", "                 ", "                  ", "                   ",
            "                    "
        };

        for (int i = 0; i <= 20; i++) {
            String result = StringUtil.padding(i);
            assertEquals(expectedResults[i], result);
        }
    }

    @Test
    public void testIsBlankNullString() {
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmptyString() {
        assertTrue(StringUtil.isBlank(""));
    }

    @Test
    public void testIsBlankWhitespaceString() {
        assertTrue(StringUtil.isBlank("   "));
    }

    @Test
    public void testIsBlankNonBlankString() {
        assertFalse(StringUtil.isBlank("Hello World!"));
    }

    @Test
    public void testIsNumericNullString() {
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsNumericEmptyString() {
        assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericNumericString() {
        assertTrue(StringUtil.isNumeric("12345"));
    }

    @Test
    public void testIsNumericNonNumericString() {
        assertFalse(StringUtil.isNumeric("Hello World!"));
    }

    @Test
    public void testIsWhitespaceSpace() {
        assertTrue(StringUtil.isWhitespace(' '));
    }

    @Test
    public void testIsWhitespaceTab() {
        assertTrue(StringUtil.isWhitespace('\t'));
    }

    @Test
    public void testIsWhitespaceNewLine() {
        assertTrue(StringUtil.isWhitespace('\n'));
    }

    @Test
    public void testIsWhitespaceFormFeed() {
        assertTrue(StringUtil.isWhitespace('\f'));
    }

    @Test
    public void testIsWhitespaceCarriageReturn() {
        assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsFormerWhitespaceNonBreakingSpace() {
        assertTrue(StringUtil.isActuallyWhitespace(160));
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "Hello   World!\nHow are\tyou?";
        String expectedResult = "Hello World! How are you?";

        String result = StringUtil.normaliseWhitespace(string);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAppendNormalisedWhitespace() {
        StringBuilder sb = new StringBuilder();
        String string = "Hello   World!\nHow are\tyou?";
        String expectedResult = "Hello World! How are you?";

        StringUtil.appendNormalisedWhitespace(sb, string, false);

        assertEquals(expectedResult, sb.toString());
    }

    @Test
    public void testInMatchString() {
        String needle = "World";
        String[] haystack = new String[]{"Hello", "World", "!"};

        assertTrue(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInNoMatchString() {
        String needle = "Goodbye";
        String[] haystack = new String[]{"Hello", "World", "!"};

        assertFalse(StringUtil.in(needle, haystack));
    }

    @Test
    public void testInSortedMatchString() {
        String needle = "World";
        String[] haystack = new String[]{"!", "Hello", "World"};

        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testInSortedNoMatchString() {
        String needle = "Goodbye";
        String[] haystack = new String[]{"!", "Hello", "World"};

        assertFalse(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testResolveRelativeUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "page.html";
        String expectedResult = "http://www.example.com/page.html";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL(expectedResult), result);
    }

    @Test
    public void testResolveAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "http://www.otherexample.com/page.html";
        String expectedResult = "http://www.otherexample.com/page.html";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL(expectedResult), result);
    }

    @Test
    public void testResolveQuestionMarkUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com/page.html");
        String relUrl = "?param=1";
        String expectedResult = "http://www.example.com/page.html?param=1";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL(expectedResult), result);
    }

    @Test
    public void testResolveDotUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com");
        String relUrl = "./page.html";
        String expectedResult = "http://www.example.com/page.html";

        URL result = StringUtil.resolve(base, relUrl);

        assertEquals(new URL(expectedResult), result);
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        String baseUrl = "";
        String relUrl = "page.html";
        String expectedResult = "";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testResolveMalformedUrl() {
        String baseUrl = "http://www.example.com";
        String relUrl = "malformed url";
        String expectedResult = "";

        String result = StringUtil.resolve(baseUrl, relUrl);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        assertNotSame(sb1, sb2);
        assertTrue(sb1.length() <= StringUtil.MaxCachedBuilderSize);
        assertTrue(sb2.length() <= StringUtil.MaxCachedBuilderSize);
    }
}