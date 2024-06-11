import static org.junit.Assert.*;
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
        formatter.setSyntaxPrefix("syntax: ");
        assertEquals("syntax: ", formatter.getSyntaxPrefix());
    }
    
    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\r\n");
        assertEquals("\r\n", formatter.getNewLine());
    }
    
    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("opt-");
        assertEquals("opt-", formatter.getOptPrefix());
    }
    
    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("longopt-");
        assertEquals("longopt-", formatter.getLongOptPrefix());
    }
    
    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());
    }
    
    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator((o1, o2) -> {
            return o1.compareToIgnoreCase(o2);
        });
        assertNotNull(formatter.getOptionComparator());
    }
    
    // Add other test cases for the remaining public methods
    
}