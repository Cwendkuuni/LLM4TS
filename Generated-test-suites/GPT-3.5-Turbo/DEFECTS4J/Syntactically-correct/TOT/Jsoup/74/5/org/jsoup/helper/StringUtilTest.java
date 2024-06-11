
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        // empty collection
        String result1 = StringUtil.join(new ArrayList<>(), ",");
        assertEquals("", result1);

        // collection with one element
        List<String> collection2 = Arrays.asList("element");
        String result2 = StringUtil.join(collection2, ",");
        assertEquals("element", result2);

        // collection with multiple elements
        List<String> collection3 = Arrays.asList("element1", "element2", "element3");
        String result3 = StringUtil.join(collection3, ",");
        assertEquals("element1,element2,element3", result3);
    }

    @Test
    public void testJoinIterator() {
        // empty iterator
        String result1 = StringUtil.join(Collections.emptyIterator(), ",");
        assertEquals("", result1);

        // iterator with one element
        List<String> list2 = Arrays.asList("element");
        String result2 = StringUtil.join(list2.iterator(), ",");
        assertEquals("element", result2);

        // iterator with multiple elements
        List<String> list3 = Arrays.asList("element1", "element2", "element3");
        String result3 = StringUtil.join(list3.iterator(), ",");
        assertEquals("element1,element2,element3", result3);
    }

    @Test
    public void testJoinArray() {
        // empty array
        String[] array1 = new String[0];
        String result1 = StringUtil.join(array1, ",");
        assertEquals("", result1);

        // array with one element
        String[] array2 = new String[1];
        array2[0] = "element";
        String result2 = StringUtil.join(array2, ",");
        assertEquals("element", result2);

        // array with multiple elements
        String[] array3 = {"element1", "element2", "element3"};
        String result3 = StringUtil.join(array3, ",");
        assertEquals("element1,element2,element3", result3);
    }

    @Test
    public void testPadding() {
        // width is less than 0
        try {
            StringUtil.padding(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // pass
        }

        // width is 0
        String result1 = StringUtil.padding(0);
        assertEquals("", result1);

        // width is less than the length of padding array
        for (int i = 1; i < StringUtil.padding.length; i++) {
            String result = StringUtil.padding(i);
            assertEquals(StringUtil.padding[i], result);
        }

        // width is greater than or equal to the length of padding array
        int width = 22;
        String expected = "                      ";
        String result2 = StringUtil.padding(width);
        assertEquals(expected, result2);
    }

    @Test
    public void testIsBlank() {
        // string is null
        assertTrue(StringUtil.isBlank(null));

        // string is empty
        assertTrue(StringUtil.isBlank(""));

        // string contains only whitespace characters
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("\t"));
        assertTrue(StringUtil.isBlank("\n"));
        assertTrue(StringUtil.isBlank("\r\n"));

        // string contains non-whitespace characters
        assertFalse(StringUtil.isBlank("text"));
        assertFalse(StringUtil.isBlank("  text  "));
    }

    @Test
    public void testIsNumeric() {
        // string is null
        assertFalse(StringUtil.isNumeric(null));

        // string is empty
        assertFalse(StringUtil.isNumeric(""));

        // string contains only digit characters
        assertTrue(StringUtil.isNumeric("123"));

        // string contains non-digit characters
        assertFalse(StringUtil.isNumeric("123a"));
        assertFalse(StringUtil.isNumeric("abc"));
    }

    @Test
    public void testIsWhitespace() {
        // code point is a whitespace character
        assertTrue(StringUtil.isWhitespace(' '));
        assertTrue(StringUtil.isWhitespace('\t'));
        assertTrue(StringUtil.isWhitespace('\n'));
        assertTrue(StringUtil.isWhitespace('\f'));
        assertTrue(StringUtil.isWhitespace('\r'));

        // code point is not a whitespace character
        assertFalse(StringUtil.isWhitespace('a'));
        assertFalse(StringUtil.isWhitespace('1'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        // code point is a whitespace character
        assertTrue(StringUtil.isActuallyWhitespace(' '));
        assertTrue(StringUtil.isActuallyWhitespace('\t'));
        assertTrue(StringUtil.isActuallyWhitespace('\n'));
        assertTrue(StringUtil.isActuallyWhitespace('\f'));
        assertTrue(StringUtil.isActuallyWhitespace('\r'));
        assertTrue(StringUtil.isActuallyWhitespace(160));

        // code point is not a whitespace character
        assertFalse(StringUtil.isActuallyWhitespace('a'));
        assertFalse(StringUtil.isActuallyWhitespace('1'));
    }

    @Test
    public void testNormaliseWhitespace() {
        // string is null
        String result1 = StringUtil.normaliseWhitespace(null);
        assertEquals("", result1);

        // string is empty
        String result2 = StringUtil.normaliseWhitespace("");
        assertEquals("", result2);

        // string contains multiple spaces and whitespace characters
        String result3 = StringUtil.normaliseWhitespace("  \t  \n  ");
        assertEquals(" ", result3);

        // string contains non-whitespace characters
        String result4 = StringUtil.normaliseWhitespace("  text  ");
        assertEquals(" text ", result4);
    }

    @Test
    public void testIn() {
        // haystack is empty
        assertFalse(StringUtil.in("needle"));

        // haystack contains needle
        String[] haystack2 = {"needle"};
        assertTrue(StringUtil.in("needle", haystack2));

        // haystack does not contain needle
        String[] haystack3 = {"item1", "item2"};
        assertFalse(StringUtil.in("needle", haystack3));
    }

    @Test
    public void testInSorted() {
        // haystack is empty
        assertFalse(StringUtil.inSorted("needle", new String[0]));

        // haystack contains needle
        String[] haystack2 = {"apple", "banana", "needle"};
        assertTrue(StringUtil.inSorted("needle", haystack2));

        // haystack does not contain needle
        String[] haystack3 = {"apple", "banana"};
        assertFalse(StringUtil.inSorted("needle", haystack3));
    }

    @Test
    public void testResolve() {
        // base URL + rel URL contains "?"
        try {
            URL base = new URL("https://example.com");
            String relUrl = "?param=value";
            URL resolvedUrl = StringUtil.resolve(base, relUrl);
            assertEquals("https://example.com/?param=value", resolvedUrl.toString());
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException thrown");
        }

        // base URL does not start with "/" and rel URL starts with "."
        try {
            URL base = new URL("https://example.com");
            String relUrl = "./foo";
            URL resolvedUrl = StringUtil.resolve(base, relUrl);
            assertEquals("https://example.com/./foo", resolvedUrl.toString());
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException thrown");
        }
    }

    @Test
    public void testResolveString() {
        // base URL is invalid
        try {
            String baseUrl = "https://example com";
            String relUrl = "subpage";
            String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
            assertEquals("", resolvedUrl);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        // base URL is valid and rel URL is invalid
        try {
            String baseUrl = "https://example.com";
            String relUrl = "https://invalid.com";
            String resolvedUrl = StringUtil.resolve(baseUrl, relUrl);
            assertEquals("https://invalid.com", resolvedUrl);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        assertNotNull(sb1);
        assertEquals(0, sb1.length());

        StringBuilder sb2 = StringUtil.stringBuilder();
        assertSame(sb1, sb2);
    }
}

