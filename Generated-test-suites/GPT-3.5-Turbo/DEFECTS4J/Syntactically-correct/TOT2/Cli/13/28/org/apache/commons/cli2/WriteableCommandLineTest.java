import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.hasValue(option));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, values.get(0));
        commandLine.addValue(option, values.get(1));
        List<String> argumentValues = commandLine.getArgumentValues(option);
        assertEquals(values, argumentValues);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertTrue(commandLine.hasDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;
        commandLine.addSwitch(option, value);
        assertTrue(commandLine.hasSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.hasDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);
        assertTrue(commandLine.getProperty(property) == value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }
}