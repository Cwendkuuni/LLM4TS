import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new Option("test");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    // Add additional test cases for edge cases and error scenarios

}