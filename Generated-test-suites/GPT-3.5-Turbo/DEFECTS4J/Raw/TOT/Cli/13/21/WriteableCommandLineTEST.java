import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private String property;
    private String value;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = Mockito.mock(Option.class);
        property = "property";
        value = "value";
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);

        Mockito.verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, value);

        Mockito.verify(commandLine).addValue(option, value);
    }

    @Test
    public void testGetValuesForOption_existingValues() {
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");

        Mockito.when(commandLine.getValuesForOption(option)).thenReturn(values);

        List<Object> actualValues = commandLine.getValuesForOption(option);

        Assert.assertEquals(values, actualValues);
    }

    @Test
    public void testGetValuesForOption_noExistingValues() {
        List<Object> actualValues = commandLine.getValuesForOption(option);

        Assert.assertNotNull(actualValues);
        Assert.assertEquals(0, actualValues.size());
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        Mockito.verify(commandLine).setDefaultValues(option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;

        commandLine.addSwitch(option, value);

        Mockito.verify(commandLine).addSwitch(option, value);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_duplicateSwitch() {
        boolean value = true;

        // Adding switch once
        commandLine.addSwitch(option, value);

        // Adding switch again, should throw exception
        commandLine.addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        Mockito.verify(commandLine).setDefaultSwitch(option, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(property, value);

        Mockito.verify(commandLine).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOption_true() {
        String argument = "-option";

        boolean result = commandLine.looksLikeOption(argument);

        Assert.assertTrue(result);
    }

    @Test
    public void testLooksLikeOption_false() {
        String argument = "value";

        boolean result = commandLine.looksLikeOption(argument);

        Assert.assertFalse(result);
    }
}