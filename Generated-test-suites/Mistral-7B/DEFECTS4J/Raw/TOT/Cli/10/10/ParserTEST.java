import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseSimple() throws Exception {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-i", "input.txt"});

        assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-i"});
        fail("Missing argument exception expected");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws Exception {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-i", "input.txt"});
        fail("Missing option exception expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"--unknown"});
        fail("Unrecognized option exception expected");
    }

    @Test
    public void testParseWithOptionalArgument() throws Exception {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-o", "output.txt"});
        assertEquals("output.txt", cmd.getOptionValue("output"));

        cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-o"});
        assertNull(cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithRequiredAndOptionalOptions() throws Exception {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-i", "input.txt", "-o", "output.txt"});

        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws Exception {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-i", "input.txt", "-o", "--unknown"});
        fail("Parse exception expected");
    }

    @Test
    public void testParseWithHelpOption() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "print this help message");
        options.addOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-h"});
        assertTrue(cmd.hasOption("help"));
    }
}