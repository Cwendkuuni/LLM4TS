import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {
    // Test getHeight
    @Test
    public void testGetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());

        formatter.setWidth(120);
        assertEquals(120, formatter.getWidth());
    }

    // Test getLeftPadding
    @Test
    public void testGetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(1, formatter.getLeftPadding());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());

        formatter.setLeftPadding(4);
        assertEquals(4, formatter.getLeftPadding());
    }

    // Test getDescPadding
    @Test
    public void testGetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(3, formatter.getDescPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());

        formatter.setDescPadding(6);
        assertEquals(6, formatter.getDescPadding());
    }

    // Test getSyntaxPrefix
    @Test
    public void testGetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("usage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", formatter.getSyntaxPrefix());

        formatter.setSyntaxPrefix("CMD: ");
        assertEquals("CMD: ", formatter.getSyntaxPrefix());
    }

    // Test getNewLine
    @Test
    public void testGetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());

        formatter.setNewLine("\r\n");
        assertEquals("\r\n", formatter.getNewLine());
    }

    // Test getOptPrefix
    @Test
    public void testGetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("-", formatter.getOptPrefix());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());

        formatter.setOptPrefix("@");
        assertEquals("@", formatter.getOptPrefix());
    }

    // Test getLongOptPrefix
    @Test
    public void testGetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("++");
        assertEquals("++", formatter.getLongOptPrefix());

        formatter.setLongOptPrefix("**");
        assertEquals("**", formatter.getLongOptPrefix());
    }

    // Test getArgName
    @Test
    public void testGetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals("arg", formatter.getArgName());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());

        formatter.setArgName("value");
        assertEquals("value", formatter.getArgName());
    }
}