package org.jsoup.helper;
import org.junit.Test;
import org.junit.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("a", "b", "c");
        String result = StringUtil.join(list, ",");
        Assert.assertEquals("a,b,c", result);
    }

    @Test
    public void testJoinIterator() {
        List<String> list = Arrays.asList("a", "b", "c");
        Iterator<String> iterator = list.iterator();
        String result = StringUtil.join(iterator, ",");
        Assert.assertEquals("a,b,c", result);
    }

    @Test
    public void testJoinArray() {
        String[] array = {"a", "b", "c"};
        String result = StringUtil.join(array, ",");
        Assert.assertEquals("a,b,c", result);
    }

    @Test
    public void testPadding() {
        String result = StringUtil.padding(5);
        Assert.assertEquals("     ", result);
    }

    @Test
    public void testIsBlank() {
        boolean result1 = StringUtil.isBlank(null);
        boolean result2 = StringUtil.isBlank("");
        boolean result3 = StringUtil.isBlank(" ");
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
    }

    @Test
    public void testIsNumeric() {
        boolean result1 = StringUtil.isNumeric(null);
        boolean result2 = StringUtil.isNumeric("");
        boolean result3 = StringUtil.isNumeric("12345");
        Assert.assertFalse(result1);
        Assert.assertFalse(result2);
        Assert.assertTrue(result3);
    }

    @Test
    public void testIsWhitespace() {
        boolean result1 = StringUtil.isWhitespace(' ');
        boolean result2 = StringUtil.isWhitespace('\t');
        boolean result3 = StringUtil.isWhitespace('\n');
        boolean result4 = StringUtil.isWhitespace('\f');
        boolean result5 = StringUtil.isWhitespace('\r');
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
        Assert.assertTrue(result4);
        Assert.assertTrue(result5);
    }

    @Test
    public void testIsActuallyWhitespace() {
        boolean result1 = StringUtil.isActuallyWhitespace(' ');
        boolean result2 = StringUtil.isActuallyWhitespace('\t');
        boolean result3 = StringUtil.isActuallyWhitespace('\n');
        boolean result4 = StringUtil.isActuallyWhitespace('\f');
        boolean result5 = StringUtil.isActuallyWhitespace('\r');
        boolean result6 = StringUtil.isActuallyWhitespace(160);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
        Assert.assertTrue(result4);
        Assert.assertTrue(result5);
        Assert.assertTrue(result6);
    }

    @Test
    public void testNormaliseWhitespace() {
        String result = StringUtil.normaliseWhitespace("     hello   \n\tworld");
        Assert.assertEquals(" hello world", result);
    }

    @Test
    public void testIn() {
        boolean result1 = StringUtil.in("apple", "banana", "apple", "orange");
        boolean result2 = StringUtil.in("grape", "kiwi", "mango");
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testInSorted() {
        String[] array = {"apple", "banana", "kiwi", "orange"};
        boolean result1 = StringUtil.inSorted("banana", array);
        boolean result2 = StringUtil.inSorted("grape", array);
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testResolveURL() throws MalformedURLException {
        URL base = new URL("https://www.example.com");
        String relUrl = "/page.html";
        URL result = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(new URL("https://www.example.com/page.html"), result);
    }

    @Test
    public void testResolveString() {
        String baseUrl = "https://www.example.com";
        String relUrl = "/page.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("https://www.example.com/page.html", result);
    }

}