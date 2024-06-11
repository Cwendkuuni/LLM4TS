import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList<>());
        commandLine.addOption(new Option("option2"));
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option option1 = (Option) options.get(0);
        assertEquals("option1", option1.getLongOpt());
        Option option2 = (Option) commandLine.getOption("option2");
        assertEquals("option2", option2.getLongOpt());
    }
}