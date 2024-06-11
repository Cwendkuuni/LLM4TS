import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
        option = new OptionImpl("test");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option);
        assertEquals(2, undefaultedValues.size());
        assertTrue(undefaultedValues.contains(value1));
        assertTrue(undefaultedValues.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option, value);

        assertTrue(commandLine.getSwitch(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-t";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testDoesNotLookLikeOption() {
        String argument = "value";
        assertFalse(commandLine.looksLikeOption(argument));
    }
}