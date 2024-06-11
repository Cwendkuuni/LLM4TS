package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class HelpFormatterTest {

    // Test getWidth() method
    @Test
    public void testGetWidth() {
        HelpFormatter helpFormatter = new HelpFormatter();
        assertEquals(74, helpFormatter.getWidth());
    }

    // Test setWidth() method
    @Test
    public void testSetWidth() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(80);
        assertEquals(80, helpFormatter.getWidth());
    }

    // Test setWidth() method with negative value
    @Test(expected = IllegalArgumentException.class)
    public void testSetWidthNegative() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(-5);
    }

    // Test printHelp() method with null cmdLineSyntax
    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpNullCmdLineSyntax() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(null, new Options());
    }

    // Test printHelp() method with empty cmdLineSyntax
    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpEmptyCmdLineSyntax() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("", new Options());
    }

    // Add more test cases for other methods and scenarios

}