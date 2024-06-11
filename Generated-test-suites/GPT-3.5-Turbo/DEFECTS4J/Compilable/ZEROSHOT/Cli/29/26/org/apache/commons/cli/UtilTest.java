package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullString_ReturnNull() {
        String str = null;
        String result = Util.stripLeadingHyphens(str);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_HyphenAtBeginning_RemoveHyphen() {
        String str = "-example";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphenAtBeginning_RemoveHyphens() {
        String str = "--example";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphenAtBeginning_ReturnSameString() {
        String str = "example";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotedString_RemoveQuotes() {
        String str = "\"example\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes_ReturnSameString() {
        String str = "example";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesOnlyAtBeginning_RemoveQuotes() {
        String str = "\"example";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesOnlyAtEnd_RemoveQuotes() {
        String str = "example\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("example", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesAtBothEnds_RemoveQuotes() {
        String str = "\"example\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("example", result);
    }
}