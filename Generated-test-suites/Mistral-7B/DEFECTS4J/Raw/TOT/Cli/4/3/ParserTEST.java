import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testSimpleParsing() throws ParseException {
        String[] args = new String[]{"-a", "1", "--foo", "bar"};
        Options options = new Options();
        Option optionA = new Option("a", true, "a", "An option with an arg");
        Option optionFoo = new Option("foo", true, null, "A foo option");
        options.addOption(optionA);
        options.addOption(optionFoo);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertFalse(cmd.hasOption("unknown"));
        assertNull(cmd.getOptionValue("unknown"));
        assertSame(args[0], cmd.getArgs()[0]);
        assertSame(args[3], cmd.getArgs()[3]);
    }

    @Test
    public void testMultipleRequiredOptions() throws ParseException {
        String[] args = new String[]{"--option1", "--option2"};
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1", "Option 1");
        Option option2 = new Option("option2", true, "o2", "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        options.addRequiredOption(option1);
        options.addRequiredOption(option2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("unknown"));
        assertEquals(2, cmd.getArgs().length);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        String[] args = new String[]{"--option1"};
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1", "Option 1");
        Option option2 = new Option("option2", true, "o2", "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        options.addRequiredOption(option1);
        options.addRequiredOption(option2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this should throw a MissingOptionException
    }

    @Test
    public void testParsingWithProperties() throws ParseException {
        String[] args = new String[]{"--file", "input.txt", "--delimiter", "|"};
        Properties properties = new Properties();
        properties.setProperty("file", "output.txt");

        Options options = new Options();
        Option fileOption = new Option("file", true, "f", "File to read");
        Option delimiterOption = new Option("delimiter", true, null, "Delimiter");
        options.addOption(fileOption);
        options.addOption(delimiterOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("input.txt", cmd.getOptionValue("file"));
        assertEquals("|", cmd.getOptionValue("delimiter"));
        assertEquals("output.txt", properties.getProperty("file"));
    }

    @Test
    public void testParsingWithOptionGroup() throws ParseException {
        String[] args = new String[]{"--group", "--foo"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group of options");
        Option fooOption = new Option("foo", true, null, "Foo option");
        group.addOption(fooOption);
        options.addOption(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("group"));
        assertTrue(cmd.hasOption("foo"));
    }
}