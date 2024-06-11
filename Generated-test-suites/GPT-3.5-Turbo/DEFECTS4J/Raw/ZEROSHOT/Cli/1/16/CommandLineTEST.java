import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmdLine = new CommandLine();

        // Add some options to the command line
        Option option1 = new Option("a");
        Option option2 = new Option("b");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        // Test if the options are set
        assertTrue(cmdLine.hasOption("a"));
        assertTrue(cmdLine.hasOption("b"));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmdLine = new CommandLine();

        // Add an option to the command line
        Option option1 = new Option("a");
        option1.setType(Integer.class);
        option1.addValue("123");
        cmdLine.addOption(option1);

        // Test if the option value is returned as Integer object
        assertEquals(Integer.valueOf(123), cmdLine.getOptionObject("a"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmdLine = new CommandLine();

        // Add an option to the command line
        Option option1 = new Option("a");
        option1.addValue("value");
        cmdLine.addOption(option1);

        // Test if the option value is returned as String
        assertEquals("value", cmdLine.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmdLine = new CommandLine();

        // Add an option with multiple values to the command line
        Option option1 = new Option("a");
        option1.addValue("value1");
        option1.addValue("value2");
        cmdLine.addOption(option1);

        // Test if the option values are returned as array
        String[] expectedValues = {"value1", "value2"};
        assertArrayEquals(expectedValues, cmdLine.getOptionValues("a"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmdLine = new CommandLine();

        // Add some left-over unrecognized arguments
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");

        // Test if the left-over arguments are returned as array
        String[] expectedArgs = {"arg1", "arg2"};
        assertArrayEquals(expectedArgs, cmdLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        CommandLine cmdLine = new CommandLine();

        // Add some left-over unrecognized arguments
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");

        // Test if the left-over arguments are returned as list
        List<String> expectedArgs = new ArrayList<String>();
        expectedArgs.add("arg1");
        expectedArgs.add("arg2");
        assertEquals(expectedArgs, cmdLine.getArgList());
    }
}