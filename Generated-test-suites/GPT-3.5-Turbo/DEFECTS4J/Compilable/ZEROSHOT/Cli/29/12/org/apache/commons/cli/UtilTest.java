package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens_nullString() {
        String str = null;
        assertNull(Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_doubleHyphens() {
        String str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_singleHyphen() {
        String str = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_noHyphen() {
        String str = "test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithQuote() {
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_endsWithQuote() {
        String str = "test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        String str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_emptyString() {
        String str = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }
}