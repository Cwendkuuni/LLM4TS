import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
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
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        Option option1 = new Option("o", "option1", true, "Option 1");
        requiredOptions.add(option1);
        options.addOption(option1);
        parser.setOptions(options);
        List<Option> actualOptions = parser.getRequiredOptions();
        Assert.assertEquals(requiredOptions, actualOptions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        Parser parser = new DefaultParser();
        parser.flatten(null, new String[0], false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        parser.flatten(options, null, false);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--", "-a", "-b", "-c", "--"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setRequired(true);
        options.addOption(option);
        String[] arguments = new String[0];
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithSingleValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);
        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithMultipleValueOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        option.setArgName("values");
        options.addOption(option);
        String[] arguments = {"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", args.get(0));
        Assert.assertEquals("value2", args.get(1));
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("long-option", "longOption", false, "Long Option");
        options.addOption(option);
        String[] arguments = {"--long-option"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testParseWithShortOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", false, "Option");
        options.addOption(option);
        String[] arguments = {"-o"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("option"));
    }

    @Test
    public void testParseWithShortOptionAndValue() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option");
        options.addOption(option);
        String[] arguments = {"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("option"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-x"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");
        parser.parse(options, null, properties);
        Option option1 = new Option("o", "option1", true, "Option 1");
        option1.setArgName("value");
        Option option2 = new Option("o", "option2", true, "Option 2");
        option2.setArgName("value");
        options.addOption(option1);
        options.addOption(option2);
        CommandLine cmd = parser.parse(options, new String[0]);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("value1", args.get(0));
        Assert.assertEquals("value2", args.get(1));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-o", "value", "--", "-n", "value2"};
        parser.parse(options, arguments, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.parse(options, null, (Properties) null);
    }
}