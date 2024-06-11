package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String result = HTMLFilter.filter(null);
        Assert.assertNull(result);
    }

    @Test
    public void testFilter_MessageWithNoHTMLChars_ReturnsSameMessage() {
        String message = "Hello World!";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(message, result);
    }

    @Test
    public void testFilter_MessageWithLTChar_ReplacesWithHTMLLT() {
        String message = "10 < 20";
        String expected = "10 &lt; 20";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithGTChar_ReplacesWithHTMLGT() {
        String message = "20 > 10";
        String expected = "20 &gt; 10";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithAmpChar_ReplacesWithHTMLAmp() {
        String message = "AT&T";
        String expected = "AT&amp;T";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithQuoteChar_ReplacesWithHTMLQuote() {
        String message = "\"Hello\"";
        String expected = "&quot;Hello&quot;";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithMultipleHTMLChars_ReplacesAllChars() {
        String message = "<b>Hello & Welcome</b>";
        String expected = "&lt;b&gt;Hello &amp; Welcome&lt;/b&gt;";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expected, result);
    }

}