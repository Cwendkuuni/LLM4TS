import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParse_WithOptionsAndArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        String[] arguments = {"-a", "valueA", "--optionB"};
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(2, commandLine.getArgs().size());
        Assert.assertEquals("valueA", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));
    }

    @Test
    public void testParse_WithNamedOptionAndValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");

        String[] arguments = {};
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, properties);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Option optionA = commandLine.getOption(options.getOption("a"));
        Assert.assertEquals("valueA", optionA.getValue());
        Assert.assertNull(optionA.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingArgumentForOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"-a"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);

        // This line should throw MissingArgumentException
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"--unknownOption"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments);

        // This line should throw UnrecognizedOptionException
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {"--", "unknownArgument1", "--", "unknownArgument2"};
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, false);

        Assert.assertEquals(4, commandLine.getArgs().size());
        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertEquals("unknownArgument1", commandLine.getArgs().get(0));
        Assert.assertEquals("unknownArgument2", commandLine.getArgs().get(2));
    }

    @Test
    public void testParse_WithEmptyArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        String[] arguments = {};
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParse_WithOptionsAndEmptyArguments() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        String[] arguments = {};
        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertEquals(0, commandLine.getArgs().size());
        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("b"));
    }
}