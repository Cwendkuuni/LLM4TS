import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", false, "option B");

        String[] arguments = {"-a", "argumentA", "-b", "argumentB"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("argumentA", cmd.getOptionValue("a"));
        assertEquals("argumentB", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws Exception {
        Options options = new Options();
        options.addOption("a", "option A");

        String[] arguments = {"-a"};

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());

        String[] arguments = {"-a", "argumentA", "-b", "argumentB"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("argumentA", cmd.getOptionValue("a"));
        assertEquals("argumentB", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());

        String[] arguments = {"-a", "argumentA"};

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", "option B");

        String[] arguments = {"-a", "argumentA", "nonOption", "-b"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("argumentA", cmd.getOptionValue("a"));
        assertEquals(2, cmd.getArgList().size());
        assertEquals("nonOption", cmd.getArgList().get(0));
        assertEquals("-b", cmd.getArgList().get(1));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("a", "option A");
        options.addOption("b", "option B");

        Properties properties = new Properties();
        properties.setProperty("a", "argumentA");
        properties.setProperty("b", "argumentB");

        String[] arguments = {};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("argumentA", cmd.getOptionValue("a"));
        assertEquals("argumentB", cmd.getOptionValue("b"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("a", "option A");

        String[] arguments = {"-a", "-b"};

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithLongOptionWithArgument() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("optionA").hasArg().build());

        String[] arguments = {"-a", "argumentA"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("argumentA", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithMultipleArgumentsForOption() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());

        String[] arguments = {"-a", "argumentA", "argumentB"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("argumentA", cmd.getOptionValues("a")[0]);
        assertEquals("argumentB", cmd.getOptionValues("a")[1]);
    }
}