import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new Option("", "", false, "");
        argument = new Argument("", "");
        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument);
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new Option("", "", false, "");
        commandLine.addOption(newOption);
        assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        String value = "test";
        commandLine.addValue(argument, value);
        List<String> values = commandLine.getValues(argument, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option newOption = new Option("", "", false, "");
        commandLine.addSwitch(newOption, true);
        Boolean switchValue = commandLine.getSwitch(newOption, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option resultOption = commandLine.getOption("");
        assertEquals(option, resultOption);
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        List<String> values = commandLine.getValues(argument, defaultValues);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = true;
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String resultValue = commandLine.getProperty(property, null);
        assertEquals(value, resultValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption(""));
    }

    @Test
    public void testToString() {
        String argumentValue = "test argument";
        commandLine.addValue(argument, argumentValue);
        String result = commandLine.toString();
        assertEquals(argumentValue, result);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLine.setDefaultValues(argument, defaults);
        List<String> resultDefaults = commandLine.getValues(argument, null);
        assertEquals(defaults, resultDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean resultSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, resultSwitch);
    }

    @Test
    public void testGetNormalised() {
        List<String> expected = new ArrayList<>();
        expected.add("test");
        commandLine.addValue(argument, "test");
        List<String> normalised = commandLine.getNormalised();
        assertEquals(expected, normalised);
    }
}