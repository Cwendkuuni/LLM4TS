import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption_shortName() {
        assertFalse(commandLine.hasOption("a"));
        commandLine.addOption(new Option("a"));
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testHasOption_longName() {
        assertFalse(commandLine.hasOption("option"));
        commandLine.addOption(new Option("option"));
        assertTrue(commandLine.hasOption("option"));
    }

    @Test
    public void testHasOption_invalidOption() {
        assertFalse(commandLine.hasOption("abc"));
    }

    @Test
    public void testGetOptionObject_shortName() {
        commandLine.addOption(new Option("a", "argument", true, "description"));
        commandLine.addArg("argumentValue");
        Object optionObject = commandLine.getOptionObject("a");
        assertNotNull(optionObject);
        assertTrue(optionObject instanceof String);
        assertEquals("argumentValue", (String) optionObject);
    }

    @Test
    public void testGetOptionObject_longName() {
        commandLine.addOption(new Option("option", "argument", true, "description"));
        commandLine.addArg("argumentValue");
        Object optionObject = commandLine.getOptionObject("option");
        assertNotNull(optionObject);
        assertTrue(optionObject instanceof String);
        assertEquals("argumentValue", (String) optionObject);
    }

    @Test
    public void testGetOptionObject_invalidOption() {
        assertNull(commandLine.getOptionObject("abc"));
    }

    @Test
    public void testGetOptionValue_shortName() {
        commandLine.addOption(new Option("a", "description"));
        commandLine.addArg("value");
        String optionValue = commandLine.getOptionValue("a");
        assertNotNull(optionValue);
        assertEquals("value", optionValue);
    }

    @Test
    public void testGetOptionValue_longName() {
        commandLine.addOption(new Option("option", "description"));
        commandLine.addArg("value");
        String optionValue = commandLine.getOptionValue("option");
        assertNotNull(optionValue);
        assertEquals("value", optionValue);
    }

    @Test
    public void testGetOptionValue_invalidOption() {
        assertNull(commandLine.getOptionValue("abc"));
    }

    @Test
    public void testGetOptionValues_shortName() {
        commandLine.addOption(new Option("a", "description"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        String[] optionValues = commandLine.getOptionValues("a");
        assertNotNull(optionValues);
        assertEquals(2, optionValues.length);
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
    }

    @Test
    public void testGetOptionValues_longName() {
        commandLine.addOption(new Option("option", "description"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");
        String[] optionValues = commandLine.getOptionValues("option");
        assertNotNull(optionValues);
        assertEquals(2, optionValues.length);
        assertEquals("value1", optionValues[0]);
        assertEquals("value2", optionValues[1]);
    }

    @Test
    public void testGetOptionValues_invalidOption() {
        assertNull(commandLine.getOptionValues("abc"));
    }

    @Test
    public void testGetOptionValue_defaultValue() {
        String optionValue = commandLine.getOptionValue("a", "defaultValue");
        assertNotNull(optionValue);
        assertEquals("defaultValue", optionValue);
    }

    @Test
    public void testGetOptionValue_invalidOption_defaultValue() {
        String optionValue = commandLine.getOptionValue("abc", "defaultValue");
        assertNotNull(optionValue);
        assertEquals("defaultValue", optionValue);
    }

    @Test
    public void testGetArgs() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testGetArgList() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        List<String> argList = commandLine.getArgList();
        assertNotNull(argList);
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        String[] args = commandLine.getArgs();
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("a"));
        Option[] options = commandLine.getOptions();
        assertNotNull(options);
        assertEquals(1, options.length);
        assertEquals(option, options[0]);
    }
}