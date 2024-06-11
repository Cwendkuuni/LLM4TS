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

    // Edge case testing for setSyntaxPrefix() method
    @Test
    public void testSetSyntaxPrefixEdgeCase() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("");
        assertEquals("", formatter.getSyntaxPrefix());
    }

    // Test for setArgName() method with null argument
    @Test(expected = IllegalArgumentException.class)
    public void testSetArgNameWithNull() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName(null);
    }

    // Testing exception handling for printHelp() method
    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, new Options());
    }

    // Edge case testing for printWrapped() method
    @Test
    public void testPrintWrappedEdgeCase() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped(new PrintWriter(System.out), 80, 0, "Testing edge case scenario where text fits exactly in width");
    }

    // Test case for printHelp() method with all arguments provided
    @Test
    public void testPrintHelpWithAllArgs() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, "usage", "header", new Options(), "footer", true);
    }

    // Exception handling testing for printUsage() method
    @Test(expected = IllegalArgumentException.class)
    public void testPrintUsageWithNullSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(System.out), 80, null, new Options());
    }

}