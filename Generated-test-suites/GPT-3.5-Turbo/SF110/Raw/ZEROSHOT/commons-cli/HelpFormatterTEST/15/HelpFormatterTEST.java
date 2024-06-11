import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {
    
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    @Test
    public void testSetGetWidth() {
        assertEquals(74, formatter.getWidth());
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());
    }

    @Test
    public void testSetGetLeftPadding() {
        assertEquals(1, formatter.getLeftPadding());
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetGetDescPadding() {
        assertEquals(3, formatter.getDescPadding());
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testSetGetSyntaxPrefix() {
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        formatter.setSyntaxPrefix("prefix: ");
        assertEquals("prefix: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetGetNewLine() {
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());
    }

    @Test
    public void testSetGetOptPrefix() {
        assertEquals("-", formatter.getOptPrefix());
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());
    }

    @Test
    public void testSetGetLongOptPrefix() {
        assertEquals("--", formatter.getLongOptPrefix());
        formatter.setLongOptPrefix("--=");
        assertEquals("--=", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetGetArgName() {
        assertEquals("arg", formatter.getArgName());
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());
    }

    @Test
    public void testGetOptionComparator() {
        assertNotNull(formatter.getOptionComparator());
        assertTrue(formatter.getOptionComparator() instanceof HelpFormatter.OptionComparator);
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter.OptionComparator comparator = new HelpFormatter.OptionComparator();
        formatter.setOptionComparator(comparator);
        assertEquals(comparator, formatter.getOptionComparator());
    }

    // TODO: write more test cases to cover all methods in the HelpFormatter class
    
}