package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-f", "file.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");

        String[] arguments = {"-f", "file2.txt"};
        CommandLine cmd = parser.parse(options, arguments, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = {"-f", "file.txt", "argument1", "argument2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("argument1", cmd.getArgs()[0]);
        assertEquals("argument2", cmd.getArgs()[1]);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = new Option("f", true, "File");
        ListIterator<String> iter = Arrays.asList("file.txt").listIterator();

        parser.processArgs(opt, iter);
        assertTrue(opt.hasArgs());
        assertEquals(1, opt.getValues().length);
        assertEquals("file.txt", opt.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option opt = new Option("f", true, "File");
        ListIterator<String> iter = Arrays.asList().listIterator();

        parser.processArgs(opt, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        options.addOption("f", true, "File");

        String arg = "-f";
        ListIterator<String> iter = Arrays.asList("file.txt").listIterator();

        parser.processOption(arg, iter);

        assertTrue(parser.getOptions().hasOption("f"));
        Option opt = parser.getOptions().getOption("f");
        assertTrue(opt.hasArg());
        assertEquals("file.txt", opt.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        String arg = "-z";
        ListIterator<String> iter = Arrays.asList().listIterator();

        parser.processOption(arg, iter);
    }

    @Test
    public void testProcessProperties() throws ParseException {
        options.addOption("f", true, "File");

        Properties properties = new Properties();
        properties.setProperty("f", "file.txt");

        CommandLine cmd = parser.parse(options, null, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("file.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Option requiredOption = new Option("r", true, "Required");
        requiredOption.setRequired(true);

        options.addOption(requiredOption);

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOption() throws MissingOptionException {
        Option requiredOption = new Option("r", true, "Required");
        requiredOption.setRequired(true);

        options.addOption(requiredOption);

        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

}