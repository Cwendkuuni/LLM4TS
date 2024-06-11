import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addOption("h", "help", false, "print this message");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option", "value"};
        CommandLine cmd = parser.parse(options, args);

        assertEquals("value", cmd.getOptionValue("option"));
        assertFalse(cmd.hasOption("help"));
        assertTrue(cmd.hasOption("option"));
        assertEquals(1, cmd.getArgs().length);
        assertEquals("value", cmd.getArgs()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd.getOptionValue("option"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "an option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option"};
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd.getOptionValue("option"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "an option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--unknown"};
        CommandLine cmd = parser.parse(options, args);

        assertFalse(cmd.hasOption("unknown"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption("o", "option", true, "an option");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option=value--"};
        CommandLine cmd = parser.parse(options, args);

        assertFalse(cmd.hasOption("option"));
    }
}