import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        commandLine.addOption(option);

        Object value = new Object();
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");
        commandLine.addOption(option);

        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> values = commandLine.getArgumentValues(option);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        commandLine.addOption(option);

        List<Object> defaultValues = new ArrayList<>();
        Object defaultValue1 = new Object();
        Object defaultValue2 = new Object();
        defaultValues.add(defaultValue1);
        defaultValues.add(defaultValue2);

        commandLine.setDefaultValues(option, defaultValues);

        List<Object> retrievedDefaultValues = commandLine.getDefaultValues(option);
        assertEquals(2, retrievedDefaultValues.size());
        assertTrue(retrievedDefaultValues.contains(defaultValue1));
        assertTrue(retrievedDefaultValues.contains(defaultValue2));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getDefaultSwitch(option));
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
        String optionArgument = "-test";
        assertTrue(commandLine.looksLikeOption(optionArgument));

        String nonOptionArgument = "test";
        assertFalse(commandLine.looksLikeOption(nonOptionArgument));
    }
}