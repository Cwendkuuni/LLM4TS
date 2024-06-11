import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> args = new ArrayList<>();
        args.add("testArg");
        Option option = new OptionImpl();
        try {
            option.process(commandLine, args.listIterator());
            assertTrue("Process method should add arguments to CommandLine", commandLine.hasOption("testArg"));
        } catch (OptionException e) {
            fail("Exception thrown when processing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        option.defaults(commandLine);
        // test if defaults are successfully added to CommandLine
        assertTrue("Defaults should be added to CommandLine", commandLine.hasOption("defaultArg"));
    }

    @Test
    public void testCanProcessWithString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        assertTrue("Option should be able to process the argument", option.canProcess(commandLine, "testArg"));
    }

    @Test
    public void testCanProcessWithListIterator() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        List<String> args = new ArrayList<>();
        args.add("testArg");
        assertTrue("Option should be able to process the argument from ListIterator", option.canProcess(commandLine, args.listIterator()));
    }

    @Test
    public void testGetTriggers() {
        Option option = new OptionImpl();
        Set triggers = option.getTriggers();
        // test if triggers set is not null
        assertNotNull("Triggers set should not be null", triggers);
    }

    // Additional test cases for other methods can be added here

}