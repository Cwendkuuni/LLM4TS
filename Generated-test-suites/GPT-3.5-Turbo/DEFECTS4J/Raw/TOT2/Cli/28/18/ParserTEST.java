import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A description");

        Parser parser = new DefaultParser();
        try {
            String[] args = {"-a", "arg1"};
            CommandLine cmd = parser.parse(options, args);

            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertEquals("arg1", cmd.getOptionValue("a"));
        } catch (ParseException e) {
            Assert.fail("ParseException thrown unexpectedly");
        }
    }

    @Test
    public void testParseWithRequiredOptions() {
        Options options = new Options();
        Option optionB = new Option("b", "optionB", true, "Option B description");
        optionB.setRequired(true);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        try {
            String[] args = {};
            parser.parse(options, args);

            Assert.fail("ParseException expected for missing required option");
        } catch (MissingOptionException e) {
            Assert.assertTrue(e.getMissingOptions().contains("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = {"-c"};

        try {
            parser.parse(options, args);
            Assert.fail("UnrecognizedOptionException expected for unrecognized option");
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: -c", e.getMessage());
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException thrown");
        }
    }

    // Additional test cases for edge cases and exceptions can be added here

}