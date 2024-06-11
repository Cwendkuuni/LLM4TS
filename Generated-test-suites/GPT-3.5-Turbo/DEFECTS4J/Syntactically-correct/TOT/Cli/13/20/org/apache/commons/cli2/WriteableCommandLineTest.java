import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void addOption_ValidOption_Success() {
        Option option = new Option("test", "description");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void addValue_ValidOptionAndValue_Success() {
        Option option = new Option("test", "description");
        commandLine.addOption(option);

        Object value = new Object();
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void addValue_InvalidOption_Exception() {
        Option option = new Option("test", "description");

        try {
            Object value = new Object();
            commandLine.addValue(option, value);
            fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            // Exception expected, test passes
        }
    }

    @Test
    public void getValues_ValidOption_NoDefaults_Success() {
        Option option = new Option("test", "description");
        commandLine.addOption(option);

        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test
    public void getValues_ValidOption_WithDefaults_Success() {
        Option option = new Option("test", "description");
        commandLine.addOption(option);

        Object value1 = new Object();
        Object value2 = new Object();
        commandLine.addValue(option, value1);

        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(value2);
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assertEquals(expectedValues, commandLine.getValues(option));
    }

    @Test
    public void setDefaultValues_ValidOption_Success() {
        Option option = new Option("test", "description");
        commandLine.addOption(option);

        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(value1);
        defaultValues.add(value2);

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void addSwitch_ValidOptionAndValue_Success() {
        Option option = new Option("test", "description", true);
        commandLine.addOption(option);

        boolean value = true;
        commandLine.addSwitch(option, value);

        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void addSwitch_InvalidOption_Exception() {
        Option option = new Option("test", "description");

        try {
            boolean value = true;
            commandLine.addSwitch(option, value);
            fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            // Exception expected, test passes
        }
    }

    @Test
    public void setDefaultSwitch_ValidOption_Success() {
        Option option = new Option("test", "description");
        commandLine.addOption(option);

        Boolean defaultValue = true;
        commandLine.setDefaultSwitch(option, defaultValue);

        assertEquals(defaultValue, commandLine.getDefaultSwitch(option));
    }

    @Test
    public void addProperty_ValidProperty_Success() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void looksLikeOption_ValidArgument_True() {
        String argument = "-option";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void looksLikeOption_InvalidArgument_False() {
        String argument = "value";
        assertFalse(commandLine.looksLikeOption(argument));
    }
}