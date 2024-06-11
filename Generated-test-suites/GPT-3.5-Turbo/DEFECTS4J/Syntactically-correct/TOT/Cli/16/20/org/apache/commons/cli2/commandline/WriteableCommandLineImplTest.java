import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option.Builder("option1").withDescription("Option 1").create();
        Option option2 = new Option.Builder("option2").withDescription("Option 2").create();

        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        // Check if options are added correctly
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        // Add a value to the option
        commandLine.addValue(option1, "value1");

        // Check if value is added correctly
        assertEquals(1, commandLine.getValues(option1, null).size());
        assertEquals("value1", commandLine.getValues(option1, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        // Add a switch to the option
        commandLine.addSwitch(option1, true);

        // Check if switch is added correctly
        assertTrue(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        // Check if the command line has the option
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(new Option.Builder("option3").withDescription("Option 3").create()));
    }

    @Test
    public void testGetOption() {
        // Check if the correct option is returned
        assertEquals(option1, commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option3"));
    }

    @Test
    public void testGetValues() {
        // Add values to the option
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        // Check if the correct values are returned
        assertEquals(2, commandLine.getValues(option1, null).size());
        assertEquals("value1", commandLine.getValues(option1, null).get(0));
        assertEquals("value2", commandLine.getValues(option1, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Add values to the option
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        // Check if the correct values are returned
        assertEquals(2, commandLine.getUndefaultedValues(option1).size());
        assertEquals("value1", commandLine.getUndefaultedValues(option1).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option1).get(1));
    }

    @Test
    public void testGetSwitch() {
        // Add a switch to the option
        commandLine.addSwitch(option1, true);

        // Check if the correct switch is returned
        assertTrue(commandLine.getSwitch(option1, null));

        // Check if the default switch is returned when no switch is added
        assertFalse(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testGetProperty() {
        // Add a property to the option
        commandLine.addProperty(option1, "property1", "value1");

        // Check if the correct property value is returned
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));

        // Check if the default value is returned when no property is added
        assertNull(commandLine.getProperty(option2, "property1", null));
    }

    @Test
    public void testLooksLikeOption() {
        // Check if the trigger looks like an option
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertTrue(commandLine.looksLikeOption("--option1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        // Add values to the command line
        commandLine.getValue().add("value1");
        commandLine.getValue().add("value2");

        // Check if the correct string representation is returned
        assertEquals("-option1 value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        // Check if the correct options are returned
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        // Check if the correct option triggers are returned
        assertEquals(2, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        // Set default values for the option
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        commandLine.setDefaultValues(option1, defaults);

        // Check if the default values are set correctly
        assertEquals(defaults, commandLine.getValues(option1, null));

        // Check if the default values are removed when set to null
        commandLine.setDefaultValues(option1, null);
        assertNull(commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Set a default switch for the option
        commandLine.setDefaultSwitch(option1, true);

        // Check if the default switch is set correctly
        assertTrue(commandLine.getSwitch(option1, null));

        // Check if the default switch is removed when set to null
        commandLine.setDefaultSwitch(option1, null);
        assertNull(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        // Check if the correct normalised values are returned
        assertEquals(0, commandLine.getNormalised().size());
    }
}