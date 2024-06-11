package com.imsmart.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        // Test for empty string
        String emptyString = "";
        assertEquals("", HTMLFilter.filter(emptyString));
        
        // Test for string with HTML special characters
        String message = "<div>Hello & Welcome</div>";
        String expectedOutput = "&lt;div&gt;Hello &amp; Welcome&lt;/div&gt;";
        assertEquals(expectedOutput, HTMLFilter.filter(message));
        
        // Test for string with only HTML special characters
        String specialCharacters = "<>&\"";
        expectedOutput = "&lt;&gt;&amp;&quot;";
        assertEquals(expectedOutput, HTMLFilter.filter(specialCharacters));
        
        // Test for string with mixed content
        String mixedContent = "This is a test message <p>Please ignore it & don't forget to <a href=\"#\">click here</a></p>";
        expectedOutput = "This is a test message &lt;p&gt;Please ignore it &amp; don't forget to &lt;a href=\"#\"&gt;click here&lt;/a&gt;&lt;/p&gt;";
        assertEquals(expectedOutput, HTMLFilter.filter(mixedContent));
        
        // Test for null input
        assertNull(HTMLFilter.filter(null));
    }
}