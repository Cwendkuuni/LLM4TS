import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValue(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<String> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("test"));
    }
}