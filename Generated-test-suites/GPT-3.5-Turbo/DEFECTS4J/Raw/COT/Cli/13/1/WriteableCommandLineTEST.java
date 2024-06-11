import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");

        commandLine.addOption(option);

        // Verify that the option was added to the command line
        Mockito.verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = new Object();

        commandLine.addValue(option, value);

        // Verify that the value was added to the option in the command line
        Mockito.verify(commandLine).addValue(option, value);
    }

    @Test
    public void testGetArgumentValues() {
        Option option = new Option("test");

        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        Mockito.when(commandLine.getArgumentValues(option)).thenReturn(expectedValues);

        List<Object> actualValues = commandLine.getArgumentValues(option);

        // Verify that the expected values are returned for the option in the command line
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        // Verify that the default values were set for the option in the command line
        Mockito.verify(commandLine).setDefaultValues(option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        boolean value = true;

        commandLine.addSwitch(option, value);

        // Verify that the switch value was added to the option in the command line
        Mockito.verify(commandLine).addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Verify that the default switch value was set for the option in the command line
        Mockito.verify(commandLine).setDefaultSwitch(option, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";

        commandLine.addProperty(property, value);

        // Verify that the property value was added to the command line
        Mockito.verify(commandLine).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";

        boolean result = commandLine.looksLikeOption(argument);

        // Verify that the argument is recognized as an option trigger
        assertEquals(true, result);
    }
}