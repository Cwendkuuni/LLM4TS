import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("r", "remove", true, "Remove option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("a"));
    }

    @Test
    public void testGetValues() {
        Argument argument = new Argument();
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addValue(argument, "value");
        assertEquals("value", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("r", "remove", true, "Remove option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("r", "remove", true, "Remove option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        assertTrue(commandLine.looksLikeOption("-r"));
        assertFalse(commandLine.looksLikeOption("remove"));
    }

    @Test
    public void testToString() {
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        assertEquals("", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", true, "Remove option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(option1, commandLine.getOptions().get(0));
        assertEquals(option2, commandLine.getOptions().get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("a", "add", false, "Add option");
        Option option2 = new Option("r", "remove", true, "Remove option");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.getOptionTriggers().contains("a"));
        assertTrue(commandLine.getOptionTriggers().contains("add"));
        assertTrue(commandLine.getOptionTriggers().contains("r"));
        assertTrue(commandLine.getOptionTriggers().contains("remove"));
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = new Argument();
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.setDefaultValues(argument, Collections.singletonList("default"));
        assertEquals("default", commandLine.getValues(argument, null).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("r", "remove", false, "Remove option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("a", "add", false, "Add option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, null);

        assertEquals(null, commandLine.getNormalised());
    }
}