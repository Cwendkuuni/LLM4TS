
package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage() {
        String message = null;
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertNull(filteredMessage);
    }

    @Test
    public void testFilter_EmptyMessage() {
        String message = "";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals("", filteredMessage);
    }

    @Test
    public void testFilter_NoSpecialCharacters() {
        String message = "This is a plain message without any special character.";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals(message, filteredMessage);
    }

    @Test
    public void testFilter_WithSpecialCharacters() {
        String message = "<script>alert('XSS');</script>";
        String expected = "&lt;script&gt;alert(&#039;XSS&#039;);&lt;/script&gt;";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filteredMessage);
    }

    @Test
    public void testFilter_WithMultipleSpecialCharacters() {
        String message = "<hello> & \"world\"";
        String expected = "&lt;hello&gt; &amp; &quot;world&quot;";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filteredMessage);
    }
    
    @Test
    public void testFilter_WithInvertedSpecialCharacters() {
        String message = ">test<&?";
        String expected = "&gt;test&lt;&amp;?";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filteredMessage);
    }

    @Test
    public void testFilter_WithNullCharacter() {
        String message = "Null\u0000Char";
        String expected = "Null\u0000Char";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals(expected, filteredMessage);
    }
  
    @Test
    public void testFilter_LargeMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            sb.append("<div>").append(i).append("</div>");
        }
        String message = sb.toString();
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertNotNull(filteredMessage);
        Assert.assertTrue(filteredMessage.length() > 0);
    }

}

