import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithValidArguments() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        Assert.assertTrue(parser.parse(options, arguments, true) instanceof MissingOptionException);
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException {
        String[] arguments = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        Assert.assertTrue(parser.parse(options, arguments, true) instanceof MissingArgumentException);
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"--unknownOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        Assert.assertTrue(parser.parse(options, arguments, true) instanceof UnrecognizedOptionException);
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        String[] arguments = new String[]{"--optionA", "arg1", "-a", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals("arg1", cmd.getArgList().get(0));
        Assert.assertEquals("arg2", cmd.getArgList().get(1));
        Assert.assertEquals("arg2", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] arguments = new String[]{"--long-option-a"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals("true", cmd.getOptionValue("longOptionA"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[]{"--propFile", "test.properties"};
        Properties props = new Properties();
        props.setProperty("optionA", "valueA");
        props.setProperty("optionB.value", "valueB");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("propFile", false, "Property file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, props, true);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));

        Option option = options.getOption("propFile");
        Assert.assertEquals("test.properties", option.getValue());
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        String[] arguments = new String[]{"--group", "--group.optionA=valueA"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "The group", 1, 'g');
        group.setRequired(true);

        group.addOption(new Option("optionA", true, "Option A"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);
        Option option = cmd.getOption(group);

        Assert.assertEquals("valueA", option.getValue());
    }
}