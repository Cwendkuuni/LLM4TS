package com.imsmart.servlet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_basic() {
        String input = "<p>Test & Test</p>";
        String expected = "&lt;p&gt;Test &amp; Test&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_nullInput() {
        String input = null;
        String result = HTMLFilter.filter(input);
        assertEquals(null, result);
    }
    
    @Test
    public void testFilter_emptyInput() {
        String input = "";
        String result = HTMLFilter.filter(input);
        assertEquals("", result);
    }
    
    @Test
    public void testFilter_edgeCases() {
        String input = "<>&\"'"; // all special characters
        String expected = "&lt;&gt;&amp;&quot;&apos;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFilter_invalidInput() {
        String input = "Test <script>alert('XSS Attack')</script>";
        String expected = "Test &lt;script&gt;alert('XSS Attack')&lt;/script&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}