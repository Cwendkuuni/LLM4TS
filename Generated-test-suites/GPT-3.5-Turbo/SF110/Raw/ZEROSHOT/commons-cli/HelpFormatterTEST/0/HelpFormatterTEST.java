import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(80);
        assertEquals(80, hf.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter hf = new HelpFormatter();
        hf.setLeftPadding(2);
        assertEquals(2, hf.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter hf = new HelpFormatter();
        hf.setDescPadding(4);
        assertEquals(4, hf.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter hf = new HelpFormatter();
        hf.setSyntaxPrefix("usage: ");
        assertEquals("usage: ", hf.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter hf = new HelpFormatter();
        hf.setNewLine("\n");
        assertEquals("\n", hf.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter hf = new HelpFormatter();
        hf.setOptPrefix("-");
        assertEquals("-", hf.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter hf = new HelpFormatter();
        hf.setLongOptPrefix("--");
        assertEquals("--", hf.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter hf = new HelpFormatter();
        hf.setArgName("arg");
        assertEquals("arg", hf.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter hf = new HelpFormatter();
        hf.setOptionComparator(null);
        assertNotNull(hf.getOptionComparator());
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("cmdLineSyntax", null);
        // Test that no exception occurs
    }

    @Test
    public void testPrintUsage() {
        HelpFormatter hf = new HelpFormatter();
        hf.printUsage(null, 80, "cmdLineSyntax");
        // Test that no exception occurs
    }

    @Test
    public void testPrintOptions() {
        HelpFormatter hf = new HelpFormatter();
        hf.printOptions(null, 80, null, 1, 3);
        // Test that no exception occurs
    }

    @Test
    public void testPrintWrapped() {
        HelpFormatter hf = new HelpFormatter();
        hf.printWrapped(null, 80, "text");
        // Test that no exception occurs
    }

    @Test
    public void testRtrim() {
        HelpFormatter hf = new HelpFormatter();
        String result = hf.rtrim("   test   ");
        assertEquals("   test", result);
    }

    @Test
    public void testFindWrapPos() {
        HelpFormatter hf = new HelpFormatter();
        int pos = hf.findWrapPos("This is a test", 10, 0);
        assertEquals(7, pos);
    }

    @Test
    public void testCreatePadding() {
        HelpFormatter hf = new HelpFormatter();
        String padding = hf.createPadding(5);
        assertEquals("     ", padding);
    }

    @Test
    public void testOptionComparator() {
        HelpFormatter.OptionComparator comparator = new HelpFormatter.OptionComparator();
        Option opt1 = new Option("a", "option1");
        Option opt2 = new Option("b", "option2");
        int result = comparator.compare(opt1, opt2);
        assertTrue(result < 0);
    }
}