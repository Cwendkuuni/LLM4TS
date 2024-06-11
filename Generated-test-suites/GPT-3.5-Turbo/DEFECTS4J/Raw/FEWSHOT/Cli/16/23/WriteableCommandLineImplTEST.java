import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private Option option;
    private List arguments;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = new Option();
        arguments = Arrays.asList("arg1", "arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(Collections.EMPTY_LIST, values);
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(Collections.singleton(property), commandLine.getProperties(option));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-trigger";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String commandLineString = "arg1 arg2";
        assertEquals(commandLineString, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        assertEquals(Collections.singleton(option.getPreferredName()), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultValue = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultValue);
        assertEquals(defaultValue, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(arguments, commandLine.getNormalised());
    }
}