import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private Argument argument;

    @Before
    public void setUp() {
        option = new Option();
        option.setPreferredName("option");
        option.addTrigger("opt");
        option.addTrigger("o");

        argument = new Argument();
        argument.setName("arg");

        List<String> args = new ArrayList<>();
        args.add("arg1");
        args.add("arg2");

        commandLine = new WriteableCommandLineImpl(option, args);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(argument, "value");

        List<String> values = commandLine.getValues(argument, null);

        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);

        Option result = commandLine.getOption("opt");

        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        List<String> values = commandLine.getValues(argument, defaultValues);

        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");

        String result = commandLine.getProperty("key", null);

        assertEquals("value", result);
    }

    @Test
    public void testToString() {
        String result = commandLine.toString();

        assertEquals("arg1 arg2", result);
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

        assertTrue(commandLine.getOptionTriggers().contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        List<String> result = commandLine.getValues(option, null);

        assertEquals(2, result.size());
        assertEquals("default1", result.get(0));
        assertEquals("default2", result.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> result = commandLine.getNormalised();

        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0));
        assertEquals("arg2", result.get(1));
    }
}