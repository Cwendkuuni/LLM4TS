import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("o", "option");
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("o", "option");
        Object value = "value";
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("o", "option");
        Object value = "value";
        commandLine.addValue(option, value);

        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(1, undefaultedValues.size());
        assertEquals(value, undefaultedValues.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("o", "option");
        List<Object> defaultValues = new ArrayList<>(Arrays.asList("default1", "default2"));
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> retrievedDefaultValues = commandLine.getDefaultValues(option);
        assertEquals(defaultValues, retrievedDefaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("s", "switch");
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("s", "switch");
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("p", "property");
        String propertyName = "name";
        String propertyValue = "value";
        commandLine.addProperty(option, propertyName, propertyValue);

        String retrievedValue = commandLine.getProperty(option, propertyName);
        assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String propertyName = "name";
        String propertyValue = "value";
        commandLine.addProperty(propertyName, propertyValue);

        String retrievedValue = commandLine.getProperty(propertyName);
        assertEquals(propertyValue, retrievedValue);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-o";
        assertTrue(commandLine.looksLikeOption(argument));

        argument = "--option";
        assertTrue(commandLine.looksLikeOption(argument));

        argument = "value";
        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl("o", "option");
        commandLine.setCurrentOption(option);

        assertEquals(option, commandLine.getCurrentOption());
    }
}