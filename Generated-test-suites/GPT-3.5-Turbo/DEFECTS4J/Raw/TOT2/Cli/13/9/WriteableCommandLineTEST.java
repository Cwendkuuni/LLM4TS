import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        // Test adding an option to the CommandLine
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        // Test adding a value to an Option in the CommandLine
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value");

        assertEquals("value", writeableCommandLine.getValue(option));
    }

    @Test
    public void testSetDefaultValues() {
        // Test setting default values for an Option in the CommandLine
        Option option = new Option("test");

        writeableCommandLine.setDefaultValues(option, List.of("default1", "default2"));

        assertEquals(List.of("default1", "default2"), writeableCommandLine.getValues(option));
    }

    @Test
    public void testAddSwitch() {
        // Test adding a switch value to an Option in the CommandLine
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);

        assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchException() {
        // Test adding a switch value that has already been added
        Option option = new Option("test");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test setting default state for a Switch in the CommandLine
        Option option = new Option("test");

        writeableCommandLine.setDefaultSwitch(option, true);

        assertTrue(writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        // Test adding a property value to a name in the CommandLine
        writeableCommandLine.addProperty("property1", "value1");

        assertEquals("value1", writeableCommandLine.getProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        // Test detecting if an argument looks like an Option trigger
        assertTrue(writeableCommandLine.looksLikeOption("-option"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }
}