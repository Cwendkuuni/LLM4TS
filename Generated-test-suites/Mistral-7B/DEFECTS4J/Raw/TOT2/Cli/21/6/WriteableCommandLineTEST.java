import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class WriteableCommandLineTest {

    @Test
    public void testAddOptionSameName() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("Test", new String[0]);
        Option option1 = new Option("t", "testOption1", true, "Test Option 1");
        commandLine.addOption(option1);
        Option option2 = new Option("t", "testOption2", true, "Test Option 2");
        try {
            commandLine.addOption(option2);
            assertFalse("Should not be able to add an option with the same name.", true);
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddOptionNullName() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("Test", new String[0]);
        Option option = new Option(null, "testOption", true, "Test Option");
        try {
            commandLine.addOption(option);
            assertFalse("Should not be able to add an option with a null name.", true);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("Test", new String[0]);
        Option option = new Option("t", "testOption", true, "Test Option");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOption("t"));
    }
}

//