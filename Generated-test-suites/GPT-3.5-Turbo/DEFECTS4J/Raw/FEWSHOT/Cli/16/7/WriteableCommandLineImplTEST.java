import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder().withPreferredName("root").create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        String value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));
        assertEquals(values, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        assertNull(commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property";
        String defaultValue = "default";
        assertEquals(defaultValue, commandLine.getProperty(property, defaultValue));
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertTrue(commandLine.getProperties().contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--option";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String argument = "argument";
        List<String> arguments = Arrays.asList(argument);
        commandLine = new WriteableCommandLineImpl(null, arguments);
        assertEquals(argument, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<String> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        String argument = "argument";
        List<String> arguments = Arrays.asList(argument);
        commandLine = new WriteableCommandLineImpl(null, arguments);
        assertEquals(arguments, commandLine.getNormalised());
    }
}