import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());

        // When
        Option option = new Option("option2", "opt2", true);
        commandLine.addOption(option);

        // Then
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "opt1", true);

        // When
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        // Then
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "opt1", false);

        // When
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        // Then
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "opt1", false);

        // When
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);

        // Then
        // Exception should be thrown
    }

    // Add tests for getOption, getValues, getSwitch, getProperty, getProperties, looksLikeOption, and toString()

    @Test
    public void testAddProperty() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option1", "opt1", true);

        // When
        commandLine.addProperty(option, "property1");
        commandLine.addProperty(option, "property2", "value2");

        // Then
        Properties properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("property1", properties.getProperty("property1"));
        Assert.assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testToString() {
        // Given
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        commandLine.addOption(new Option("option2", "opt2", true));
        commandLine.addValue(commandLine.getOption("option2"), "value1");

        // When
        String cmdLineString = commandLine.toString();

        // Then
        String expected = "option2 opt2 value1";
        Assert.assertEquals(expected, cmdLineString);
    }
}