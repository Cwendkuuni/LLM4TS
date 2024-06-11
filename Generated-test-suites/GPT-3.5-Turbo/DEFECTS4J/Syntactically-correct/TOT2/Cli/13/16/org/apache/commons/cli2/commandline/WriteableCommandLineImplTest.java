import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("test", "test", false, "Test option");
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Argument argument = new Argument("arg", true, "Argument");
        cmd.addValue(argument, "value");
        List values = cmd.getValues(argument, Collections.singletonList("default"));
        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("switch", "s", false, "Switch");
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Option option = new Option("hasOption", "h", false, "Has Option");
        cmd.addOption(option);
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        cmd.addProperty("property", "value");
        assertEquals("value", cmd.getProperty("property", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl();
        Set prefixes = new HashSet();
        prefixes.add("-");
        cmd.setPrefixes(prefixes);
        assertTrue(cmd.looksLikeOption("-test"));
    }

}