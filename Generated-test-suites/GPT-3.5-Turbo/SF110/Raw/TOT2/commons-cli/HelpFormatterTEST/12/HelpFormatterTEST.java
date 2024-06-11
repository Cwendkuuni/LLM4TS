import static org.junit.Assert.*;
import org.junit.Test;

public class HelpFormatterTest {

    // Test cases for setWidth method
    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());
    }

    // Test cases for setLeftPadding method
    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    // Test cases for setDescPadding method
    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());
    }

    // Additional test cases for setDescPadding method with negative padding
    @Test(expected = IllegalArgumentException.class)
    public void testSetDescPaddingNegative() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(-1);
    }

    // Test cases for setOptPrefix method
    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("*");
        assertEquals("*", formatter.getOptPrefix());
    }

    // Test cases for setLongOptPrefix method
    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--test");
        assertEquals("--test", formatter.getLongOptPrefix());
    }

    // Additional test cases for setLongOptPrefix method with null prefix
    @Test(expected = IllegalArgumentException.class)
    public void testSetLongOptPrefixNull() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix(null);
    }

    // Test cases for setArgName method
    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());
    }

    // Test cases for getOptionComparator method
    @Test
    public void testGetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter.getOptionComparator());
    }

    // Additional test cases for setOptionComparator method with null comparator
    @Test
    public void testSetOptionComparatorNull() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        assertNotNull(formatter.getOptionComparator());
    }

    // Test case for printHelp method
    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message");
        formatter.printHelp("cmd", options);
        // Test if any exceptions are thrown
    }
}