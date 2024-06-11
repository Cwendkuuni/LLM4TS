import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cmd(); // Assuming Cmd is a class implementing WriteableCommandLine
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        assertEquals("test", commandLine.getValuesForOption("option").get(0));
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");
        List<String> values = commandLine.getValuesForOption("option");
        assertEquals(2, values.size());
        assertEquals("test1", values.get(0));
        assertEquals("test2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValuesForOption("option");
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option.getLongOpt()));
        assertTrue(commandLine.isSet(option.getLongOpt()));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.isSet(option.getLongOpt()));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new Cmd();
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    private static class Cmd extends CommandLineBase implements WriteableCommandLine {}
}