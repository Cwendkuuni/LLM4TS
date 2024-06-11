package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String result1 = Util.stripLeadingHyphens("----test");
        String result2 = Util.stripLeadingHyphens("-test");
        String result3 = Util.stripLeadingHyphens("test");

        assertEquals(result1, "test");
        assertEquals(result2, "test");
        assertEquals(result3, "test");
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String result1 = Util.stripLeadingAndTrailingQuotes("\"test\"");
        String result2 = Util.stripLeadingAndTrailingQuotes("\"test");

        assertEquals(result1, "test");
        assertEquals(result2, "\"test");
    }
}