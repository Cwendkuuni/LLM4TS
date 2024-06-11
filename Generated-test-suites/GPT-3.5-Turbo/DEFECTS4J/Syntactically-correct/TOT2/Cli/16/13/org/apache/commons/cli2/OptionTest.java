import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    @Test
    public void testProcess() {
        // Test basic functionality of process method
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        List<String> args = Arrays.asList("-option1");
        ListIterator<String> argsIterator = args.listIterator();
        Option option = new OptionImpl();
        
        try {
            option.process(commandLine, argsIterator);
            assertTrue(commandLine.hasOption("option1"));
        } catch (OptionException e) {
            fail("Exception thrown when processing valid argument");
        }
    }

    @Test
    public void testDefaults() {
        // Test defaults method with empty command line
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();
        
        option.defaults(commandLine);
        // Assert any defaults applied are in commandLine
        assertEquals(0, commandLine.getOptions().size());
    }

    // Additional test cases for other methods can be added here
    
}