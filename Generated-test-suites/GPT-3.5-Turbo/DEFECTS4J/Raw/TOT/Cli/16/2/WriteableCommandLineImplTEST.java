import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;
    private List arguments;

    @Before
    public void setUp() {
        option = new Option.Builder("o", "option").create();
        arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        String value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        String value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(defaultValues.get(0)));
        assertTrue(values.contains(defaultValues.get(1)));
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        String value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, false);
        Boolean switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
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
        String retrievedValue = commandLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Set properties = commandLine.getProperties(option);
        assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-o";
        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        String expectedString = value1 + " " + value2;
        assertEquals(expectedString, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List retrievedDefaults = commandLine.getValues(option, null);
        assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean retrievedSwitch = commandLine.getSwitch(option, null);
        assertEquals(defaultSwitch, retrievedSwitch);
    }

    @Test
    public void testGetNormalised() {
        String value = "value";
        arguments.add(value);
        List normalised = commandLine.getNormalised();
        assertTrue(normalised.contains(value));
    }

}