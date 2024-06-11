import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cmd(); // Assuming Cmd is a valid instance of WriteableCommandLine
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getOptionValues(option).get(0));
    }

    @Test
    public void testGetOptionValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getOptionValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        List defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValues("property").get(0));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = new Cmd();
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = new Cmd();
        commandLine.addOption(Option.builder("o").longOpt("option").build());
        assertTrue(commandLine.looksLikeOption("o"));
    }
}