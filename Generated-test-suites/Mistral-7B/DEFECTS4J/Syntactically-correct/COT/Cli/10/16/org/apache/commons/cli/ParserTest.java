import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new StubParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new StubParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionsNull() {
        Parser parser = new StubParser();
        parser.setOptions(null);
        parser.getOptions();
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new StubParser();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(new Options(), arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Parser parser = new StubParser();
        String[] arguments = {"--option1", "arg1", "--option2", "arg2"};
        CommandLine cmd = parser.parse(new Options(), arguments);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Parser parser = new StubParser();
        String[] arguments = {"--invalid"};
        parser.parse(new Options(), arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("option1", true, "o");
        Parser parser = new StubParser();
        parser.setOptions(options);
        String[] arguments = {};
        try {
            parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            throw new ParseException("Missing required option", e);
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new StubParser();
        Properties properties = new Properties();
        properties.put("option1", "value1");
        CommandLine cmd = parser.parse(new Options(), new String[0], properties);
        Assert.assertEquals(1, cmd.getOptions().size());
        Option option = cmd.getOptions().get(0);
        Assert.assertTrue(option.hasValue("value1"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new StubParser();
        String[] arguments = {"--option1", "-n", "arg1", "arg2"};
        CommandLine cmd = parser.parse(new Options(), arguments, true);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
        Assert.assertEquals("arg2", cmd.getArgs().get(2));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArgument() throws ParseException {
        Parser parser = new StubParser();
        String[] arguments = {"--option1", "arg1", "--option2", "arg1"};
        parser.parse(new Options(), arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Parser parser = new StubParser();
        String[] arguments = {"--invalid", "arg1"};
        parser.parse(new Options(), arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithDuplicateOption() throws ParseException {
        Parser parser = new StubParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        parser.setOptions(options);
        String[] arguments = {"--option1", "--option1"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithDuplicateOptionValue() throws ParseException {
        Parser parser = new StubParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        parser.setOptions(options);
        String[] arguments = {"--option1", "arg1", "--option1", "arg1"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1");
        group.setRequired(true);
        options.addOptionGroup(group);
        options.addOption("option1", true, "o");
        Parser parser = new StubParser();
        parser.setOptions(options);
        String[] arguments = {};
        try {
            parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            throw new ParseException("Missing required option group", e);
        }
    }

    private static class StubParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] args, 
                                   boolean stopAtNonOption) {
            return args;
        }
    }
}