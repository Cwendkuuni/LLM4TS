import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsAndNoArguments() throws ParseException {
        String[] args = new String[0];
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertFalse(cmd.hasOption("option1"));
        assertFalse(cmd.hasOption("option2"));
    }

    @Test
    public void testParseWithOptionsAndNoArguments() throws ParseException {
        String[] args = new String[]{"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertNotNull(cmd.getOptionValues("option1"));
        assertEquals(true, cmd.hasOption("option1"));
        assertEquals(true, cmd.hasOption("option2"));
        assertEquals(null, cmd.getOptionValues("unknownOption"));
        assertFalse(cmd.getOptionValues("noOption").hasRemaining());
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        String[] args = new String[]{"--option1", "value1", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("value1", cmd.getArgs().get(0));
        assertNotNull(cmd.getOptionValues("option1"));
        assertEquals(true, cmd.hasOption("option1"));
        assertEquals("value2", cmd.getOptionValues("option2")[0]);
        assertEquals(true, cmd.hasOption("option2"));
        assertNull(cmd.getOptionValues("unknownOption"));
        assertFalse(cmd.getOptionValues("noOption").hasRemaining());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingOptionArgument() throws ParseException {
        String[] args = new String[]{"--option1"};
        Options options = new Options();
        options.addOption("option1", true, "o1").requiredArg();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws ParseException {
        String[] args = new String[]{"--unknownOption"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknownOption"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionFormat() throws ParseException {
        String[] args = new String[]{"--option1=value1", "--option2=value2--option3"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }
}