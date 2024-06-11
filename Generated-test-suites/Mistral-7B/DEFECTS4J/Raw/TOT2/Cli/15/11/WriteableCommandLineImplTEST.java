import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Given
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        commandLine.addOption(option);

        // Then
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        // Given
        Option option = new Option("a", "argument", 1, "Argument description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        commandLine.addValue(option, "value1");

        // Then
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // Given
        Option option = new Option("s", "switch", false, "Switch description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        commandLine.addSwitch(option, true);

        // Then
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        // Given
        Option option = new Option("h", "help", false, "Print help");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // When
        boolean result = commandLine.hasOption(option);

        // Then
        Assert.assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionTwice() {
        // Given
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // When
        commandLine.addOption(option);

        // Then
        // The test should fail with an IllegalArgumentException
    }

    @Test
    public void testGetOption() {
        // Given
        Option option = new Option("o", "option", true, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // When
        Option result = commandLine.getOption("o");

        // Then
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        // Given
        Option option = new Option("a", "argument", 1, "Argument description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        // When
        List values = commandLine.getValues(option, null);

        // Then
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Given
        Option option = new Option("a", "argument", 1, "Argument description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");

        // When
        List values = commandLine.getUndefaultedValues(option);

        // Then
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        // Given
        Option option = new Option("s", "switch", false, "Switch description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);

        // When
        Boolean switchValue = commandLine.getSwitch(option, false);

        // Then
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        // Given
        Option option = new Option("p", "property", true, "Property option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "value");

        // When
        String propertyValue = commandLine.getProperty(option, "defaultValue");

        // Then
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testToString() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("h", "help", false, "Print help"), Collections.singletonList("arg1 arg2"));

        // When
        String result = commandLine.toString();

        // Then
        Assert.assertEquals("arg1 arg2", result);
    }
}