import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        option = new Option("o");
        arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new Option("n");
        commandLine.addOption(newOption);

        assertTrue(commandLine.hasOption(newOption));
        assertNotNull(commandLine.getOption("n"));
    }

    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option, value);

        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(new Option("a")));
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        List defaultValues = Arrays.asList("default1", "default2");
        List values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, values.get(0));

        assertEquals(values, commandLine.getValues(option, defaultValues));
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = false;
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertEquals(value, commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        String expected = "arg1 arg2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option newOption = new Option("n");
        commandLine.addOption(newOption);

        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptions().contains(newOption));
    }

    @Test
    public void testGetOptionTriggers() {
        Option newOption = new Option("n");
        commandLine.addOption(newOption);

        assertTrue(commandLine.getOptionTriggers().contains("o"));
        assertTrue(commandLine.getOptionTriggers().contains("n"));
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);

        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(arguments, commandLine.getNormalised());
    }
}