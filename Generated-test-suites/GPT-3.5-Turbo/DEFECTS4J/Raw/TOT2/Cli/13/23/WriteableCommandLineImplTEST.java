import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, arguments);
        Option option = new OptionBuilder().create();
        cmdLine.addOption(option);
        assertTrue(cmdLine.getOptions().contains(option));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, arguments);
        Option option = new OptionBuilder().create();
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
        assertFalse(cmdLine.hasOption(new OptionBuilder().create()));
    }

    // Add more test cases for other methods in WriteableCommandLineImpl class...

}