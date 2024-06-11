import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new Argument();
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withLongName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test(expected = RuntimeException.class)
    public void testAddSwitch_AlreadySet() {
        Option option = new OptionBuilder().withLongName("switch").create();
        boolean value = true;
        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withLongName("option").create();
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withLongName("option").create();
        assertNull(commandLine.getOption("option"));
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        List<String> values = commandLine.getValues(option, defaultValues);
        assertEquals(defaultValues, values);
        commandLine.addValue(option, "value");
        values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withLongName("switch").create();
        Boolean defaultValue = false;
        Boolean switchValue = commandLine.getSwitch(option, defaultValue);
        assertEquals(defaultValue, switchValue);
        commandLine.addSwitch(option, true);
        switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        String defaultValue = "defaultValue";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, null));
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().withLongName("option").create();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set<String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetProperties_DefaultOption() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set<String> properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder().withLongName("option").create();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        String expected = "--option value";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withLongName("option1").create();
        Option option2 = new OptionBuilder().withLongName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withLongName("option1").create();
        Option option2 = new OptionBuilder().withLongName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValues(option, null);
        assertEquals(defaults, values);
        commandLine.addValue(option, "value");
        values = commandLine.getValues(option, defaults);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withLongName("switch").create();
        Boolean defaultSwitch = false;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, switchValue);
        commandLine.addSwitch(option, true);
        switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withLongName("option").create();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        List<String> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}