import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    private HelpFormatter helpFormatter;

    @Before
    public void setUp() {
        helpFormatter = new HelpFormatter();
    }

    @Test
    public void testSetGetWidth() {
        helpFormatter.setWidth(80);
        assertEquals(80, helpFormatter.getWidth());
    }

    @Test
    public void testSetGetLeftPadding() {
        helpFormatter.setLeftPadding(5);
        assertEquals(5, helpFormatter.getLeftPadding());
    }

    @Test
    public void testSetGetDescPadding() {
        helpFormatter.setDescPadding(4);
        assertEquals(4, helpFormatter.getDescPadding());
    }

    @Test
    public void testSetGetSyntaxPrefix() {
        helpFormatter.setSyntaxPrefix("Syntax: ");
        assertEquals("Syntax: ", helpFormatter.getSyntaxPrefix());
    }

    @Test
    public void testSetGetNewLine() {
        helpFormatter.setNewLine("\n");
        assertEquals("\n", helpFormatter.getNewLine());
    }

    @Test
    public void testSetGetOptPrefix() {
        helpFormatter.setOptPrefix("/");
        assertEquals("/", helpFormatter.getOptPrefix());
    }

    @Test
    public void testSetGetLongOptPrefix() {
        helpFormatter.setLongOptPrefix("===");
        assertEquals("===", helpFormatter.getLongOptPrefix());
    }

    @Test
    public void testSetGetArgName() {
        helpFormatter.setArgName("argument");
        assertEquals("argument", helpFormatter.getArgName());
    }

    @Test
    public void testSetGetOptionComparator() {
        Comparator comparator = new OptionComparator();
        helpFormatter.setOptionComparator(comparator);
        assertEquals(comparator, helpFormatter.getOptionComparator());
    }

    // Add more test cases for other methods as needed

}