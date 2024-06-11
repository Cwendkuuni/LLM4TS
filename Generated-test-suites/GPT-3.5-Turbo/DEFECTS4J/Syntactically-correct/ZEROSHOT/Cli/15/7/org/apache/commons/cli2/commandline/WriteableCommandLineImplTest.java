import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private Option option1;
    private Option option2;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create options for testing
        option1 = new Option.Builder("o1")
                .withDescription("Option 1")
                .create();
        option2 = new Option.Builder("o2")
                .withDescription("Option 2")
                .create();

        // Create command line
        commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(option1));
    }

    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option1, value);
        assertEquals(value, commandLine.getValues(option1, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        assertEquals(true, commandLine.getSwitch(option1, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option2));
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        assertEquals(option1, commandLine.getOption("o1"));
        assertNull(commandLine.getOption("o3"));
    }

    @Test
    public void testGetValues() {
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option1, value1);
        commandLine.addValue(option1, value2);
        assertEquals(value1, commandLine.getValues(option1, null).get(0));
        assertEquals(value2, commandLine.getValues(option1, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option1, value1);
        commandLine.addValue(option1, value2);
        assertEquals(value1, commandLine.getUndefaultedValues(option1).get(0));
        assertEquals(value2, commandLine.getUndefaultedValues(option1).get(1));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        assertEquals(true, commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
        assertNull(commandLine.getProperty("property2"));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(option1, property, value);
        assertEquals(value, commandLine.getProperty(option1, property, null));
        assertNull(commandLine.getProperty(option2, property, null));
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(option1, property1, value1);
        commandLine.addProperty(option1, property2, value2);
        assertTrue(commandLine.getProperties().contains(property1));
        assertTrue(commandLine.getProperties().contains(property2));
        assertFalse(commandLine.getProperties(option2).contains(property1));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        WriteableCommandLineImpl commandLine2 = new WriteableCommandLineImpl(option1, arguments);
        assertEquals(arg1 + " " + arg2, commandLine2.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptionTriggers().contains("o1"));
        assertTrue(commandLine.getOptionTriggers().contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        commandLine.setDefaultValues(option1, defaults);
        assertEquals(defaults, commandLine.getValues(option1, null));
        commandLine.setDefaultValues(option1, null);
        assertEquals(defaults, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        assertEquals(true, commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        String arg1 = "arg1";
        String arg2 = "arg2";
        List<String> arguments = new ArrayList<>();
        arguments.add(arg1);
        arguments.add(arg2);
        WriteableCommandLineImpl commandLine2 = new WriteableCommandLineImpl(option1, arguments);
        assertEquals(arguments, commandLine2.getNormalised());
    }
}