import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").hasArgs().build());

        Parser parser = new DefaultParser();

        String[] arguments = {"-a", "valueA", "-b", "-c", "value1", "value2"};

        String[] flattenedArguments = parser.flatten(options, arguments, false);

        assertEquals(6, flattenedArguments.length);
        assertEquals("-a", flattenedArguments[0]);
        assertEquals("valueA", flattenedArguments[1]);
        assertEquals("-b", flattenedArguments[2]);
        assertEquals("-c", flattenedArguments[3]);
        assertEquals("value1", flattenedArguments[4]);
        assertEquals("value2", flattenedArguments[5]);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("alpha").hasArg().build());
        options.addOption(Option.builder("b").longOpt("beta").hasArg().build());
        options.addOption(Option.builder("c").longOpt("gamma").build());

        Parser parser = new DefaultParser();

        String[] arguments = {"-a", "valueA", "--beta", "valueB", "-c"};

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("beta"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        Parser parser = new DefaultParser();

        String[] arguments = {"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());

        Parser parser = new DefaultParser();

        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());

        Parser parser = new DefaultParser();

        String[] arguments = {"-a", "valueA", "nonOption", "-b", "--", "-c"};

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals("valueA", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));

        assertEquals(2, cmd.getArgList().size());
        assertEquals("nonOption", cmd.getArgList().get(0));
        assertEquals("-b", cmd.getArgList().get(1));
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg().build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").hasArgs().build());

        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.put("a", "valueA");
        properties.put("b", "true");
        properties.put("c", "value1,value2");

        CommandLine cmd = new CommandLine();

        parser.processProperties(properties, cmd, options);

        assertEquals("valueA", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value1,value2", cmd.getOptionValue("c"));
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());
        options.addOption(Option.builder("c").build());

        Parser parser = new DefaultParser();

        CommandLine cmd = new CommandLine();
        cmd.addOption(options.getOption("a"));
        cmd.addOption(options.getOption("c"));

        parser.checkRequiredOptions(cmd, options); // Should throw MissingOptionException
    }
}