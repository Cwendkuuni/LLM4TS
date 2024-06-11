package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo-bar";
        String expected = "foo-bar";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testStripLeadingHyphens_EmptyString() {
        String input = "";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals("", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphens_NullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }
}