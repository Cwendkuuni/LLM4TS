package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class HelpFormatterTest {

    private HelpFormatter helpFormatter;

    @Before
    public void setup() {
        helpFormatter = new HelpFormatter();
    }

    @Test
    public void testSetGetWidth() {
        helpFormatter.setWidth(80);
        assertEquals(80, helpFormatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        helpFormatter.setLeftPadding(2);
        assertEquals(2, helpFormatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        helpFormatter.setDescPadding(4);
        assertEquals(4, helpFormatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        helpFormatter.setSyntaxPrefix("This is a prefix: ");
        assertEquals("This is a prefix: ", helpFormatter.getSyntaxPrefix());
    }

    // Add more test cases for the remaining methods

}