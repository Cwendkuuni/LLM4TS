import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("opt1", "Option 1", false);
        Option option2 = new Option("opt2", "Option 2", true);
        Argument argument1 = new Argument("arg1", "Argument 1", true);
        Argument argument2 = new Argument("arg2", "Argument 2", false);
        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);
        arguments.add(argument2);
        commandLine = new WriteableCommandLineImpl(options, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, new ArrayList<>()).size());
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("opt4", "Option 4", true)));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("opt3"));
        assertNull(commandLine.getOption("opt4"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        assertEquals(2, commandLine.getValues(option, defaultValues).size());
        assertEquals("defaultValue1", commandLine.getValues(option, defaultValues).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(new Option("opt4", "Option 4", true), false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals("value1", commandLine.getProperty("property1", "defaultValue1"));
        assertEquals("defaultValue2", commandLine.getProperty("property3", "defaultValue2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-opt1"));
        assertTrue(commandLine.looksLikeOption("--opt2"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        assertEquals("", commandLine.toString());
        commandLine.addValue(new Argument("arg1", "Argument 1", true), "value1");
        commandLine.addValue(new Argument("arg2", "Argument 2", false), "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(4, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("opt3", "Option 3", true);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(2, commandLine.getValues(option, new ArrayList<>()).size());
        assertEquals("defaultValue1", commandLine.getValues(option, new ArrayList<>()).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("opt3", "Option 3", true);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<Object> normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}