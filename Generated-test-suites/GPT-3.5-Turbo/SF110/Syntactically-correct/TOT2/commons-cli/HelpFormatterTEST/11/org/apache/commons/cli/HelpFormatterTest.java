package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testGetWidth() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(80);
        assertEquals(80, helpFormatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setLeftPadding(3);
        assertEquals(3, helpFormatter.getLeftPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", helpFormatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setNewLine("\n");
        assertEquals("\n", helpFormatter.getNewLine());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setLongOptPrefix("--");
        assertEquals("--", helpFormatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setArgName("argument");
        assertEquals("argument", helpFormatter.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(null);
        assertNotNull(helpFormatter.getOptionComparator());
    }

    // Additional test cases for different scenarios can be added here

}