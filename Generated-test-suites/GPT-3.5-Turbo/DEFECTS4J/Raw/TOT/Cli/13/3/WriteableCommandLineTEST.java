import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void addOptionTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", true, "option description");
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption("opt1"));
    }

    @Test
    public void addValueTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", true, "option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        
        assertEquals("value1", commandLine.getValues(option).get(0));
    }

    @Test
    public void getValuesTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", true, "option description");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void setDefaultValuesTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", true, "option description");

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void addSwitchTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", false, "option description");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void addSwitchAlreadyAddedTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", false, "option description");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true); // Should throw IllegalStateException
    }

    @Test
    public void setDefaultSwitchTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("opt1", "option", false, "option description");

        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch(option));

        commandLine.setDefaultSwitch(option, false);
        assertEquals(false, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void addPropertyTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addProperty("prop1", "value1");
        assertEquals("value1", commandLine.getProperty("prop1"));

        commandLine.addProperty("prop2", "value2");
        assertEquals("value2", commandLine.getProperty("prop2"));
    }

    @Test
    public void looksLikeOptionTest() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        assertTrue(commandLine.looksLikeOption("-opt1"));
        assertTrue(commandLine.looksLikeOption("--opt2"));
        assertFalse(commandLine.looksLikeOption("value1"));
        assertFalse(commandLine.looksLikeOption(""));
    }
}