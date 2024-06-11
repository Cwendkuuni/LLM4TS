import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setup() {
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("verbose", "v", false, "Enable verbose output");

        writeableCommandLine.addOption(option);

        assertTrue(writeableCommandLine.hasOption("verbose"));
        assertFalse(writeableCommandLine.hasOption("unknownOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("name", "n", true, "Specifies a name");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "John");

        List<String> values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("John", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("name", "n", true, "Specifies a name");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "John");
        writeableCommandLine.addValue(option, "Jane");

        List<String> values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("John"));
        assertTrue(values.contains("Jane"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("name", "n", true, "Specifies a name");

        writeableCommandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("Default");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List<String> values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("Default", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("verbose", "v", false, "Enable verbose output");

        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.hasOption("verbose"));

        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.hasOption("verbose"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        Option option = new Option("verbose", "v", false, "Enable verbose output");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("verbose", "v", false, "Enable verbose output");

        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.hasOption("verbose"));

        writeableCommandLine.setDefaultSwitch(option, false);
        assertFalse(writeableCommandLine.hasOption("verbose"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("property", "p", true, "Specifies a property");

        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "key", "value");

        String value = writeableCommandLine.getProperty(option, "key");
        assertEquals("value", value);
    }

    @Test
    public void testAddPropertyToDefault() {
        writeableCommandLine.addProperty("key", "value");

        String value = writeableCommandLine.getProperty(null, "key");
        assertEquals("value", value);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-v"));
        assertTrue(writeableCommandLine.looksLikeOption("--verbose"));
        assertFalse(writeableCommandLine.looksLikeOption("-value"));
        assertFalse(writeableCommandLine.looksLikeOption("argument"));
    }
}