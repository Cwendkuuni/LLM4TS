package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_singleHyphenPrefix() {
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
    }

    @Test
    public void testStripLeadingHyphens_doubleHyphenPrefix() {
        assertEquals("def", Util.stripLeadingHyphens("--def"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        assertEquals("123", Util.stripLeadingAndTrailingQuotes("123"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_singleQuotes() {
        assertEquals("456", Util.stripLeadingAndTrailingQuotes("\"456\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_doubleQuotes() {
        assertEquals("789", Util.stripLeadingAndTrailingQuotes("\"789\""));
    }

}