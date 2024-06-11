package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens_emptyString() {
        String result = Util.stripLeadingHyphens("");
        assertEquals("", result);
    }
    
    @Test
    public void testStripLeadingHyphens_singleHyphen() {
        String result = Util.stripLeadingHyphens("-");
        assertEquals("", result);
    }
    
    @Test
    public void testStripLeadingHyphens_doubleHyphen() {
        String result = Util.stripLeadingHyphens("--");
        assertEquals("", result);
    }
    
    @Test
    public void testStripLeadingHyphens_singleCharacter() {
        String result = Util.stripLeadingHyphens("-f");
        assertEquals("f", result);
    }
    
    @Test
    public void testStripLeadingHyphens_multipleCharacters() {
        String result = Util.stripLeadingHyphens("--foo");
        assertEquals("foo", result);
    }
    
    @Test
    public void testStripLeadingHyphens_multipleHyphens() {
        String result = Util.stripLeadingHyphens("---foo");
        assertEquals("-foo", result);
    }
    
    @Test
    public void testStripLeadingHyphens_null() {
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_emptyString() {
        String result = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("foo");
        assertEquals("foo", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_singleQuote() {
        String result = Util.stripLeadingAndTrailingQuotes("\"foo");
        assertEquals("\"foo", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_doubleQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"foo\"");
        assertEquals("foo", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_quotesAtStartAndEnd() {
        String result = Util.stripLeadingAndTrailingQuotes("\"foo\" bar");
        assertEquals("foo\" bar", result);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_quotesInMiddle() {
        String result = Util.stripLeadingAndTrailingQuotes("foo \"bar\"");
        assertEquals("foo \"bar\"", result);
    }
}