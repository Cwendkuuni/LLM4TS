import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    /**
     * Test parsing of a simple command line with no options or arguments.
     */
    @Test
    public void testEmptyCommandLine() throws ParseException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    /**
     * Test parsing of a command line with a single option.
     */
    @Test
    public void testSingleOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Specify output file");
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--output", "test.txt"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("unknown"));
    }

    /**
     * Test parsing of a command line with multiple options.
     */
    @Test
    public void testMultipleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "Specify output file");
        Option option2 = new Option("i", "input", true, "Specify input file");
        options.addOption(option1);
        options.addOption(option2);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--output", "test.txt", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertNull(cmd.getOptionValue("unknown"));
    }

    /**
     * Test parsing of a command line with an optional argument.
     */
    @Test
    public void testOptionalArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Specify output file");
        option.setRequired(false);
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--output", "test.txt"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("output2"));
    }

    /**
     * Test parsing of a command line with a required argument.
     */
    @Test(expected = ParseException.class)
    public void testMissingRequiredArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Specify output file");
        option.setRequired(true);
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {};
        parser.parse(options, arguments, true);
    }

    /**
     * Test parsing of a command line with a value-separated argument.
     */
    @Test
    public void testValueSeparatedArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("l", "logs", true, "Log files to display");
        option.setValueSeparator(';');
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--logs", "log1.txt;log2.txt;log3.txt"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertArrayEquals(new String[]{"log1.txt", "log2.txt", "log3.txt"},
            cmd.getOptionValues("logs"));
    }

    /**
     * Test parsing of a command line with a group of options.
     */
    @Test
    public void testOptionGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--g", "--option1", "--option2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("Option 1", cmd.getOptionValue("o1"));
        Assert.assertEquals("Option 2", cmd.getOptionValue("o2"));
    }

    /**
     * Test parsing of a command line with conflicting options.
     */
    @Test(expected = ParseException.class)
    public void testConflictingOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--o1", "value1", "--o2", "value2"};
        parser.parse(options, arguments, true);
    }

    /**
     * Test parsing of a command line with an invalid option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException, UnrecognizedOptionException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {"--invalid"};
        parser.parse(options, arguments, true);
    }

    /**
     * Test parsing of a command line with a missing required option.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException, MissingOptionException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Specify output file");
        option.setRequired(true);
        options.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] arguments = {};
        parser.parse(options, arguments, true);
    }
}