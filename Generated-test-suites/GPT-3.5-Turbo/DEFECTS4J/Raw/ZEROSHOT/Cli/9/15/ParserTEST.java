import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        options = new Options();
        options.addOption("h", "help", false, "display help");
        options.addOption("v", "version", false, "display version");
        options.addOption("f", "file", true, "input file");
    }

    @Test
    public void testParseWithoutOptions() throws ParseException {
        String[] args = new String[]{"input.txt"};

        CommandLine cmd = parser.parse(options, args);

        String[] expectedArgs = new String[]{"input.txt"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertFalse(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithHelpOption() throws ParseException {
        String[] args = new String[]{"-h"};

        CommandLine cmd = parser.parse(options, args);

        String[] expectedArgs = new String[]{};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithVersionOption() throws ParseException {
        String[] args = new String[]{"-v"};

        CommandLine cmd = parser.parse(options, args);

        String[] expectedArgs = new String[]{};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertFalse(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithFileOption() throws ParseException {
        String[] args = new String[]{"-f", "input.txt"};

        CommandLine cmd = parser.parse(options, args);

        String[] expectedArgs = new String[]{};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertFalse(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithoutFileOptionValue() throws ParseException {
        String[] args = new String[]{"-f"};

        parser.parse(options, args);
    }

}