package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--str";
        String expectedOutput = "str";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }
}