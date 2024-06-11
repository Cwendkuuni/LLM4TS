import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "output", false, "Output file flag");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "foo", "bar");
        String propertyValue = commandLine.getProperty(option, "foo");
        assertEquals("bar", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "foo", "bar");
        Set properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("foo"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("file.txt"));
        assertEquals("file.txt", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
    }
}