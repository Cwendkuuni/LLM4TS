import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.CommandLine;

public class ParserTest {
    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new DefaultParser();
        options = new Options();
        options.addOption(Option.builder("f").hasArg(true).argName("file").desc("Specify a file").build());
        options.addOption(Option.builder("d").desc("Enable debug mode").build());
        options.addOption(Option.builder("h").desc("Print help message").build());
    }

    @Test
    public void testParse_singleOption() throws ParseException {
        String[] args = {"-f", "test.txt"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertNull(cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue("h"));
    }

    @Test
    public void testParse_multipleOptions() throws ParseException {
        String[] args = {"-f", "test.txt", "-d", "-h"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertEquals("true", cmd.getOptionValue("d"));
        assertEquals("true", cmd.getOptionValue("h"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOption() throws ParseException {
        String[] args = {};
        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        String[] args = {"-f", "test.txt", "-u"};
        parser.parse(options, args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_missingArgument() throws ParseException {
        String[] args = {"-f"};
        parser.parse(options, args);
    }
}