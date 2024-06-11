import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
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

        Assert.assertNotNull(parser.getOptions());
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);

        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("a", true, true, "option A"));
        options.addOptions(requiredOptions);

        parser.setOptions(options);

        List actual = parser.getRequiredOptions();

        Assert.assertEquals(requiredOptions, actual);
    }

    @Test
    public void testFlattenNullArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = null;

        String[] actual = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[0], actual);
    }

    @Test
    public void testParseEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = new String[0];

        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertNotNull(commandLine);
        Assert.assertEquals(0, commandLine.getArgs().size());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("a", true, true, "option A"));
        options.addOptions(requiredOptions);

        String[] arguments = {"--optionA"};

        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertFalse(commandLine.hasOption("optionA"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("a", true, true, "option A"));
        options.addOptions(requiredOptions);

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithOptionalOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("a", true, false, "option A"));
        options.addOptions(requiredOptions);

        String[] arguments = {"--optionA", "value"};

        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertNotNull(commandLine);
        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("optionA"));
        Assert.assertEquals("value", commandLine.getStringOptionValue("optionA"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("a", true, false, "option A"));
        options.addOptions(requiredOptions);

        String[] arguments = {"--optionA"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List requiredOptions = Arrays.asList(new Option("a", true, false, "option A"));
        options.addOptions(requiredOptions);

        String[] arguments = {"--optionA", "arg1", "arg2"};

        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertNotNull(commandLine);
        Assert.assertEquals(2, commandLine.getArgs().size());
        Assert.assertTrue(commandLine.hasOption("optionA"));
        Assert.assertEquals("arg1", commandLine.getStringOptionValue("optionA"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = {"--unrecognized"};

        parser.parse(options, arguments);
    }
}