import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());
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
        formatter.setDescPadding(10);
        assertEquals(10, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", formatter.getSyntaxPrefix());
    }

    // Add more test cases for each method following the same format

}