import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseBasic() throws ParseException {
        Options options = new Options();
        String[] args = {"--option1", "--option2"};
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args);
        assertEquals(2, cmd.getArgs().size());
        assertEquals("option1", cmd.getOptionValues("option1")[0]);
        assertEquals("option2", cmd.getOptionValues("option2")[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--unknown"};
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args);
        fail("Should have thrown ParseException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("option1", true, "o");
        String[] args = {};
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
        } catch (ParseException e) {
            throw new MissingOptionException("Missing required option: option1", e);
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o");
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0], properties, false);
        assertEquals(0, cmd.getArgs().size());
        assertEquals("value1", cmd.getOptionValues("option1")[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidBooleanValue() throws ParseException {
        Options options = new Options();
        options.addOption("option1", false, "o");
        Properties properties = new Properties();
        properties.setProperty("option1", "false");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, new String[0], properties, false);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid boolean value 'false' for option 'option1'");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithDuplicateOption() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option1", true, "o");
        String[] args = {};
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
        } catch (ParseException e) {
            throw new IllegalStateException("Duplicate option: option1");
        }
    }
}