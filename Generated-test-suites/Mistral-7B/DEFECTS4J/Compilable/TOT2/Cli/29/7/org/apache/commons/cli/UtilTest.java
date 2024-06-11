package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--test-string";
        String expectedOutput = "test-string";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }
}