package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_withNullMessage_returnsNull() {
        String filteredMessage = HTMLFilter.filter(null);
        assertNull(filteredMessage);
    }

    @Test
    public void testFilter_withEmptyMessage_returnsEmptyString() {
        String filteredMessage = HTMLFilter.filter("");
        assertEquals("", filteredMessage);
    }

    @Test
    public void testFilter_withMessageWithoutTags_returnsSameMessage() {
        String message = "This is a test message.";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals(message, filteredMessage);
    }

    @Test
    public void testFilter_withMessageWithTags_returnsFilteredMessage() {
        String message = "<h1>Test</h1><p>This is a test message.</p>";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("&lt;h1&gt;Test&lt;/h1&gt;&lt;p&gt;This is a test message.&lt;/p&gt;", filteredMessage);
    }

    @Test
    public void testFilter_withMessageWithSpecialCharacters_returnsFilteredMessage() {
        String message = "Hello & < World > \"";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("Hello &amp; &lt; World &gt; &quot;", filteredMessage);
    }

    @Test
    public void testFilter_withMessageContainingMixedTagsAndSpecialCharacters_returnsFilteredMessage() {
        String message = "<p>Hello & < World > \"</p>";
        String filteredMessage = HTMLFilter.filter(message);
        assertEquals("&lt;p&gt;Hello &amp; &lt; World &gt; &quot;&lt;/p&gt;", filteredMessage);
    }
}


