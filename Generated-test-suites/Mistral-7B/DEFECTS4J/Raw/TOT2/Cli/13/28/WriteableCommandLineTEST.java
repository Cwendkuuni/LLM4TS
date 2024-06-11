import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("test", new String[0]);
        Option option = new Option("o", "option", true, "An option");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptions().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionWithNullCommandLine() {
        Option option = new Option("o", "option", true, "An option");
        WriteableCommandLine commandLine = null;
        commandLine.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithExistingOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("test", new String[0]);
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option1);
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionWithNullOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("test", new String[0]);
        Option option = null;
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("test", new String[0]);
        Option option = new Option("o", "option", true, "An option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        Assert.assertEquals("value1", commandLine.getOptionValues(option).get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNullCommandLine() {
        Option option = new Option("o", "option", true, "An option");
        WriteableCommandLine commandLine = null;
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddValueWithNoOption() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("test", new String[0]);
        commandLine.addValue(new Option("o", "option", true, "An option"), 0);
    }

    @Test
    public void testGetOptionValues() {
        WriteableCommandLine commandLine = new CmdLineParser().parse("test --option value1 value2", new String[0]);
        Option option = commandLine.getOption("option", true, null);
        List<String> values = commandLine.getOptionValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testLooksLikeOption() {
        assertFalse(WriteableCommandLine.looksLikeOption(""));
        assertFalse(WriteableCommandLine.looksLikeOption("test"));
        assertFalse(WriteableCommandLine.looksLikeOption("--option"));
        assertTrue(WriteableCommandLine.looksLikeOption("--optionvalue"));
        assertTrue(WriteableCommandLine.looksLikeOption("--option value"));
        assertTrue(WriteableCommandLine.looksLikeOption("--option value1 value2"));
    }
}