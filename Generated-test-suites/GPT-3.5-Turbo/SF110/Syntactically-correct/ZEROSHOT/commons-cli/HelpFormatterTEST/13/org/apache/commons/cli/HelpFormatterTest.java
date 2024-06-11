import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelpFormatterTest {
    
    private HelpFormatter helpFormatter;
    
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
        helpFormatter.setNewLine("\n");
        assertEquals("\n", helpFormatter.getNewLine());
    }
    
    @Test
    public void testSetOptPrefix() {
        helpFormatter.setOptPrefix("--");
        assertEquals("--", helpFormatter.getOptPrefix());
    }
    
    @Test
    public void testSetLongOptPrefix() {
        helpFormatter.setLongOptPrefix("----");
        assertEquals("----", helpFormatter.getLongOptPrefix());
    }
    
    @Test
    public void testSetArgName() {
        helpFormatter.setArgName("argument");
        assertEquals("argument", helpFormatter.getArgName());
    }
    
    @Test
    public void testSetOptionComparator() {
        Comparator<org.apache.commons.cli.Option> comparator = new Comparator<org.apache.commons.cli.Option>() {
            public int compare(org.apache.commons.cli.Option o1, org.apache.commons.cli.Option o2) {
                return o1.getKey().compareToIgnoreCase(o2.getKey());
            }
        };
        helpFormatter.setOptionComparator(comparator);
        assertNotNull(helpFormatter.getOptionComparator());
    }
    
    @Test
    public void testPrintHelp() {
        helpFormatter.printHelp("command", new Options());
    }
    
    @Test
    public void testPrintHelpWithUsage() {
        helpFormatter.printHelp("command", new Options(), true);
    }
    
    @Test
    public void testPrintHelpWithHeaderFooter() {
        helpFormatter.printHelp("command", "header", new Options(), "footer");
    }
    
    @Test
    public void testPrintHelpWithHeaderFooterAndUsage() {
        helpFormatter.printHelp("command", "header", new Options(), "footer", true);
    }
    
    @Test
    public void testPrintHelpWithWidthAndHeaderFooter() {
        helpFormatter.printHelp(80, "command", "header", new Options(), "footer");
    }
    
    @Test
    public void testPrintHelpWithWidthHeaderFooterAndUsage() {
        helpFormatter.printHelp(80, "command", "header", new Options(), "footer", true);
    }
    
    @Test
    public void testPrintUsageWithOptions() {
        helpFormatter.printUsage("command", new Options());
    }
    
    @Test
    public void testPrintUsage() {
        helpFormatter.printUsage("command");
    }
    
    @Test
    public void testPrintOptions() {
        helpFormatter.printOptions(new PrintWriter(System.out), 80, new Options(), 2, 4);
    }
    
    @Test
    public void testPrintWrapped() {
        helpFormatter.printWrapped(new PrintWriter(System.out), 80, "This is a wrapped text.");
    }
    
    @Test
    public void testPrintWrappedWithNextLineTabStop() {
        helpFormatter.printWrapped(new PrintWriter(System.out), 80, 4, "This is a wrapped text.");
    }
    
    @Test
    public void testRenderOptions() {
        StringBuffer sb = new StringBuffer();
        helpFormatter.renderOptions(sb, 80, new Options(), 2, 4);
        assertNotNull(sb.toString());
    }
    
    @Test
    public void testRenderWrappedText() {
        StringBuffer sb = new StringBuffer();
        helpFormatter.renderWrappedText(sb, 80, 4, "This is a wrapped text.");
        assertNotNull(sb.toString());
    }
    
    @Test
    public void testFindWrapPos() {
        assertEquals(-1, helpFormatter.findWrapPos("This is a test.", 80, 0));
    }
    
    @Test
    public void testCreatePadding() {
        assertEquals("  ", helpFormatter.createPadding(2));
    }
    
    @Test
    public void testRtrim() {
        assertEquals("   ", helpFormatter.rtrim("     "));
    }
}