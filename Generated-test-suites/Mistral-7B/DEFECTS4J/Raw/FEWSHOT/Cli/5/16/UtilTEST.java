package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));

        // Test empty string
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("", Util.stripLeadingHyphens("--"));

        // Test leading hyphens followed by text
        assertEquals("text", Util.stripLeadingHyphens("--text"));
        assertEquals("text", Util.stripLeadingHyphens("-- -text"));
        assertEquals("text", Util.stripLeadingHyphens("---text"));

        // Test multiple leading hyphens
        assertEquals("text", Util.stripLeadingHyphens("-----text"));
        assertEquals("text", Util.stripLeadingHyphens("---- -text"));
        assertEquals("text", Util.stripLeadingHyphens("---- --text"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));

        // Test empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));

        // Test leading quotes only
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));

        // Test trailing quotes only
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));

        // Test leading and trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));

        // Test multiple quotes
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo \"bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and \"bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
    }

    @Test
    public void testStripLeadingHyphensWithQuotes() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));

        // Test leading quotes followed by hyphens
        assertEquals("text", Util.stripLeadingHyphens("\"--text"));
        assertEquals("text", Util.stripLeadingHyphens("\"-- -text"));
        assertEquals("text", Util.stripLeadingHyphens("\"---text"));

        // Test trailing quotes followed by hyphens
        assertEquals("text", Util.stripLeadingHyphens("text--"));
        assertEquals("text", Util.stripLeadingHyphens(" text--"));
        assertEquals("text", Util.stripLeadingHyphens(" text--"));

        // Test both leading and trailing quotes and hyphens
        assertEquals("text", Util.stripLeadingHyphens("\"--text"));
        assertEquals("text", Util.stripLeadingHyphens("text--"));
        assertEquals("text", Util.stripLeadingHyphens("\"text--"));
    }
}