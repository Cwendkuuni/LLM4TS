import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        // Create a sample root option and arguments
        Option rootOption = new OptionBuilder("r").withDescription("Root Option").create();
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        // Initialize WriteableCommandLineImpl
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder("o").withDescription("Test Option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder("o").withDescription("Test Option").create();
        commandLine.addValue(option, "value1");
        assertEquals(commandLine.getValues(option, null).get(0), "value1");
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder("o").withDescription("Test Option").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder("o").withDescription("Test Option").create();
        commandLine.addOption(option);
        assertEquals(commandLine.getOption("o"), option);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        assertEquals(commandLine.toString(), "arg1 arg2");
    }

    // Include additional tests for other methods

}