package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString() {
        String result = Util.stripLeadingHyphens(null);
        assertEquals(null, result);
    }
    
    @Test
    public void testStripLeadingHyphens_doubleHyphens() {
        String result = Util.stripLeadingHyphens("--example");
        assertEquals("example", result);
    }
    
    @Test
    public void testStripLeadingHyphens_singleHyphen() {
        String result = Util.stripLeadingHyphens("-example");
        assertEquals("example", result);
    }
    
    @Test
    public void testStripLeadingHyphens_noHyphen() {
        String result = Util.stripLeadingHyphens("example");
        assertEquals("example", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_singleQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("'example'");
        assertEquals("example", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_doubleQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("\"example\"");
        assertEquals("example", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("example");
        assertEquals("example", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_mixedQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("'example\"");
        assertEquals("'example\"", result);
    }
}
