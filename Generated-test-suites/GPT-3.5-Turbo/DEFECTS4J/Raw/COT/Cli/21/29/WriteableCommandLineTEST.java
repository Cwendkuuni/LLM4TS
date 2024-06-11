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
        Option option = new Option("option");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption("option"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option");
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValue(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("option");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option");
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        assertEquals(Arrays.asList("default1", "default2"), commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("switch");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false); // Should throw an exception
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("switch");
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option");
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("value1", commandLine.getProperty(option, "property1"));
        assertEquals("value2", commandLine.getProperty(option, "property2"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}