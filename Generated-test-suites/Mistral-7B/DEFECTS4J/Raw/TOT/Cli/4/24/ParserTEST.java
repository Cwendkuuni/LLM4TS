import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimpleOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "-b", "--c=3"};
        Options options = new Options();
        options.addOption("a", true, "An optional option");
        options.addOption("b", true, "Another optional option");
        options.addOption("c", true, "An option with an argument");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
    }

    @Test
    public void testParseRequiredOptions() throws ParseException {
        String[] arguments = new String[]{"--input", "input.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.hasOption("i"));
        Assert.assertEquals("input.txt", cmd.getOptionValue("i"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--help"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.fail("Missing required option: i");
    }

    @Test
    public void testParseLongOptions() throws ParseException {
        String[] arguments = new String[]{"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "A long option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testParseOptionsWithArguments() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "-b", "2", "--c=3"};
        Options options = new Options();
        options.addOption("a", true, "An optional option");
        options.addOption("b", true, "Another optional option");
        options.addOption("c", true, "An option with an argument");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] arguments = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "An option with an argument");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.fail("Missing argument for option: a");
    }

    @Test
    public void testParseOptionsWithGroups() throws ParseException {
        String[] arguments = new String[]{"--group", "group1"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1");
        group1.setRequired(false);
        group1.addOption("g1", true, "Option in group1");
        options.addOptionGroup(group1);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.hasOption("g1"));
    }

    @Test
    public void testParseOptionsWithMixedGroupAndNonGroup() throws ParseException {
        String[] arguments = new String[]{"--group", "group1", "-a"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1");
        group1.setRequired(false);
        group1.addOption("g1", true, "Option in group1");
        options.addOptionGroup(group1);
        options.addOption("a", true, "Another optional option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertTrue(cmd.hasOption("g1"));
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"--unknown"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.fail("Unrecognized option: unknown");
    }
}