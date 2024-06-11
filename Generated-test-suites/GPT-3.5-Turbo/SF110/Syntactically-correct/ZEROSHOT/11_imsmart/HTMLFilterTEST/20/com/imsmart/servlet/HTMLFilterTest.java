package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String result = HTMLFilter.filter(null);
        Assert.assertNull(result);
    }

    @Test
    public void testFilterEmptyMessage() {
        String result = HTMLFilter.filter("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testFilterNoSpecialCharacters() {
        String message = "This message does not contain any special characters";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(message, result);
    }

    @Test
    public void testFilterWithLessThan() {
        String message = "This message contains a < tag";
        String expectedResult = "This message contains a &lt; tag";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFilterWithGreaterThan() {
        String message = "This message contains a > tag";
        String expectedResult = "This message contains a &gt; tag";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFilterWithAmpersand() {
        String message = "This message contains an & character";
        String expectedResult = "This message contains an &amp; character";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFilterWithDoubleQuote() {
        String message = "This message contains a \" character";
        String expectedResult = "This message contains a &quot; character";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(expectedResult, result);
    }
}