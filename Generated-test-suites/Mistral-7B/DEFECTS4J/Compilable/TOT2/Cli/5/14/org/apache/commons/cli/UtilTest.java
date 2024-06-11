package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingHyphensOneLeadingHyphen() {
        String input = "--one";
        String expectedOutput = "one";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String input = "----test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }
}