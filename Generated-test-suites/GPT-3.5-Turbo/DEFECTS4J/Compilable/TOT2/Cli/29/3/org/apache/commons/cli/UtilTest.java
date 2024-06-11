package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullString() {
        String result = Util.stripLeadingHyphens(null);
        assertEquals(null, result);
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_WithQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }
}