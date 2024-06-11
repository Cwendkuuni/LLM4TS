
package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        Assert.assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_NoHtmlTagsInMessage_ReturnsMessageUnchanged() {
        String message = "This is a test message.";
        Assert.assertEquals(message, HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_LessThanSymbolInMessage_ReturnsLessThanSymbolEscaped() {
        String message = "This is a < test message.";
        Assert.assertEquals("This is a &lt; test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_GreaterThanSymbolInMessage_ReturnsGreaterThanSymbolEscaped() {
        String message = "This is a > test message.";
        Assert.assertEquals("This is a &gt; test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_AmpersandSymbolInMessage_ReturnsAmpersandSymbolEscaped() {
        String message = "This is a & test message.";
        Assert.assertEquals("This is a &amp; test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_QuoteSymbolInMessage_ReturnsQuoteSymbolEscaped() {
        String message = "This is a \" test message.";
        Assert.assertEquals("This is a &quot; test message.", HTMLFilter.filter(message));
    }

    @Test
    public void testFilter_MixedSymbolsInMessage_ReturnsSymbolsEscaped() {
        String message = "<this> &is& \"a\" >test< message";
        Assert.assertEquals("&lt;this&gt; &amp;is&amp; &quot;a&quot; &gt;test&lt; message", HTMLFilter.filter(message));
    }
  
}
