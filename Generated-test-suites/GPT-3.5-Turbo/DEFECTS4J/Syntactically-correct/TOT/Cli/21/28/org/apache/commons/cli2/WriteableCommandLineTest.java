import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLine() {
            // Implement the interface methods with dummy code for testing purposes
            // ...
        };
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("file"));
        assertFalse(commandLine.hasOption("x"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        commandLine.addValue(option, "file1.txt");
        commandLine.addValue(option, "file2.txt");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("file1.txt"));
        assertTrue(values.contains("file2.txt"));

        Option nonExistingOption = new Option("x", "nonexisting", false, "Non-existing option");
        commandLine.addValue(nonExistingOption, "value");
        assertTrue(commandLine.getUndefaultedValues(nonExistingOption).isEmpty());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        commandLine.addValue(option, "file1.txt");
        commandLine.addValue(option, "file2.txt");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("file1.txt"));
        assertTrue(values.contains("file2.txt"));

        Option nonExistingOption = new Option("x", "nonexisting", false, "Non-existing option");
        assertTrue(commandLine.getUndefaultedValues(nonExistingOption).isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("f", "file", true, "File option");
        commandLine.addOption(option);

        List<String> defaultValues = List.of("default1.txt", "default2.txt");
        commandLine.setDefaultValues(option, defaultValues);

        assertTrue(commandLine.hasOption("f"));
        assertTrue(commandLine.hasOption("file"));
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch option");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.isSwitchSet(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.isSwitchSet(option));

        commandLine.addSwitch(option, true);

        Option nonExistingOption = new Option("x", "nonexisting", true, "Non-existing option");
        commandLine.addSwitch(nonExistingOption, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "Switch option");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.isSwitchSet(option));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.isSwitchSet(option));

        Option nonExistingOption = new Option("x", "nonexisting", false, "Non-existing option");
        commandLine.setDefaultSwitch(nonExistingOption, true);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", false, "Property option");
        commandLine.addOption(option);

        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");

        assertEquals("value1", commandLine.getProperty(option, "key1"));
        assertEquals("value2", commandLine.getProperty(option, "key2"));

        assertNull(commandLine.getProperty(option, "nonexisting"));

        Option nonExistingOption = new Option("x", "nonexisting", false, "Non-existing option");
        commandLine.addProperty(nonExistingOption, "key", "value");
        assertNull(commandLine.getProperty(nonExistingOption, "key"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        Option option = new Option("p", "property", false, "Property option");

        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");

        assertEquals("value1", commandLine.getProperty(null, "key1"));
        assertEquals("value2", commandLine.getProperty(null, "key2"));

        assertNull(commandLine.getProperty(null, "nonexisting"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-f"));
        assertTrue(commandLine.looksLikeOption("--file"));
        assertFalse(commandLine.looksLikeOption("file.txt"));
        assertFalse(commandLine.looksLikeOption("not an option"));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new Option("o", "option", false, "Option");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }
}