import static org.junit.Assert.*;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testSetGetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(2);
        assertEquals(2, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Syntax: ");
        assertEquals("Syntax: ", formatter.getSyntaxPrefix());
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
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());
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
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());
    }

    // Additional tests for edge cases and exceptions can be added here

}