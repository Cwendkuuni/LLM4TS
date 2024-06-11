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
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals(value, commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        assertEquals(2, commandLine.getValues(option).size());
        assertTrue(commandLine.getValues(option).contains(value1));
        assertTrue(commandLine.getValues(option).contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<Object> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.getSwitches().containsKey(option));
        assertEquals(value, commandLine.getSwitches().get(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_duplicateSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
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
        String argument = "-test";
        assertTrue(commandLine.looksLikeOption(argument));
    }
}