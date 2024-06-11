package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String expected = "";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorOneElement() {
        Iterator<String> strings = Arrays.asList("test").iterator();
        String expected = "test";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorMultipleElements() {
        Iterator<String> strings = Arrays.asList("test1", "test2", "test3").iterator();
        String expected = "test1test2test3";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String expected = "";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayOneElement() {
        String[] strings = new String[]{"test"};
        String expected = "test";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayMultipleElements() {
        String[] strings = new String[]{"test1", "test2", "test3"};
        String expected = "test1test2test3";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String actual = StringUtil.padding(-1);
    }

    @Test
    public void testIsBlankNullString() {
        boolean actual = StringUtil.isBlank(null);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankEmptyString() {
        boolean actual = StringUtil.isBlank("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankOnlyWhitespace() {
        String string = "   hello   ";
        boolean actual = StringUtil.isBlank(string);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNotBlank() {
        String string = "hello world";
        boolean actual = StringUtil.isBlank(string);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericTrue() {
        String actual = StringUtil.isNumeric("12345");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String actual = StringUtil.isNumeric("");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String actual = StringUtil.isNumeric("abc");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTrueSpace() {
        boolean actual = StringUtil.isWhitespace(' ');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTrueTab() {
        boolean actual = StringUtil.isWhitespace('\t');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTrueNewline() {
        boolean actual = StringUtil.isWhitespace('\n');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceFalseOther() {
        boolean actual = StringUtil.isWhitespace('x');
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrueSpace() {
        boolean actual = StringUtil.isActuallyWhitespace(' ');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrueTab() {
        boolean actual = StringUtil.isActuallyWhitespace('\t');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrueNewline() {
        boolean actual = StringUtil.isActuallyWhitespace('\n');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTrueNonBreakingSpace() {
        boolean actual = StringUtil.isActuallyWhitespace('\u00A0');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceFalseOther() {
        boolean actual = StringUtil.isActuallyWhitespace('x');
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String actual = StringUtil.normaliseWhitespace("");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceOnlySpace() {
        String actual = StringUtil.normaliseWhitespace("   hello   ");
        String expected = "hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String actual = StringUtil.normaliseWhitespace("  hello \r world\n  ");
        String expected = "helloworld";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNullNeedle() {
        boolean actual = StringUtil.in(null, "foo", "bar");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInEmptyNeedle() {
        boolean actual = StringUtil.in("", "foo", "bar");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNeedleInArray() {
        boolean actual = StringUtil.in("foo", "foo", "bar", "baz");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNeedleNotInArray() {
        boolean actual = StringUtil.in("bar", "foo", "bar", "baz");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveEmptyBase() {
        String actual = StringUtil.resolve(null, "test");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveEmptyRel() {
        String base = "http://example.com";
        String actual = StringUtil.resolve(base, "");
        String expected = base;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsRel() {
        String base = "http://example.com";
        String rel = "/test";
        String actual = StringUtil.resolve(base, rel);
        String expected = base + rel;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsBaseAbsRel() {
        String base = "http://example.com";
        String rel = "http://example.org/test";
        String actual = StringUtil.resolve(base, rel);
        String expected = rel;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelRel() {
        String base = "http://example.com/path1";
        String rel = "path2/test";
        String actual = StringUtil.resolve(base, rel);
        String expected = base + "/" + rel;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelAbs() {
        String base = "http://example.com/path1";
        String rel = "/path2/test";
        String actual = StringUtil.resolve(base, rel);
        String expected = rel;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveMalformedBase() {
        String base = "http://example.coma";
        String rel = "/test";
        try {
            String actual = StringUtil.resolve(base, rel);
            Assert.fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testResolveMalformedRel() {
        String base = "http://example.com";
        String rel = "/test/";
        try {
            String actual = StringUtil.resolve(base, rel);
            Assert.fail();
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testStringBuilder() {
        StringBuilder actual = StringUtil.stringBuilder();
        StringBuilder expected = new StringBuilder(8 * 1024);
        Assert.assertSame(expected, actual);
    }
}