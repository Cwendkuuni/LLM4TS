package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("syntax: ");
        assertEquals("syntax: ", formatter.getSyntaxPrefix());
    }

    // Add more test cases for other methods

}