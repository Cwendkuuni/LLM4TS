package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(80);
        assertEquals(80, helpFormatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setLeftPadding(2);
        assertEquals(2, helpFormatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setDescPadding(4);
        assertEquals(4, helpFormatter.getDescPadding());
    }

    // Add more basic test cases for each method here...

    @Test
    public void testPrintHelpWithNullSyntax() {
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            helpFormatter.printHelp(null, new Options());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Add more test cases for edge cases and error scenarios...

}