import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("f", "file", true, "input file");

        Parser parser = new DefaultParser();
        String[] args = {"-f", "input.txt"};
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            fail("ParseException should not be thrown for valid options and arguments");
        }

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());

        Parser parser = new DefaultParser();
        String[] args = {};
        try {
            parser.parse(options, args);
            fail("MissingOptionException should be thrown for missing required options");
        } catch (MissingOptionException e) {
            // expected
        } catch (ParseException e) {
            fail("Unexpected ParseException for missing required options");
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();

        Parser parser = new DefaultParser();
        String[] args = {"-u"};
        try {
            parser.parse(options, args);
        } catch (UnrecognizedOptionException e) {
            // expected
            assertEquals("Unrecognized option: -u", e.getMessage());
            assertEquals("-u", e.getOption());
        } catch (ParseException e) {
            fail("UnrecognizedOptionException should be thrown for unrecognized options");
        }
    }
}