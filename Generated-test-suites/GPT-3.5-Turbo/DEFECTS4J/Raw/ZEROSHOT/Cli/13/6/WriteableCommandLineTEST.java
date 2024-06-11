import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("testOption");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("testOption");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option);

        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("testOption");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("testOption");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1"));
        assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }
}