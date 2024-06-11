package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_nullMessage() {
        String input = null;
        String output = HTMLFilter.filter(input);
        Assert.assertNull(output);
    }

    @Test
    public void testFilter_emptyMessage() {
        String input = "";
        String output = HTMLFilter.filter(input);
        Assert.assertEquals("", output);
    }

    @Test
    public void testFilter_noSpecialCharacters() {
        String input = "This is a test message.";
        String output = HTMLFilter.filter(input);
        Assert.assertEquals(input, output);
    }

    @Test
    public void testFilter_withSpecialCharacters() {
        String input = "<p>This is a test message.</p>";
        String expectedOutput = "&lt;p&gt;This is a test message.&lt;/p&gt;";
        String output = HTMLFilter.filter(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilter_multipleSpecialCharacters() {
        String input = "<strong>This is a &amp;test&amp; message.</strong>";
        String expectedOutput = "&lt;strong&gt;This is a &amp;amp;test&amp;amp; message.&lt;/strong&gt;";
        String output = HTMLFilter.filter(input);
        Assert.assertEquals(expectedOutput, output);
    }

    // Add more test methods to cover all scenarios

}