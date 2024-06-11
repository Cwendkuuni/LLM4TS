package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilterBasic() {
        String original = "This is a <b>test</b> message";
        String filtered = HTMLFilter.filter(original);
        assertEquals("This is a &lt;b&gt;test&lt;/b&gt; message", filtered);
    }

    @Test
    public void testFilterNull() {
        assertNull(HTMLFilter.filter(null));
    }

    @Test
    public void testFilterEmptyString() {
        String original = "";
        assertEquals("", HTMLFilter.filter(original));
    }

    @Test
    public void testFilterEdgeCases() {
        // Test for special characters
        String original = "<>&\"";
        String filtered = HTMLFilter.filter(original);
        assertEquals("&lt;&gt;&amp;&quot;", filtered);
    }

    @Test
    public void testFilterLongMessage() {
        // Test for a message with maximum possible length
        String original = "A".repeat(Integer.MAX_VALUE);
        String filtered = HTMLFilter.filter(original);
        // This will test if the method can handle a very long input
        assertNotNull(filtered);
        // This will test if the output length is correct
        assertEquals(original.length() + 4, filtered.length());
    }

    @Test
    public void testFilterWithOtherSpecialCharacters() {
        // Test for other special characters
        String original = "©®™";
        String filtered = HTMLFilter.filter(original);
        assertEquals(original, filtered);
    }

    @Test
    public void testFilterPerformance() {
        // Test for performance by filtering a large string multiple times
        String original = "This is a <b>test</b> message";
        for (int i = 0; i < 1000000; i++) {
            HTMLFilter.filter(original);
        }
    }
}