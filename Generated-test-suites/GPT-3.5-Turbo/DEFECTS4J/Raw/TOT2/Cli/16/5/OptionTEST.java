import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testProcess_BasicScenario() {
        // Create a WriteableCommandLine object
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        // Create a ListIterator with some arguments
        ListIterator args = Arrays.asList("arg1", "arg2", "arg3").listIterator();

        // Create an Option object
        Option option = new OptionImpl();

        // Test the process method
        option.process(commandLine, args);

        // Assert that the commandLine object has been modified as expected
        assertTrue(commandLine.hasOption("arg1"));
        assertFalse(commandLine.hasOption("arg4"));
    }

    // Additional test cases for other methods can be added here

}