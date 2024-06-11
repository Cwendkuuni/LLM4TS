import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new Option("root", "Root Option");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test", "Test Option");

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "Switch Option");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "Test Option");

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(option1, commandLine.getOption("test1"));
        assertEquals(option2, commandLine.getOption("test2"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        List<String> values = commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues, values);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        values = commandLine.getValues(option, defaultValues);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("switch", "Switch Option");
        Boolean defaultValue = false;

        assertFalse(commandLine.getSwitch(option, defaultValue));

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";

        assertEquals(value, commandLine.getProperty(property));

        Option option = new PropertyOption();

        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));        
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "property";
        String defaultValue = "default";

        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));

        Option option = new PropertyOption();

        commandLine.addProperty(option, property, "value");

        assertEquals("value", commandLine.getProperty(option, property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        Option option1 = new PropertyOption();
        Option option2 = new PropertyOption();
        String property1 = "property1";
        String property2 = "property2";
        String value1 = "value1";
        String value2 = "value2";

        Set<String> properties = commandLine.getProperties();
        assertTrue(properties.isEmpty());

        commandLine.addProperty(option1, property1, value1);
        commandLine.addProperty(option2, property2, value2);

        properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains(property1));
        assertTrue(properties.contains(property2));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(commandLine.looksLikeOption("arg1"));
        assertFalse(commandLine.looksLikeOption("arg2"));

        Option option1 = new Option("-opt1", "Option 1");
        Option option2 = new Option("--opt2", "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertTrue(commandLine.looksLikeOption("-opt1"));
        assertTrue(commandLine.looksLikeOption("--opt2"));

        assertFalse(commandLine.looksLikeOption("arg1"));
        assertFalse(commandLine.looksLikeOption("arg2"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(new Argument(), "value1");
        commandLine.addValue(new Argument(), "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1", "Test Option 1");
        Option option2 = new Option("test2", "Test Option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains(option1.getPreferredName()));
        assertTrue(triggers.contains(option2.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        List<String> values = commandLine.getValues(option, null);
        assertEquals(defaults, values);

        commandLine.addValue(option, "value1");

        values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("switch", "Switch Option");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertTrue(commandLine.getSwitch(option, null));

        commandLine.addSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, arguments);

        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}