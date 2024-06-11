import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"--input", "file.txt", "--output", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("input"));
        assertTrue(cmd.hasOption("output"));
        assertEquals("file.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        options.addOption(Option.builder("i").required().hasArg().build());
        String[] args = {};

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        options.addOption(Option.builder("i").required().hasArg().build());
        options.addOption(Option.builder("o").required().hasArg().build());
        Properties props = new Properties();
        props.setProperty("i", "file.txt");
        props.setProperty("o", "output.txt");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, props);

        assertTrue(cmd.hasOption("i"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("file.txt", cmd.getOptionValue("i"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption(Option.builder("i").hasArg().build());
        options.addOption(Option.builder("o").hasArg().build());
        String[] args = {"--input", "file.txt", "nonOption", "--output", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("input"));
        assertEquals("file.txt", cmd.getOptionValue("input"));
        assertFalse(cmd.hasOption("o"));
        assertNotNull(cmd.getArgs());
        assertEquals(2, cmd.getArgs().length);
        assertEquals("nonOption", cmd.getArgs()[0]);
        assertEquals("--output", cmd.getArgs()[1]);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = Option.builder("a").hasArgs().build();
        ListIterator<String> iterator = Arrays.asList("1", "2", "3").listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(opt, iterator);

        assertTrue(opt.hasArgs());
        assertNotNull(opt.getValues());
        assertEquals(3, opt.getValues().length);
        assertEquals("1", opt.getValues()[0]);
        assertEquals("2", opt.getValues()[1]);
        assertEquals("3", opt.getValues()[2]);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option opt = Option.builder("a").hasArg().build();
        ListIterator<String> iterator = Collections.singletonList("value").listIterator();

        Parser parser = new ParserImpl();
        parser.processArgs(opt, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        options.addOption(Option.builder("i").hasArg().build());
        ListIterator<String> iterator = Collections.singletonList("-i").listIterator();

        Parser parser = new ParserImpl();
        CommandLine cmd = new CommandLine();
        parser.processOption("-i", iterator);

        assertTrue(cmd.hasOption("i"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        ListIterator<String> iterator = Collections.singletonList("-u").listIterator();

        Parser parser = new ParserImpl();
        parser.processOption("-u", iterator);
    }

    @Test
    public void testSetOptions() {
        Options options = new Options();
        options.addOption(Option.builder("i").hasArg().build());

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        assertNotNull(parser.getOptions());
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = Option.builder("i").required().hasArg().build();
        options.addOption(option);

        Parser parser = new ParserImpl();
        parser.setOptions(options);

        List requiredOptions = parser.getRequiredOptions();
        assertNotNull(requiredOptions);
        assertEquals(1, requiredOptions.size());
        assertEquals(option.getKey(), requiredOptions.get(0));
    }

}