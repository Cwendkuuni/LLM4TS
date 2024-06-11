import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test cases for the CommandLine class.
 */
public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        // Test when option is set
        commandLine.addOption(new Option("a", "optionA", false, "Option A"));
        assertTrue(commandLine.hasOption("a"));

        // Test when option is not set
        assertFalse(commandLine.hasOption("b"));

        // Test when option is set with different char name
        commandLine.addOption(new Option("c", "optionC", false, "Option C"));
        assertTrue(commandLine.hasOption('c'));

        // Test when option is not set with any matching name
        assertFalse(commandLine.hasOption('d'));
    }

    @Test
    public void testGetOptionObject() {
        // Test when option is set
        commandLine.addOption(new Option("a", "optionA", true, "Option A", Integer.class));
        commandLine.addOptionValue("a", "10");
        assertEquals(10, commandLine.getOptionObject("a"));

        // Test when option is not set
        assertNull(commandLine.getOptionObject("b"));

        // Test when option is set with different char name
        commandLine.addOption(new Option("c", "optionC", true, "Option C", String.class));
        commandLine.addOptionValue('c', "testing");
        assertEquals("testing", commandLine.getOptionObject('c'));

        // Test when option is not set with any matching name
        assertNull(commandLine.getOptionObject('d'));
    }

    @Test
    public void testGetOptionValue() {
        // Test when option is set
        commandLine.addOption(new Option("a", "optionA", true, "Option A"));
        commandLine.addOptionValue("a", "valueA");
        assertEquals("valueA", commandLine.getOptionValue("a"));

        // Test when option is not set
        assertNull(commandLine.getOptionValue("b"));

        // Test when option is set with different char name
        commandLine.addOption(new Option("c", "optionC", true, "Option C"));
        commandLine.addOptionValue('c', "valueC");
        assertEquals("valueC", commandLine.getOptionValue('c'));

        // Test when option is not set with any matching name
        assertNull(commandLine.getOptionValue('d'));
    }

    @Test
    public void testGetOptionValues() {
        // Test when option is set
        commandLine.addOption(new Option("a", "optionA", true, "Option A"));
        commandLine.addOptionValues("a", new String[]{"valueA1", "valueA2"});
        assertArrayEquals(new String[]{"valueA1", "valueA2"}, commandLine.getOptionValues("a"));

        // Test when option is not set
        assertNull(commandLine.getOptionValues("b"));

        // Test when option is set with different char name
        commandLine.addOption(new Option("c", "optionC", true, "Option C"));
        commandLine.addOptionValues('c', new String[]{"valueC1", "valueC2"});
        assertArrayEquals(new String[]{"valueC1", "valueC2"}, commandLine.getOptionValues('c'));

        // Test when option is not set with any matching name
        assertNull(commandLine.getOptionValues('d'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue() {
        // Test when option is set
        commandLine.addOption(new Option("a", "optionA", true, "Option A"));
        commandLine.addOptionValue("a", "valueA");
        assertEquals("valueA", commandLine.getOptionValue("a", "defaultA"));

        // Test when option is not set
        assertEquals("defaultB", commandLine.getOptionValue("b", "defaultB"));

        // Test when option is set with different char name
        commandLine.addOption(new Option("c", "optionC", true, "Option C"));
        commandLine.addOptionValue('c', "valueC");
        assertEquals("valueC", commandLine.getOptionValue('c', "defaultC"));

        // Test when option is not set with any matching name
        assertEquals("defaultD", commandLine.getOptionValue('d', "defaultD"));
    }

    @Test
    public void testGetArgs() {
        // Test when args list is not empty
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());

        // Test when args list is empty
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testGetArgList() {
        // Test when args list is not empty
        commandLine.addArg("arg1");
        commandLine.addArg("arg2");
        assertEquals(2, commandLine.getArgList().size());

        // Test when args list is empty
        assertEquals(0, commandLine.getArgList().size());
    }

    @Test
    public void testAddOption() {
        // Test adding a new option
        Option optionA = new Option("a", "optionA", true, "Option A");
        commandLine.addOption(optionA);
        assertEquals(optionA, commandLine.getOptions()[0]);

        // Test adding an existing option with different char name
        Option optionC = new Option("c", "optionC", true, "Option C");
        commandLine.addOption(optionC);
        Option optionC1 = new Option("c", "optionC", true, "Option C with different char name");
        commandLine.addOption(optionC1);
        assertEquals(optionC, commandLine.getOptions()[1]);
    }

    @Test
    public void testIterator() {
        // Test when iterator has options
        Option optionA = new Option("a", "optionA", true, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        assertNotNull(commandLine.iterator());

        // Test when iterator is empty
        Option[] options = commandLine.getOptions();
        for (Option option : options) {
            commandLine.removeOption(option);
        }
        assertFalse(commandLine.iterator().hasNext());
    }

    @Test
    public void testGetOptions() {
        // Test when options exist
        Option optionA = new Option("a", "optionA", true, "Option A");
        Option optionB = new Option("b", "optionB", false, "Option B");
        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        assertEquals(2, commandLine.getOptions().length);

        // Test when options do not exist
        Option[] options = commandLine.getOptions();
        for (Option option : options) {
            commandLine.removeOption(option);
        }
        assertEquals(0, commandLine.getOptions().length);
    }

}