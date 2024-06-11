
import org.jsoup.helper.StringUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringUtilTest {

    @Test
    public void join_CollectionOfStrings_ReturnsJoinedString() {
        List<String> list = Arrays.asList("Hello", "World", "!");
        String sep = "-";
        
        String result = StringUtil.join(list, sep);
        
        Assert.assertEquals("Hello-World-!", result);
    }

    @Test
    public void join_IteratorOfStrings_ReturnsJoinedString() {
        List<String> list = Arrays.asList("Hello", "World", "!");
        String sep = "-";
        Iterator<String> iterator = list.iterator();
        
        String result = StringUtil.join(iterator, sep);
        
        Assert.assertEquals("Hello-World-!", result);
    }

    @Test
    public void join_ArrayOfStrings_ReturnsJoinedString() {
        String[] strings = {"Hello", "World", "!"};
        String sep = "-";
        
        String result = StringUtil.join(strings, sep);
        
        Assert.assertEquals("Hello-World-!", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void padding_WidthLessThanZero_ThrowsIllegalArgumentException() {
        int width = -1;
        
        StringUtil.padding(width);
    }

    @Test
    public void padding_ValidWidth_ReturnsSpacePadding() {
        int width = 5;
        String expected = "     ";
        
        String result = StringUtil.padding(width);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void isBlank_NullString_ReturnsTrue() {
        String string = null;
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isBlank_EmptyString_ReturnsTrue() {
        String string = "";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isBlank_WhitespaceString_ReturnsTrue() {
        String string = "       ";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isBlank_NonBlankString_ReturnsFalse() {
        String string = "Not blank";
        
        boolean result = StringUtil.isBlank(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_NullString_ReturnsFalse() {
        String string = null;
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_EmptyString_ReturnsFalse() {
        String string = "";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isNumeric_OnlyDigits_ReturnsTrue() {
        String string = "12345";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isNumeric_NonNumericString_ReturnsFalse() {
        String string = "12345a";
        
        boolean result = StringUtil.isNumeric(string);
        
        Assert.assertFalse(result);
    }

    @Test
    public void isWhitespace_Space_ReturnsTrue() {
        int c = ' ';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isWhitespace_Tab_ReturnsTrue() {
        int c = '\t';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isWhitespace_Newline_ReturnsTrue() {
        int c = '\n';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isWhitespace_FormFeed_ReturnsTrue() {
        int c = '\f';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isWhitespace_CarriageReturn_ReturnsTrue() {
        int c = '\r';
        
        boolean result = StringUtil.isWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_Space_ReturnsTrue() {
        int c = ' ';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_Tab_ReturnsTrue() {
        int c = '\t';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_Newline_ReturnsTrue() {
        int c = '\n';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_FormFeed_ReturnsTrue() {
        int c = '\f';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_CarriageReturn_ReturnsTrue() {
        int c = '\r';
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void isActuallyWhitespace_NonBreakingSpace_ReturnsTrue() {
        int c = 160;
        
        boolean result = StringUtil.isActuallyWhitespace(c);
        
        Assert.assertTrue(result);
    }

    @Test
    public void normaliseWhitespace_MultipleSpaces_ConvertsToSingleSpace() {
        String string = "Hello    World!";
        String expected = "Hello World!";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void normaliseWhitespace_NewlineConvertsToSpace() {
        String string = "Hello\nWorld!";
        String expected = "Hello World!";
        
        String result = StringUtil.normaliseWhitespace(string);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void in_StringInHaystack_ReturnsTrue() {
        String needle = "World";
        String[] haystack = {"Hello", "World", "!"};
        
        boolean result = StringUtil.in(needle, haystack);
        
        Assert.assertTrue(result);
    }

    @Test
    public void in_StringNotInHaystack_ReturnsFalse() {
        String needle = "Goodbye";
        String[] haystack = {"Hello", "World", "!"};
        
        boolean result = StringUtil.in(needle, haystack);
        
        Assert.assertFalse(result);
    }

    @Test
    public void inSorted_StringInHaystack_ReturnsTrue() {
        String needle = "World";
        String[] haystack = {"!", "Hello", "World"};
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        Assert.assertTrue(result);
    }

    @Test
    public void inSorted_StringNotInHaystack_ReturnsFalse() {
        String needle = "Goodbye";
        String[] haystack = {"!", "Hello", "World"};
        
        boolean result = StringUtil.inSorted(needle, haystack);
        
        Assert.assertFalse(result);
    }

    @Test
    public void resolve_RelativeUrl_ReturnsResolvedAbsoluteUrl() throws MalformedURLException {
        URL base = new URL("http://www.example.com/base/");
        String relUrl = "subpage/";
        String expected = "http://www.example.com/base/subpage/";
        
        URL result = StringUtil.resolve(base, relUrl);
        
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void resolve_InvalidBaseUrl_ReturnsEmptyString() {
        String baseUrl = "invalid";
        String relUrl = "subpage/";
        String expected = "";
        
        String result = StringUtil.resolve(baseUrl, relUrl);
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void stringBuilder_CalledMultipleTimes_ReturnsEmptyStringBuilder() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();
        StringBuilder sb3 = StringUtil.stringBuilder();
        
        Assert.assertEquals("", sb1.toString());
        Assert.assertEquals("", sb2.toString());
        Assert.assertEquals("", sb3.toString());
    }

    @Test
    public void stringBuilder_GrownTooBig_ReturnsNewStringBuilder() {
        // MaxCachedBuilderSize = 8192
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MaxCachedBuilderSize + 1; i++) {
            sb.append("a");
        }
        
        StringBuilder sbNew = StringUtil.stringBuilder();
        
        Assert.assertNotSame(sb, sbNew);
        Assert.assertEquals(0, sbNew.length());
    }
}

