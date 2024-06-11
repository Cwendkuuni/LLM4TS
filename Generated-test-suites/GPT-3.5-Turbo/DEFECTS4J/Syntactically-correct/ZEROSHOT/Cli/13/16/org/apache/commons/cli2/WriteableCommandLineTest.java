import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import java.util.List;

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
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<Object> defaultValues = List.of("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<Object> values = commandLine.getValues(option);
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option));
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