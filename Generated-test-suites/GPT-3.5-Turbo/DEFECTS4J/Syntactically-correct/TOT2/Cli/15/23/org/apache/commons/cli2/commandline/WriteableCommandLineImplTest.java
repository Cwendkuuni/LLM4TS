import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withLongName("test").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withLongName("test").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument("arg", false);
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withLongName("test").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddDuplicateSwitch() {
        Option option = new OptionBuilder().withLongName("test").create();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testGetProperty() {
        String property = "key";
        String value = "value";
        Option option = new OptionBuilder().create();
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testNormalisedToString() {
        List arguments = Arrays.asList("arg1", "arg2");
        Option option = new OptionBuilder().withLongName("test").create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);
        assertEquals("arg1 arg2", commandLine.toString());
    }
}