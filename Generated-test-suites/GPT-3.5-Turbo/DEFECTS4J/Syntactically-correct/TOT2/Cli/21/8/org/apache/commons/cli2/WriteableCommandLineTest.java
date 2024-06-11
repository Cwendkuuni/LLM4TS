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
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        assertTrue(writeableCommandLine.hasOption("test"));
        assertEquals("value", writeableCommandLine.getValue("test"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        assertEquals(1, writeableCommandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultValues(option, List.of("default"));
        assertTrue(writeableCommandLine.hasOption("test"));
        assertEquals("default", writeableCommandLine.getValue("test"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch("test"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch("test"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "key", "value");
        assertTrue(writeableCommandLine.hasOption("test"));
        assertEquals("value", writeableCommandLine.getProperty("test", "key"));
    }

    @Test
    public void testAddDefaultProperty() {
        writeableCommandLine.addProperty("key", "value");
        assertEquals("value", writeableCommandLine.getDefaultProperty("key"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-test"));
        assertFalse(writeableCommandLine.looksLikeOption("value"));
    }

    @Test
    public void testSetAndGetCurrentOption() {
        Option option = new Option("test", "test option");
        writeableCommandLine.setCurrentOption(option);
        assertEquals(option, writeableCommandLine.getCurrentOption());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadyAdded() {
        Option option = new Option("test", "test option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

}