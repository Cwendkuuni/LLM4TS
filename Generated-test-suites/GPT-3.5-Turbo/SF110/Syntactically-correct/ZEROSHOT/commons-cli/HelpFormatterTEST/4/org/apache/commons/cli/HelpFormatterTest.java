package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    private HelpFormatter formatter;
    private Options options;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
        options = new Options();
    }

    @Test
    public void testSetWidth() {
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        formatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        formatter.setArgName("value");
        assertEquals("value", formatter.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        Comparator comparator = new OptionComparator();
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelpWithoutAutoUsage() {
        formatter.printHelp("cmd", options);
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        formatter.printHelp("cmd", options, true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooterWithoutAutoUsage() {
        formatter.printHelp("cmd", "header", options, "footer");
    }

    @Test
    public void testPrintHelpWithHeaderAndFooterWithAutoUsage() {
        formatter.printHelp("cmd", "header", options, "footer", true);
    }

    @Test
    public void testPrintHelpWithWidthAndHeaderAndFooterWithoutAutoUsage() {
        formatter.printHelp(80, "cmd", "header", options, "footer");
    }

    @Test
    public void testPrintHelpWithWidthAndHeaderAndFooterWithAutoUsage() {
        formatter.printHelp(80, "cmd", "header", options, "footer", true);
    }

    @Test
    public void testPrintHelpWithPrintWriter() {
        formatter.printHelp(new PrintWriter(System.out), 80, "cmd", "header", options, 2, 4, "footer");
    }


    // helper method to create an option
    private void addOption(String opt, String longOpt, boolean hasArg, String description) {
        Option option = new Option(opt, longOpt, hasArg, description);
        options.addOption(option);
    }
}