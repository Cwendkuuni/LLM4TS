package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilterWithNullMessage() {
        String result = HTMLFilter.filter(null);
        Assert.assertNull(result);
    }

    @Test
    public void testFilterWithEmptyMessage() {
        String result = HTMLFilter.filter("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testFilterWithNoHTMLCharacters() {
        String message = "This is a normal message.";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals(message, result);
    }

    @Test
    public void testFilterWithHTMLEntities() {
        String message = "<p>This is a paragraph.</p>";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals("&lt;p&gt;This is a paragraph.&lt;/p&gt;", result);
    }

    @Test
    public void testFilterWithMultipleHTMLEntities() {
        String message = "<p>This is a paragraph with &amp;quot;quotes&amp;quot;.</p>";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals("&lt;p&gt;This is a paragraph with &amp;amp;quot;quotes&amp;amp;quot;.&lt;/p&gt;", result);
    }

    @Test
    public void testFilterWithMixedCharacters() {
        String message = "<p>This &amp; that \"quoted\".</p>";
        String result = HTMLFilter.filter(message);
        Assert.assertEquals("&lt;p&gt;This &amp;amp; that &amp;quot;quoted&amp;quot;.&lt;/p&gt;", result);
    }
}