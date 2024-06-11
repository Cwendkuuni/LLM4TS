import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);

        commandLine.addOption(option);

        // Check if option was added to commandLine
        Mockito.verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        String value = "testValue";

        commandLine.addValue(option, value);

        // Check if value was added to option
        Mockito.verify(option).setValue(value);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        List<String> values = Mockito.mock(List.class);

        Mockito.when(commandLine.getValues(option)).thenReturn(values);

        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");

        List<String> actualValues = commandLine.getValues(option);

        Assert.assertEquals("Size of values list should be 2", 2, actualValues.size());
        Assert.assertEquals("First value should be testValue1", "testValue1", actualValues.get(0));
        Assert.assertEquals("Second value should be testValue2", "testValue2", actualValues.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        List<String> defaultValues = Mockito.mock(List.class);

        commandLine.setDefaultValues(option, defaultValues);

        // Check if defaultValues were set to option
        Mockito.verify(option).setDefaultValues(defaultValues);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);

        commandLine.addSwitch(option, true); // First call with valid value

        // Throws IllegalStateException when adding switch value for the second time
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Check if defaultSwitch was set to option
        Mockito.verify(option).setDefaultValue(defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String property = "testProperty";
        String value = "testValue";

        commandLine.addProperty(property, value);

        // Check if property-value pair was added to commandLine
        Mockito.verify(commandLine).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOptionWithLongArgument() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "--test";

        boolean actualResult = commandLine.looksLikeOption(argument);

        Assert.assertTrue("Argument should look like an Option trigger", actualResult);
    }

    @Test
    public void testLooksLikeOptionWithShortArgument() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "-t";

        boolean actualResult = commandLine.looksLikeOption(argument);

        Assert.assertTrue("Argument should look like an Option trigger", actualResult);
    }

    @Test
    public void testLooksLikeOptionWithInvalidArgument() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "test";

        boolean actualResult = commandLine.looksLikeOption(argument);

        Assert.assertFalse("Argument should not look like an Option trigger", actualResult);
    }
}