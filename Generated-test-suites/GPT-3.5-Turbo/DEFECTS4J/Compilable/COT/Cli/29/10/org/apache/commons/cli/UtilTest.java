package org.apache.commons.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_withDoubleHyphens() {
        // Test input: "--test"
        // Expected output: "test"
        String before = "--test";
        String after = Util.stripLeadingHyphens(before);
        
        assertEquals("test", after);
    }

    @Test
    public void testStripLeadingHyphens_withSingleHyphen() {
        // Test input: "-test"
        // Expected output: "test"
        String before = "-test";
        String after = Util.stripLeadingHyphens(before);
        
        assertEquals("test", after);
    }

    @Test
    public void testStripLeadingHyphens_withoutHyphen() {
        // Test input: "test"
        // Expected output: "test"
        String before = "test";
        String after = Util.stripLeadingHyphens(before);
        
        assertEquals("test", after);
    }

    @Test
    public void testStripLeadingHyphens_withNull() {
        // Test input: null
        // Expected output: null
        String before = null;
        String after = Util.stripLeadingHyphens(before);
        
        assertNull(after);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_withLeadingAndTrailingQuotes() {
        // Test input: "\"test\""
        // Expected output: "test"
        String before = "\"test\"";
        String after = Util.stripLeadingAndTrailingQuotes(before);
        
        assertEquals("test", after);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_withLeadingQuote() {
        // Test input: "\"test"
        // Expected output: "test"
        String before = "\"test";
        String after = Util.stripLeadingAndTrailingQuotes(before);
        
        assertEquals("test", after);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_withTrailingQuote() {
        // Test input: "test\""
        // Expected output: "test"
        String before = "test\"";
        String after = Util.stripLeadingAndTrailingQuotes(before);
        
        assertEquals("test", after);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_withoutQuotes() {
        // Test input: "test"
        // Expected output: "test"
        String before = "test";
        String after = Util.stripLeadingAndTrailingQuotes(before);
        
        assertEquals("test", after);
    }

}
