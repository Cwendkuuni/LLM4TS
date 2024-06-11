package com.imsmart.servlet;

import org.junit.Assert;
import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        String message = null;
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertNull(filteredMessage);
    }

    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        String message = "";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals("", filteredMessage);
    }

    @Test
    public void testFilter_MessageWithNoSpecialCharacters_ReturnsSameMessage() {
        String message = "This is a normal message without any special characters.";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals(message, filteredMessage);
    }

    @Test
    public void testFilter_MessageWithLessThan_ReturnsFilteredMessage() {
        String message = "This is a message with a <tag>.";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals("This is a message with a &lt;tag>.", filteredMessage);
    }

    @Test
    public void testFilter_MessageWithGreaterThan_ReturnsFilteredMessage() {
        String message = "This is a message with a >tag<.";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals("This is a message with a &gt;tag&lt;.", filteredMessage);
    }

    @Test
    public void testFilter_MessageWithAmpersand_ReturnsFilteredMessage() {
        String message = "This is a message with an & character.";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals("This is a message with an &amp; character.", filteredMessage);
    }

    @Test
    public void testFilter_MessageWithDoubleQuotes_ReturnsFilteredMessage() {
        String message = "This is a message with \"double quotes\".";
        String filteredMessage = HTMLFilter.filter(message);
        Assert.assertEquals("This is a message with &quot;double quotes&quot;.", filteredMessage);
    }

}