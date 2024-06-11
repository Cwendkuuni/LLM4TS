package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("_foo", Util.stripLeadingHyphens("--_foo")); // handling leading underscores
        assertNull(Util.stripLeadingHyphens(null));
        assertNull(Util.stripLeadingHyphens(""));
        assertNull(Util.stripLeadingHyphens(" "));
        assertNull(Util.stripLeadingHyphens("--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
        assertEquals("foo ''bar'' baz", Util.stripLeadingAndTrailingQuotes("\"foo ''bar'' baz\""));
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
        assertNull(Util.stripLeadingAndTrailingQuotes(""));
        assertNull(Util.stripLeadingAndTrailingQuotes(" "));
    }
}