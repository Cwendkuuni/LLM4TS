import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;

    private Option mockOption;
    private List arguments;

    @Before
    public void setup() {
        mockOption = mock(Option.class);
        arguments = new ArrayList();

        writeableCommandLine = new WriteableCommandLineImpl(mockOption, arguments);
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(mockOption);

        assertTrue(writeableCommandLine.hasOption(mockOption));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();

        writeableCommandLine.addValue(mockOption, value);

        List<Object> values = writeableCommandLine.getValues(mockOption, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;

        writeableCommandLine.addSwitch(mockOption, value);

        Boolean switchValue = writeableCommandLine.getSwitch(mockOption, null);
        assertEquals(Boolean.valueOf(value), switchValue);
    }

    @Test
    public void testHasOption() {
        assertFalse(writeableCommandLine.hasOption(mockOption));
    }

    @Test
    public void testGetOption() {
        String trigger = "trigger";
        when(mockOption.getPreferredName()).thenReturn(trigger);

        writeableCommandLine.addOption(mockOption);

        Option option = writeableCommandLine.getOption(trigger);
        assertEquals(mockOption, option);
    }

    @Test
    public void testGetValues() {
        Object value = new Object();

        writeableCommandLine.addValue(mockOption, value);

        List<Object> values = writeableCommandLine.getValues(mockOption, null);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Object value = new Object();

        writeableCommandLine.addValue(mockOption, value);

        List<Object> values = writeableCommandLine.getUndefaultedValues(mockOption);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetSwitch() {
        boolean defaultValue = true;

        Boolean switchValue = writeableCommandLine.getSwitch(mockOption, defaultValue);
        assertEquals(Boolean.valueOf(defaultValue), switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";

        writeableCommandLine.addProperty(property, value);

        String retrievedValue = writeableCommandLine.getProperty(property);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        writeableCommandLine.addProperty(mockOption, property, value);

        String retrievedValue = writeableCommandLine.getProperty(mockOption, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property";
        String defaultValue = "defaultValue";

        String retrievedValue = writeableCommandLine.getProperty(mockOption, property, defaultValue);
        assertEquals(defaultValue, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty(mockOption, "property1", "value1");
        writeableCommandLine.addProperty(mockOption, "property2", "value2");

        int expectedSize = 2;
        int actualSize = writeableCommandLine.getProperties(mockOption).size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "trigger";
        when(mockOption.getPrefixes()).thenReturn(Collections.singleton("--"));

        String validOption = "--" + trigger;
        assertTrue(writeableCommandLine.looksLikeOption(validOption));

        String invalidOption = "-" + trigger;
        assertFalse(writeableCommandLine.looksLikeOption(invalidOption));
    }

    @Test
    public void testToString() {
        String argument = "argument";
        arguments.add(argument);

        String expectedString = argument;
        String actualString = writeableCommandLine.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetOptions() {
        writeableCommandLine.addOption(mockOption);

        int expectedSize = 1;
        int actualSize = writeableCommandLine.getOptions().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetOptionTriggers() {
        String trigger = "trigger";
        when(mockOption.getPreferredName()).thenReturn(trigger);

        writeableCommandLine.addOption(mockOption);

        int expectedSize = 1;
        int actualSize = writeableCommandLine.getOptionTriggers().size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaults = new ArrayList<>();
        defaults.add(new Object());

        writeableCommandLine.setDefaultValues(mockOption, defaults);

        List<Object> retrievedDefaults = writeableCommandLine.getValues(mockOption, null);
        assertEquals(defaults, retrievedDefaults);
    }

    @Test
    public void testSetDefaultSwitch() {
        boolean defaultSwitch = true;

        writeableCommandLine.setDefaultSwitch(mockOption, defaultSwitch);

        Boolean retrievedSwitch = writeableCommandLine.getSwitch(mockOption, null);
        assertEquals(Boolean.valueOf(defaultSwitch), retrievedSwitch);
    }

    @Test
    public void testGetNormalised() {
        int expectedSize = 0;
        int actualSize = writeableCommandLine.getNormalised().size();
        assertEquals(expectedSize, actualSize);
    }
}