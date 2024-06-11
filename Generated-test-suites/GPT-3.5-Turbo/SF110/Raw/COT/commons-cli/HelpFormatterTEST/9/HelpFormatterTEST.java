import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80); // Setting width to 80 characters
        assertEquals(80, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2); // Setting left padding to 2 characters
        assertEquals(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4); // Setting description padding to 4 characters
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: "); // Setting syntax prefix to "Usage: "
        assertEquals("Usage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\r\n"); // Setting newline to "\r\n"
        assertEquals("\r\n", formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("--"); // Setting option prefix to "--"
        assertEquals("--", formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--"); // Setting long option prefix to "--"
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argument"); // Setting argument name to "argument"
        assertEquals("argument", formatter.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator optionComparator = new CustomOptionComparator(); // Custom option comparator
        formatter.setOptionComparator(optionComparator);
        assertEquals(optionComparator, formatter.getOptionComparator());
    }

    // Custom option comparator for testing
    private class CustomOptionComparator implements Comparator {
        public int compare(final Object o1, final Object o2) {
            return 0; // Dummy implementation, always returns 0
        }
    }
}