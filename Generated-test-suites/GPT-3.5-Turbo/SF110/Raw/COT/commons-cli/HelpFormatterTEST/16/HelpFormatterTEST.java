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
        helpFormatter.setOptPrefix("-");
        assertEquals("-", helpFormatter.getOptPrefix());
    }
    
    @Test
    public void testSetLongOptPrefix() {
        helpFormatter.setLongOptPrefix("--");
        assertEquals("--", helpFormatter.getLongOptPrefix());
    }
    
    @Test
    public void testSetArgName() {
        helpFormatter.setArgName("arg");
        assertEquals("arg", helpFormatter.getArgName());
    }
    
    @Test
    public void testGetOptionComparator() {
        assertNotNull(helpFormatter.getOptionComparator());
    }
    
    @Test
    public void testSetOptionComparator() {
        helpFormatter.setOptionComparator(null);
        assertNotNull(helpFormatter.getOptionComparator());
    }
    
}