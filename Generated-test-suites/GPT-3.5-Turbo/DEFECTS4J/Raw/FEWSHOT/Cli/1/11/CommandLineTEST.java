import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOptionString() {
        assertFalse(commandLine.hasOption("o"));

        Option option = new Option("o", "optional", false,
                "an optional option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("o"));
        assertFalse(commandLine.hasOption("x"));
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(commandLine.hasOption('o'));

        Option option = new Option("o", "optional", false,
                "an optional option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption('o'));
        assertFalse(commandLine.hasOption('x'));
    }

    @Test
    public void testGetOptionObjectString() {
        assertNull(commandLine.getOptionObject("o"));

        Option option = new Option("o", "optional", false,
                "an optional option");
        option.setType(Integer.class);
        option.setValue("5");
        commandLine.addOption(option);

        assertEquals(5, commandLine.getOptionObject("o"));
    }

    @Test
    public void testGetOptionObjectChar() {
        assertNull(commandLine.getOptionObject('o'));

        Option option = new Option("o", "optional", false,
                "an optional option");
        option.setType(Integer.class);
        option.setValue("5");
        commandLine.addOption(option);

        assertEquals(5, commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValueString() {
        assertNull(commandLine.getOptionValue("o"));

        Option option = new Option("o", "optional", true,
                "an optional option");
        option.setValues(new String[] { "value1", "value2" });
        commandLine.addOption(option);

        assertEquals("value1", commandLine.getOptionValue("o"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(commandLine.getOptionValue('o'));

        Option option = new Option("o", "optional", true,
                "an optional option");
        option.setValues(new String[] { "value1", "value2" });
        commandLine.addOption(option);

        assertEquals("value1", commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValuesString() {
        assertNull(commandLine.getOptionValues("o"));

        Option option = new Option("o", "optional", true,
                "an optional option");
        option.setValues(new String[] { "value1", "value2" });
        commandLine.addOption(option);

        assertArrayEquals(new String[] { "value1", "value2" },
                commandLine.getOptionValues("o"));
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(commandLine.getOptionValues('o'));

        Option option = new Option("o", "optional", true,
                "an optional option");
        option.setValues(new String[] { "value1", "value2" });
        commandLine.addOption(option);

        assertArrayEquals(new String[] { "value1", "value2" },
                commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueStringString() {
        Option option = new Option("o", "optional", true,
                "an optional option");
        option.setValues(new String[] { "value1", "value2" });
        commandLine.addOption(option);

        assertEquals("value1", commandLine.getOptionValue("o", "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue("x", "defaultValue"));
    }

    @Test
    public void testGetOptionValueCharString() {
        Option option = new Option("o", "optional", true,
                "an optional option");
        option.setValues(new String[] { "value1", "value2" });
        commandLine.addOption(option);

        assertEquals("value1", commandLine.getOptionValue('o', "defaultValue"));
        assertEquals("defaultValue", commandLine.getOptionValue('x', "defaultValue"));
    }

    @Test
    public void testGetArgs() {
        assertArrayEquals(new String[] {}, commandLine.getArgs());

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        assertArrayEquals(new String[] { "arg1", "arg2" }, commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        assertTrue(commandLine.getArgList().isEmpty());

        commandLine.addArg("arg1");
        commandLine.addArg("arg2");

        assertEquals(2, commandLine.getArgList().size());
        assertEquals("arg1", commandLine.getArgList().get(0));
        assertEquals("arg2", commandLine.getArgList().get(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator() {
        Iterator<Option> iterator = commandLine.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "optional1", false,
                "an optional option 1");
        Option option2 = new Option("o2", "optional2", false,
                "an optional option 2");

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option[] options = commandLine.getOptions();
        assertEquals(2, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
    }

}