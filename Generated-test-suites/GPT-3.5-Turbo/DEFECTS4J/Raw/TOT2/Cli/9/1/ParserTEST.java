import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseOptionsOnly() throws Exception {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        String[] args = {"-f", "test.txt"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        String[] args = {};
        Properties props = new Properties();
        props.setProperty("f", "test.txt");
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props);
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("f").required(true).build());
        String[] args = {};
        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testStopAtNonOption() throws Exception {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");
        String[] args = {"-f", "test.txt", "file2.txt", "--", "extra.txt"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
        assertTrue(cmd.getArgs().contains("file2.txt"));
        assertFalse(cmd.getArgs().contains("--"));
        assertFalse(cmd.getArgs().contains("extra.txt"));
    }

    @Test(expected = ParseException.class)
    public void testUnrecognizedOption() throws Exception {
        Options options = new Options();
        String[] args = {"-u"};
        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

}