import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("output");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testgetProperty() {
        Option option = new Option("p", "property", true, "Property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, "property.name", "property.value");
        assertEquals("property.value", commandLine.getProperty(option, "property.name"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, "property.name", "property.value");
        Map properties = commandLine.getProperties(option);
        assertFalse(properties.isEmpty());
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, "property.name1", "property.value1");
        commandLine.addProperty(option, "property.name2", "property.value2");
        Set properties = commandLine.getProperties(option);
        assertFalse(properties.isEmpty());
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        assertEquals("test.txt output", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("test.txt");
        defaults.add("output2.txt");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
    }
}