import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertSame(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", "argA", true, "Argument A"));
        options.addOptionGroup(new OptionGroup("group1", "Group 1", requiredOptions.toArray(new Option[0])));
        parser.setOptions(options);

        List<Option> actual = parser.getRequiredOptions();
        Assert.assertArrayEquals(requiredOptions.toArray(new Option[0]), actual.toArray(new Option[0]));
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd.getArgs().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testParseWithArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        String[] arguments = {"arg1", "arg2"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        String[] arguments = {"--invalidOption", "arg1"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithEmptyProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        parser.setOptions(options);
        parser.parse(options, new String[0], properties);
    }

    @Test(expected = ParseException.class)
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.put("arg1", "value1");
        parser.setOptions(options);
        String[] arguments = new String[0];
        parser.parse(options, arguments, properties);
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        String[] arguments = {"--", "value1", "--optionA", "value2"};
        parser.parse(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testParseWithStopAtNonOptionAndProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.put("arg1", "value1");
        parser.setOptions(options);
        String[] arguments = {"--", "value1", "--optionA", "value2"};
        parser.parse(options, arguments, true, properties);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissing() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", "argA", true, "Argument A"));
        options.addOptionGroup(new OptionGroup("group1", "Group 1", requiredOptions.toArray(new Option[0])));
        parser.setOptions(options);
        String[] arguments = new String[0];
        parser.parse(options, arguments);
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        parser.setOptions(options);
        parser.processProperties(properties);
        Option optionA = options.getOption("optionA");
        Assert.assertNotNull(optionA);
        Assert.assertEquals("valueA", optionA.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissing() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", "argA", true, "Argument A"));
        options.addOptionGroup(new OptionGroup("group1", "Group 1", requiredOptions.toArray(new Option[0])));
        parser.setOptions(options);
        String[] arguments = {"--optionA"};
        List<String> tokenList = Arrays.asList(parser.flatten(options, arguments, false));
        parser.parse(options, arguments.clone(), null, true);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", "argA", true, "Argument A"));
        options.addOptionGroup(new OptionGroup("group1", "Group 1", requiredOptions.toArray(new Option[0])));
        parser.setOptions(options);
        String[] arguments = {"--optionA", "valueA"};
        List<String> tokenList = Arrays.asList(parser.flatten(options, arguments, false));
        parser.parse(options, arguments.clone(), null, true);
        Option optionA = options.getOption("optionA");
        Assert.assertNotNull(optionA);
        Assert.assertEquals("valueA", optionA.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", "argA", true, "Argument A"));
        options.addOptionGroup(new OptionGroup("group1", "Group 1", requiredOptions.toArray(new Option[0])));
        parser.setOptions(options);
        String[] arguments = {"--unknownOption"};
        List<String> tokenList = Arrays.asList(parser.flatten(options, arguments, false));
        parser.parse(options, arguments.clone(), null, true);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidTokenList() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        List<Option> requiredOptions = new ArrayList<>();
        requiredOptions.add(new Option("a", "argA", true, "Argument A"));
        options.addOptionGroup(new OptionGroup("group1", "Group 1", requiredOptions.toArray(new Option[0])));
        parser.setOptions(options);
        String[] arguments = {"--optionA", "valueA", "--", "valueB"};
        List<String> tokenList = Arrays.asList(parser.flatten(options, arguments, false));
        parser.parse(options, arguments.clone(), null, true);
    }
}