import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        Object value = "testValue";

        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }
    
    @Test
    public void testGetArgumentValues() {
        Option option = new Option("testOption");
        Object value1 = "value1";
        Object value2 = "value2";

        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");

        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);

        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch() {
        Option option = new Option("testSwitch");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitches().contains(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testSwitch");
        commandLine.setDefaultSwitch(option, false);

        assertFalse(commandLine.getSwitchValue(option));
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
        String argument = "--testOption";

        assertTrue(commandLine.looksLikeOption(argument));
    }
}