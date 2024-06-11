import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder().withDescription("Root option").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withDescription("Test option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentBuilder().withName("arg1").create();
        Object value = "value1";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withDescription("Test option").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withDescription("Test option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withDescription("Test option").create();
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption(option.getPreferredName());
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("key", "value");
        assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    // Add more test cases for other methods

}