import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParsingException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(parser.options);
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "arg1", true, "Argument 1");
        options.addOption("b", "arg2", false, "Argument 2");
        String[] arguments = {"-a", "value1", "-b"};

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getArgs()[0]);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Argument 1");
        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-x"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Argument 1");
        options.addRequiredOption("b", false, "Optional Required");
        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", false, "Required Option 1");
        options.addRequiredOption("b", false, "Required Option 2");
        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Argument 1");
        options.addOption("b", true, "Argument 2");
        String[] arguments = {"-a", "arg1", "-b", "arg3"};

        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionArgument2() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Argument 1");
        options.addOption("b", true, "Argument 2");
        String[] arguments = {"-a", "arg1", "-b", "arg1,arg2"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.put("arg1", "value1");
        properties.put("arg2", "value2");

        options.addOption("a", true, "Argument 1");
        options.addOption("b", true, "Argument 2");

        CommandLine cmd = parser.parse(options, null, properties);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getArgs()[0]);
        Assert.assertEquals("value2", cmd.getArgs()[1]);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Argument 1");
        options.addOption("b", true, "Argument 2");
        String[] arguments = {"-a", "arg1", "-b", "arg2", "arg3"};

        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg3", cmd.getArgs()[2]);
    }
}