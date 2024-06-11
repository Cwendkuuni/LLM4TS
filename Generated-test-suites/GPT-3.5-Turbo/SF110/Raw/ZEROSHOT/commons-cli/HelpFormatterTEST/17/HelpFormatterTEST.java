package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testSetGetWidth() {
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());
    }

    @Test
    public void testSetGetLeftPadding() {
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetGetDescPadding() {
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testSetGetSyntaxPrefix() {
        formatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetGetNewLine() {
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());
    }

    @Test
    public void testSetGetOptPrefix() {
        formatter.setOptPrefix("-");
        assertEquals("-", formatter.getOptPrefix());
    }

    @Test
    public void testSetGetLongOptPrefix() {
        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetGetArgName() {
        formatter.setArgName("arg");
        assertEquals("arg", formatter.getArgName());
    }

    @Test
    public void testSetGetOptionComparator() {
        Comparator comparator = new HelpFormatter.OptionComparator();
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpNoCmdLineSyntax() {
        formatter.printHelp(null, new Options());
    }

    @Test
    public void testPrintHelpWithCmdLineSyntax() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        formatter.setWidth(80);
        formatter.printHelp("cmdLineSyntax", new Options());

        String expectedOutput = "usage: cmdLineSyntax" + System.getProperty("line.separator");
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRenderOptions() {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("all").desc("Show all").build());
        options.addOption(Option.builder("o").longOpt("output").hasArg().argName("file").desc("Output to file").build());

        StringBuilder sb = new StringBuilder();
        formatter.renderOptions(sb, 80, options, 1, 3);

        String expectedOutput = "-a,--all                          Show all" + System.getProperty("line.separator") +
                "-o,--output <file>                Output to file" + System.getProperty("line.separator");
        assertEquals(expectedOutput, sb.toString());
    }

    @Test
    public void testRenderWrappedText() {
        StringBuilder sb = new StringBuilder();
        formatter.renderWrappedText(sb, 20, 4, "This is a long text that needs to be wrapped.");

        String expectedOutput = "This is a long" + System.getProperty("line.separator") +
                "    text that needs" + System.getProperty("line.separator") +
                "    to be wrapped." + System.getProperty("line.separator");
        assertEquals(expectedOutput, sb.toString());
    }
}