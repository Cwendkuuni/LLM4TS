import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("testing1");
        Option option2 = new Option("testing2");
        Argument argument1 = new Argument("arg1");
        Argument argument2 = new Argument("arg2");

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(argument1, argument2));
    }

    // Test addOption()
    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    // Test addValue()
    @Test
    public void testAddValue() {
        Option option = new Option("test");
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, values);

        assertEquals(values, commandLine.getValues(option, null));
    }

    // Test addSwitch()
    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    // Test hasOption()
    @Test
    public void testHasOption() {
        Option option = new Option("test");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    // Test getOption()
    @Test
    public void testGetOption() {
        Option option = new Option("test");
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test"));
    }

    // Test getValues()
    @Test
    public void testGetValues() {
        Option option = new Option("test");
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, values);

        assertEquals(values, commandLine.getValues(option, null));
    }

    // Test getUndefaultedValues()
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        List<String> values = Arrays.asList("value1", "value2");
        commandLine.addValue(option, values);

        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    // Test getSwitch()
    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    // Test getProperty()
    @Test
    public void testGetProperty() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    // Test addProperty()
    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    // Test getProperty()
    @Test
    public void testGetPropertyWithDefault() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property, "default"));
    }

    // Test getProperties()
    @Test
    public void testGetProperties() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);

        Set<String> properties = commandLine.getProperties();

        assertTrue(properties.contains(property));
    }

    // Test looksLikeOption()
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        Option option = new Option("test");
        Argument argument = new Argument("arg");

        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addValue(argument, "value");

        String expected = "-test arg value";
        String actual = commandLine.toString();

        assertEquals(expected, actual);
    }

    // Test getOptions()
    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    // Test getOptionTriggers()
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1");

        commandLine.addOption(option1);

        Set<String> triggers = commandLine.getOptionTriggers();

        assertTrue(triggers.contains("test1"));
    }

    // Test setDefaultValues()
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<String> values = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(option, values);

        assertEquals(values, commandLine.getValues(option, null));
    }

    // Test setDefaultSwitch()
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    // Test getNormalised()
    @Test
    public void testGetNormalised() {
        Argument argument1 = new Argument("arg1");
        Argument argument2 = new Argument("arg2");

        List<String> arguments = Arrays.asList("arg1", "arg2");

        commandLine = new WriteableCommandLineImpl(null, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }

}