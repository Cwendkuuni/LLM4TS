import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test");
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }
}