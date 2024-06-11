import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
        formatter.setSyntaxPrefix("usage: ");
        assertEquals("usage: ", formatter.getSyntaxPrefix());
    }
    
    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());
    }
    
    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("-");
        assertEquals("-", formatter.getOptPrefix());
    }
    
    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());
    }
    
    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("arg");
        assertEquals("arg", formatter.getArgName());
    }
    
    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelp_NoCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        formatter.printHelp(null, options);
    }
    
    @Test
    public void testPrintUsage() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printUsage(pw, 80, "app", new Options());
    }
    
    @Test
    public void testPrintOptions() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printOptions(pw, 80, new Options(), 2, 4);
    }
    
    @Test
    public void testPrintWrapped() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printWrapped(pw, 80, "text");
    }
}