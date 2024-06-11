import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(2, commandLine.getValues(option, null).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        assertEquals("value1", commandLine.getProperty(option, "property1", null));
        assertEquals("value2", commandLine.getProperty(option, "property2", null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(null, "value1");
        commandLine.addValue(null, "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        commandLine.setDefaultValues(option, null);
        assertEquals(2, commandLine.getValues(option, null).size());

        commandLine.setDefaultValues(option, new ArrayList());
        assertEquals(2, commandLine.getValues(option, null).size());

        commandLine.setDefaultValues(option, new ArrayList(Arrays.asList("default1", "default2")));
        assertEquals(4, commandLine.getValues(option, null).size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test", "Test Option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, false);

        commandLine.setDefaultSwitch(option, true);
        assertEquals(false, commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);
        assertEquals(true, commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, new ArrayList(Arrays.asList("value1", "value2")));

        assertEquals(2, commandLine.getNormalised().size());
    }

}