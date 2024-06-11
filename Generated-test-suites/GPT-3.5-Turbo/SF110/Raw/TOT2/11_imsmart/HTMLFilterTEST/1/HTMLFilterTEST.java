package com.imsmart.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import com.imsmart.servlet.HTMLFilter;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        String message = "<p>Hello, world!</p>";
        String expected = "&lt;p&gt;Hello, world!&lt;/p&gt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterNull() {
        String message = null;
        assertNull(HTMLFilter.filter(message));
    }

    @Test
    public void testFilterEdgeCases() {
        String message = "\"<script> alert('XSS attack') </script>\"";
        String expected = "&quot;&lt;script&gt; alert('XSS attack') &lt;/script&gt;&quot;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}
