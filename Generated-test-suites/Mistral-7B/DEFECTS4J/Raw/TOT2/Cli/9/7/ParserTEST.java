import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithValidOptions() throws ParseException {
        String[] arguments = {"-a", "1", "--optionB", "2", "--optionC", "3"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        parser.parse(options, arguments);

        CommandLine cmd = parser.getParsedCommandLine();

        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getArgList().size());
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = {"-a", "1", "--optionB", "2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredOptionB");

        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: requiredOptionB", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = {"--invalidOption", "1", "--optionB", "2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: invalidOption", e.getMessage());
        }
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] arguments = {"--longOptionA", "1", "--long-option-B", "2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        parser.parse(options, arguments);

        CommandLine cmd = parser.getParsedCommandLine();

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("longOptionA"));
        Assert.assertEquals("1", cmd.getOptionValue("longOptionA"));
        Assert.assertTrue(cmd.hasOption("longOptionB"));
        Assert.assertEquals("2", cmd.getOptionValue("longOptionB"));
        Assert.assertNull(cmd.getArgList().size());
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        String[] arguments = {"-a", "1", "--optionB", "2", "3"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true);

        parser.parse(options, arguments);

        CommandLine cmd = parser.getParsedCommandLine();

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertArrayEquals(new String[]{"2", "3"}, cmd.getOptionValues("b"));
        Assert.assertNull(cmd.getArgList().size());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = {"--property1=value1", "--property2=value2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("p", true, "property", true, "Property");

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        parser.parse(options, arguments, properties);

        CommandLine cmd = parser.getParsedCommandLine();

        Assert.assertEquals(0, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasProperty("property1"));
        Assert.assertEquals("value1", cmd.getProperty("property1"));
        Assert.assertTrue(cmd.hasProperty("property2"));
        Assert.assertEquals("value2", cmd.getProperty("property2"));
        Assert.assertNull(cmd.getArgList().size());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = {"--optionA", "-n", "arg"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("n", true, "Non-option N");

        parser.parse(options, arguments, true);

        CommandLine cmd = parser.getParsedCommandLine();

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("n"));
        Assert.assertEquals("arg", cmd.getOptionValue("n"));
        Assert.assertEquals(1, cmd.getArgList().size());
        Assert.assertEquals("arg", cmd.getArgList().get(0));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] arguments = {"--group", "--optionA", "1", "--optionB", "2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);

        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));

        options.addOptionGroup(group);

        parser.parse(options, arguments);

        CommandLine cmd = parser.getParsedCommandLine();

        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().get(0) instanceof OptionGroup);
        Assert.assertTrue(((OptionGroup) cmd.getOptions().get(0)).isSelected());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getArgList().size());
    }
}