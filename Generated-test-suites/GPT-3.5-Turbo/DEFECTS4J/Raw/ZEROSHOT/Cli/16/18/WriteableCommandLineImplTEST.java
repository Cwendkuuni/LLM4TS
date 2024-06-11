import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Argument argument1 = new Argument("argument1");

        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(argument1));
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("option3");
        commandLine.addOption(option3);

        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("option1");

        commandLine.addValue(option1, "value1");

        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option1, null));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new Option("option1");

        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        Option option2 = new Option("option2");

        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = commandLine.getOption("option1");

        assertNotNull(option1);
        assertEquals("option1", option1.getPreferredName());
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("option1");
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option1, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument1 = new Argument("argument1");

        assertEquals(Collections.singletonList("argument1"), commandLine.getUndefaultedValues(argument1));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("option1");

        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetProperty() {
        Option option1 = new Option("option1");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option1, property, value);

        assertEquals(value, commandLine.getProperty(option1, property, null));
    }

    @Test
    public void testAddProperty() {
        Option option1 = new Option("option1");
        String property = "property";
        String value = "value";

        commandLine.addProperty(option1, property, value);

        assertEquals(value, commandLine.getProperty(option1, property, null));
    }

    @Test
    public void testGetProperties() {
        Option option1 = new Option("option1");

        String property1 = "property1";
        String value1 = "value1";
        commandLine.addProperty(option1, property1, value1);

        Set<String> properties = commandLine.getProperties(option1);

        assertEquals(Collections.singleton(property1), properties);
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "-option";

        assertTrue(commandLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        Argument argument1 = new Argument("argument1");

        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(argument1));

        assertEquals("argument1", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Option option3 = new Option("option3");

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        List<Option> options = commandLine.getOptions();

        assertEquals(Arrays.asList(option1, option2, option3), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Option option3 = new Option("option3");

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);

        Set<String> optionTriggers = commandLine.getOptionTriggers();

        assertEquals(Arrays.asList("option1", "option2", "option3"), optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("option1");
        List<String> defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(option1, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("option1");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option1, defaultSwitch);

        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = Arrays.asList("argument1", "argument2", "argument3");

        commandLine = new WriteableCommandLineImpl(option1, arguments);

        assertEquals(arguments, commandLine.getNormalised());
    }
}