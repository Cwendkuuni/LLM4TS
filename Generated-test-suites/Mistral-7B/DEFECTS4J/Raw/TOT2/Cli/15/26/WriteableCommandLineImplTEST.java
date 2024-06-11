import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        // Given
        Option option = new Option("o", "option", true, "An option");

        // When
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // Then
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        // Given
        Option option = new Option("a", "argument", 1, "An argument");

        // When
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");

        // Then
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // Given
        Option option = new Option("s", "switch", false, "A switch");

        // When
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);

        // Then
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        // Given
        Option option = new Option("s", "switch", false, "A switch");

        // When
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);

        // Then
        // This test should throw an IllegalStateException
    }

    @Test
    public void testHasOption() {
        // Given
        Option option = new Option("o", "option", true, "An option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // When
        boolean hasOption = commandLine.hasOption(option);

        // Then
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        // Given
        Option option = new Option("o", "option", true, "An option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        // When
        Option gotOption = commandLine.getOption(option.getPreferredName());

        // Then
        Assert.assertEquals(option, gotOption);
    }

    @Test
    public void testGetValues() {
        // Given
        Option option = new Option("a", "argument", 1, "An argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        // When
        List values = commandLine.getValues(option, null);

        // Then
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("arg1", values.get(0));
        Assert.assertEquals("arg2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        // Given
        Option option = new Option("a", "argument", 1, "An argument");
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
        Option option = new Option("s", "switch", false, "A switch");
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
        Option option = new Option("p", "property", true, "A property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "value1");

        // When
        String propertyValue = commandLine.getProperty(option, "defaultValue");

        // Then
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        // Given
        Option option = new Option("p", "property", true, "A property");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // When
        commandLine.addProperty(option, "property", "value1");

        // Then
        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.get("property"));
    }

    // Add tests for other methods and edge cases as necessary
}