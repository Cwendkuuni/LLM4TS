import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueToExistingOption() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
    }

    @Test
    public void testGetUndefaultedValuesWhenNoValuesSet() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testAddSwitch() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(option.isSet());
        commandLine.setDefaultSwitch(option, false);
        assertFalse(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchToExistingSwitch() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        CommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getPropertyValue(option, "property"));
    }
}