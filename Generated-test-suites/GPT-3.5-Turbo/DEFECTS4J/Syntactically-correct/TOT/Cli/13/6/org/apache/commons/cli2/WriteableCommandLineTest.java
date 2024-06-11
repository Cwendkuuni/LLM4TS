import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineTest {

    // Test addOption method

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("-v", "verbose", true);
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    // Test addValue method

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("-f", "file", true);
        commandLine.addValue(option, "test.txt");
        Assert.assertEquals("test.txt", commandLine.getValues(option).get(0));
    }

    // Test getValues method

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option1 = new OptionImpl("-d", "dir", true);
        Option option2 = new OptionImpl("-f", "file", true);
        commandLine.addValue(option1, "dir1");
        commandLine.addValue(option1, "dir2");
        commandLine.addValue(option2, "file1.txt");
        List<String> values1 = commandLine.getValues(option1);
        List<String> values2 = commandLine.getValues(option2);
        Assert.assertEquals(Arrays.asList("dir1", "dir2"), values1);
        Assert.assertEquals(Collections.singletonList("file1.txt"), values2);
    }

    // Test setDefaultValues method

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("-d", "dir", true);
        List<String> defaultValues = Arrays.asList("dir1", "dir2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    // Test addSwitch method

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("-v", "verbose", false);
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitchValue(option));
    }

    @Test (expected = IllegalStateException.class)
    public void testAddSwitch_multipleTimes() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("-v", "verbose", false);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    // Test setDefaultSwitch method

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Option option = new OptionImpl("-v", "verbose", false);
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getDefaultSwitch(option));
    }

    // Test addProperty method

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addProperty("name", "John");
        Assert.assertEquals("John", commandLine.getProperty("name"));
    }

    // Test looksLikeOption method

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        Assert.assertTrue(commandLine.looksLikeOption("-h"));
        Assert.assertFalse(commandLine.looksLikeOption("filename.txt"));
    }
}