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
        Option option = new Option("--verbose", "Enable verbose mode");
        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("--input", "Specify input file");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "filename.txt");

        assertTrue(writeableCommandLine.getValues(option).contains("filename.txt"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("--input", "Specify input file");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "filename1.txt");
        writeableCommandLine.addValue(option, "filename2.txt");

        assertEquals(2, writeableCommandLine.getUndefaultedValues(option).size());

        writeableCommandLine.setDefaultValue(option, "default.txt");

        assertEquals(0, writeableCommandLine.getUndefaultedValues(option).size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("--output", "Specify output file");
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultValue(option, "output.txt");

        assertEquals("output.txt", writeableCommandLine.getDefaultValue(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("--verbose", "Enable verbose mode");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);

        assertEquals("true", writeableCommandLine.getSwitchValue(option));
        assertTrue(writeableCommandLine.getSwitchState(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("--verbose", "Enable verbose mode");
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, true);

        assertTrue(writeableCommandLine.getDefaultSwitchState(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("--config", "Specify configuration file");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "key1", "value1");
        writeableCommandLine.addProperty(option, "key2", "value2");

        assertEquals("value1", writeableCommandLine.getPropertyValue(option, "key1"));
        assertEquals("value2", writeableCommandLine.getPropertyValue(option, "key2"));
    }

    @Test
    public void testAddPropertyToDefaults() {
        writeableCommandLine.addProperty("key1", "value1");
        writeableCommandLine.addProperty("key2", "value2");

        assertEquals("value1", writeableCommandLine.getDefaultPropertyValue("key1"));
        assertEquals("value2", writeableCommandLine.getDefaultPropertyValue("key2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("--verbose"));
        assertFalse(writeableCommandLine.looksLikeOption("filename.txt"));
    }

    @Test
    public void testSetCurrentOption() {
        Option option1 = new Option("--option1", "Option 1");
        Option option2 = new Option("--option2", "Option 2");

        writeableCommandLine.setCurrentOption(option1);
        assertEquals(option1, writeableCommandLine.getCurrentOption());

        writeableCommandLine.setCurrentOption(option2);
        assertEquals(option2, writeableCommandLine.getCurrentOption());
    }
}