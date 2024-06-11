
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilter_withNullInput() {
        assertEquals(null, HTMLFilter.filter(null));
    }

    @Test
    public void testFilter_withNormalInput() {
        assertEquals("Hello & World", HTMLFilter.filter("Hello & World"));
    }

    @Test
    public void testFilter_withEmptyStringInput() {
        assertEquals("", HTMLFilter.filter(""));
    }

    @Test
    public void testFilter_withSpecialCharsInput() {
        assertEquals("&lt; &gt; &amp; &quot;", HTMLFilter.filter("< > & \""));
    }

}
