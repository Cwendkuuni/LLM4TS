import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Option option3;
    private Argument argument;

    @Before
    public void setup() {
        option1 = new Option("option1");
        option2 = new Option("option2");
        option3 = new Option("option3");
        argument = new Argument();

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option2);
        commandLine.addValue(option2, "value2");
        commandLine.addSwitch(option3, true);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(new Option("newOption"));
        assertTrue(commandLine.hasOption("newOption"));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(Arrays.asList("arg1", "arg2", "value1"), values);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, false);
        assertTrue(commandLine.getSwitch(option1, true));
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(new Option("newOption")));
    }

    @Test
    public void testGetOption() {
        assertEquals(option1, commandLine.getOption("option1"));
        assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        List<String> values = commandLine.getValues(option2, null);
        assertEquals(Arrays.asList("value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> values = commandLine.getUndefaultedValues(option2);
        assertEquals(Arrays.asList("value2"), values);
    }

    @Test
    public void testGetSwitch() {
        assertTrue(commandLine.getSwitch(option3, null));
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        assertEquals(null, property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        String property = commandLine.getProperty("property");
        assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = commandLine.getProperty(option1, "property", "defaultValue");
        assertEquals("defaultValue", property);
    }

    @Test
    public void testGetProperties() {
        Set<String> properties = commandLine.getProperties(option1);
        assertEquals(0, properties.size());

        commandLine.addProperty(option1, "property1", "value1");

        properties = commandLine.getProperties(option1);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
    }

    @Test
    public void testGetPropertiesAll() {
        Set<String> properties = commandLine.getProperties();
        assertEquals(0, properties.size());

        commandLine.addProperty(option1, "property1", "value1");

        properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property1"));
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        String expected = "arg1 arg2";
        String result = commandLine.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("option1"));
        assertTrue(optionTriggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("defaultValue");
        commandLine.setDefaultValues(option1, defaults);
        List<String> values = commandLine.getValues(option1, null);
        assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, false);
        assertFalse(commandLine.getSwitch(option1, true));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();
        assertEquals(Arrays.asList("arg1", "arg2"), normalised);
    }
}