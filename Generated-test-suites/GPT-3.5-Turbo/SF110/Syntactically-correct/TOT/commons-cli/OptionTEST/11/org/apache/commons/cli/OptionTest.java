import static org.junit.Assert.*;
import org.junit.Test;

public class OptionTest {

    // Test for Option(String opt, String description)
    @Test
    public void testOption1() {
        Option option = new Option("f", "File option");
        assertEquals("f", option.getOpt());
        assertEquals("File option", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(-1, option.getId());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue(""));
        assertNull(option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    // Test for Option(String opt, boolean hasArg, String description)
    @Test
    public void testOption2() {
        Option option = new Option("f", true, "File option with argument");
        assertEquals("f", option.getOpt());
        assertEquals("File option with argument", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertTrue(option.isRequired());
        assertEquals(-1, option.getId());
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue(""));
        assertNull(option.getValues());
        assertEquals(0, option.getValuesList().size());
    }

    // Test for Option(String opt, String longOpt, boolean hasArg, String description)
    @Test
    public void testOption3() {
        Option option = new Option("f", "file", true, "File option with long option name");
        assertEquals("f", option.getOpt());
        assertEquals("file", option.getLongOpt());
        assertEquals("File option with long option name", option.getDescription());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getType());
        assertTrue(option.isRequired());
        assertEquals(-1, option.getId());
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValue(0));
        assertNull(option.getValue(""));
        assertNull(option.getValues());
        assertEquals(0, option.getValuesList().size());
    }
}