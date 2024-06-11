import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class HelpFormatterTest {

    private HelpFormatter helpFormatter;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    @Before
    public void setUp() {
        helpFormatter = new HelpFormatter();
    }

    @Test
    public void testSetWidth() {
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
        helpFormatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", helpFormatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        helpFormatter.setNewLine("\r\n");
        assertEquals("\r\n", helpFormatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        helpFormatter.setOptPrefix("+");
        assertEquals("+", helpFormatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        helpFormatter.setLongOptPrefix("~~");
        assertEquals("~~", helpFormatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        helpFormatter.setArgName("arg_name");
        assertEquals("arg_name", helpFormatter.getArgName());
    }

    @Test
    public void testGetOptionComparator() {
        Comparator comparator = helpFormatter.getOptionComparator();
        assertEquals(HelpFormatter.OptionComparator.class, comparator.getClass());
    }

    @Test
    public void testSetOptionComparator() {
        Comparator newComparator = new OptionComparator();
        helpFormatter.setOptionComparator(newComparator);
        assertEquals(newComparator, helpFormatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp_with_cmdLineSyntax_options() {
        String expectedOutput = "usage: cmd [-a]";
        Options options = new Options();
        options.addOption(new Option("a", "description"));
        helpFormatter.printHelp("cmd", options);

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void testPrintHelp_with_cmdLineSyntax_options_autoUsage() {
        String expectedOutput = "usage: cmd [-a]";
        Options options = new Options();
        options.addOption(new Option("a", "description"));
        helpFormatter.printHelp("cmd", options, true);

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void testPrintHelp_with_cmdLineSyntax_header_options_footer() {
        String expectedOutput = "usage: cmd\nheader\n" +
                " -a   description\nfooter";
        Options options = new Options();
        options.addOption(new Option("a", "description"));
        helpFormatter.printHelp("cmd", "header", options, "footer");

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void testPrintHelp_with_cmdLineSyntax_header_options_footer_autoUsage() {
        String expectedOutput = "usage: cmd\nheader\n" +
                " -a   description\nfooter";
        Options options = new Options();
        options.addOption(new Option("a", "description"));
        helpFormatter.printHelp("cmd", "header", options, "footer", true);

        assertEquals(expectedOutput, outContent.toString().trim());
    }

    private void redirectSystemOutput() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    private void restoreSystemOutput() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
}