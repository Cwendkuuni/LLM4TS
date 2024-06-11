package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("\"abc"));
        assertEquals("def", Util.stripLeadingAndTrailingQuotes("def\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_emptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_singleQuote() {
        assertEquals("a", Util.stripLeadingAndTrailingQuotes("\"a\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_multipleQuotes() {
        assertEquals("ab\"c", Util.stripLeadingAndTrailingQuotes("\"ab\"c\""));
    }
}