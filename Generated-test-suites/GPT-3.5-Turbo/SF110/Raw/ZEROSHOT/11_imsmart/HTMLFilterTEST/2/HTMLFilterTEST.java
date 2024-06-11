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
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String result = HTMLFilter.filter("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testFilter_MessageWithSpecialCharacters_ReturnsFilteredString() {
        String input = "<script>alert('Hello & Welcome!');</script>";
        String expected = "&lt;script&gt;alert(&apos;Hello &amp; Welcome!&apos;);&lt;/script&gt;";

        String result = HTMLFilter.filter(input);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFilter_MessageWithoutSpecialCharacters_ReturnsSameString() {
        String input = "This is a test message without any special characters.";
        String result = HTMLFilter.filter(input);

        Assert.assertEquals(input, result);
    }
}