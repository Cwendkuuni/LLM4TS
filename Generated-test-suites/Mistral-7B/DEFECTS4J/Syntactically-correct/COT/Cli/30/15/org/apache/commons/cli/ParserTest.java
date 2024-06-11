import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testSetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--option1", "value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(2, commandLine.getArgs().size());
        Assert.assertEquals("value1", commandLine.getOptionValue("o1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("o2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--invalidOption", "value"};
        Options options = new Options();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithMixedArguments() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--option1", "value1", "--option2", "value2", "--arg"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(2, commandLine.getArgs().size());
        Assert.assertEquals("value1", commandLine.getOptionValue("o1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("o2"));
        Assert.assertEquals("--arg", commandLine.getArgs().get(0));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        CommandLine commandLine = parser.parse(new Options(), arguments, properties, false);

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--unrecognizedOption"};
        Options options = new Options();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--option1"};
        Options options = new Options();
        options.addOption("option1", true, "o1").isRequired(true);
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingGroupOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--groupOption1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("groupOption1", true, "go1"));
        options.addOptionGroup(group);

        parser.parse(options, arguments, false);
    }
}