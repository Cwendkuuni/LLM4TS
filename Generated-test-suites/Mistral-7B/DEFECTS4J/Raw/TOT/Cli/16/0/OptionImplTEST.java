import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import static org.apache.commons.cli2.OptionBuilder.*;
import static org.apache.commons.cli2.OptionType.*;

public class OptionImplTest {

    private CommandLineParser parser;
    private HelpFormatter formatter;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        formatter = new HelpFormatter();
        commandLine = new WriteableCommandLine("TestApp");
    }

    @Test
    public void testOptionWithNoPrefix() {
        Option option = option("optionA", "a", false, "A description");
        parser.parse(new String[]{"-a"}, commandLine);
        Assert.assertFalse("Option A should not be required", option.isRequired());
        Assert.assertEquals("Option A was not parsed correctly", "optionA", option.getLongOpt());
        Assert.assertEquals("Option A was not parsed correctly", 'a', option.getOpt());
        Assert.assertEquals("Option A was not parsed correctly", "A description", option.getDescription());
    }

    @Test
    public void testOptionWithPrefix() {
        Option option = option("optionB", "b", false, "A description")
                .withLongOpt("longOptionB")
                .withArgName("arg");
        parser.parse(new String[]{"--longOptionB=arg"}, commandLine);
        Assert.assertFalse("Option B should not be required", option.isRequired());
        Assert.assertEquals("Option B was not parsed correctly", "optionB", option.getLongOpt());
        Assert.assertEquals("Option B was not parsed correctly", 'b', option.getOpt());
        Assert.assertEquals("Option B was not parsed correctly", "arg", commandLine.getOptionValues(option.getLongOpt()));
        Assert.assertEquals("Option B was not parsed correctly", "A description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidPrefix() {
        Option option = option("optionC", "c", false, "A description")
                .withLongOpt("longOptionC")
                .withArgName("arg");
        parser.parse(new String[]{"--longOptionD=arg"}, commandLine);
        formatter.printHelp("TestApp", parser.getOptions());
    }

    @Test
    public void testOptionsParsing() {
        Option option1 = option("option1", "o1", false, "Option 1 description");
        Option option2 = option("option2", "o2", true, "Option 2 description");

        parser.parse(new String[]{"-o1", "-o2"}, commandLine);
        Assert.assertFalse("Option 1 should not be required", option1.isRequired());
        Assert.assertEquals("Option 1 was not parsed correctly", "option1", option1.getLongOpt());
        Assert.assertEquals("Option 1 was not parsed correctly", 'o1', option1.getOpt());
        Assert.assertEquals("Option 1 was not parsed correctly", "Option 1 description", option1.getDescription());
        Assert.assertNull("Option 1 did not have an argument", commandLine.getOptionValues(option1.getLongOpt()));

        Assert.assertTrue("Option 2 should be required", option2.isRequired());
        Assert.assertEquals("Option 2 was not parsed correctly", "option2", option2.getLongOpt());
        Assert.assertEquals("Option 2 was not parsed correctly", 'o2', option2.getOpt());
        Assert.assertEquals("Option 2 was not parsed correctly", "Option 2 description", option2.getDescription());
        Assert.assertNull("Option 2 did not have an argument", commandLine.getOptionValues(option2.getLongOpt()));

        Assert.assertArrayEquals("Command line arguments were not parsed correctly", new String[]{"o1"}, commandLine.getArguments());
    }
}