import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test 1: Adding a single option and checking if it exists
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("-a"), null);
        Option option = new Option("-b");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        // Test 2: Adding a value for an option and checking if it exists
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("-a"), null);
        Option option = new Option("-b");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");

        Assert.assertEquals("testValue", commandLine.getValues(option, null).get(0));
    }


    @Test
    public void testAddSwitch() {
        // Test 3: Adding a switch for an option and checking if it exists
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("-a"), null);
        Option option = new Option("-b");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertEquals(true, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        // Test 4: Adding a property for an option and checking if it exists
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("-a"), null);
        Option option = new Option("-b");
        commandLine.addOption(option);

        commandLine.addProperty(option, "prop1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty(option, "prop1", null));
    }

    @Test
    public void testLooksLikeOption() {
        // Test 5: Checking if a trigger looks like an option
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("-a"), null);
        Assert.assertTrue(commandLine.looksLikeOption("-a"));
    }

}