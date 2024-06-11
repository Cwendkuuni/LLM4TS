
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder("o").create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder("test").create();
        Object value = "value";
        commandLine.addValue(option, value);

        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder("test").create();
        boolean value = true;
        commandLine.addSwitch(option, value);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option1 = new OptionBuilder("option1").create();
        Option option2 = new OptionBuilder("option2").create();
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new OptionBuilder("option1").create();
        Option option2 = new OptionBuilder("option2").create();
        commandLine.addOption(option1);

        assertEquals(option1, commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder("test").create();

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));

        List<String> result = commandLine.getValues(option, null);

        assertEquals(values, result);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder("test").create();

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));

        List<String> result = commandLine.getUndefaultedValues(option);

        assertEquals(values, result);
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder("test").create();
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder("test").create();
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        String result = commandLine.getProperty(option, property, null);

        assertEquals(value, result);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder("test").create();
        String property = "property";
        String value = "value";

        commandLine.addProperty(option, property, value);

        Set<String> properties = commandLine.getProperties(option);

        assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        commandLine = new WriteableCommandLineImpl(new OptionBuilder("option").create(), new ArrayList<>());

        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");

        commandLine = new WriteableCommandLineImpl(new OptionBuilder("option").create(), arguments);

        String result = commandLine.toString();

        assertEquals("arg1 arg2 arg3", result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder("option1").create();
        Option option2 = new OptionBuilder("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder("option1").create();
        Option option2 = new OptionBuilder("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();

        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder("test").create();

        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");

        commandLine.setDefaultValues(option, defaults);

        List<String> result = commandLine.getValues(option, null);

        assertEquals(defaults, result);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder("test").create();

        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = Arrays.asList("arg1", "arg2", "arg3");

        commandLine = new WriteableCommandLineImpl(new OptionBuilder("option").create(), arguments);

        List<String> normalised = commandLine.getNormalised();

        assertEquals(arguments, normalised);
    }
}
