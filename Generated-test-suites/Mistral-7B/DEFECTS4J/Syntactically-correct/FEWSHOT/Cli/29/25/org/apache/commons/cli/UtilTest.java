package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        assertEquals("", Util.stripLeadingHyphens("--"));
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test
    public void testStripLeadingHyphensNoHyphens() {
        assertEquals("foobar", Util.stripLeadingHyphens("foobar"));
    }

    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("foobar", Util.stripLeadingHyphens("--foobar"));
        assertEquals("foobar", Util.stripLeadingHyphens("-- --foobar"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        assertEquals("foobar", Util.stripLeadingAndTrailingQuotes("foobar"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesQuotesInside() {
        assertEquals("foo \"bar\" baz", Util.stripLeadingAndTrailingQuotes("\"foo \"bar\" baz\""));
    }
}