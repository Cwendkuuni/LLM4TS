import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParseWithOptionsAndArguments() {
        // Test parsing with options and arguments
        String[] args = {"-a", "valueA", "-b", "valueB", "arg1", "arg2"};
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        Option optionB = new Option("b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue("Option A is present", cmd.hasOption("a"));
            assertTrue("Option B is present", cmd.hasOption("b"));
            assertEquals("Value of Option A is correct", "valueA", cmd.getOptionValue("a"));
            assertEquals("Value of Option B is correct", "valueB", cmd.getOptionValue("b"));

            String[] remainingArgs = cmd.getArgs();
            assertEquals("Number of remaining arguments is correct", 2, remainingArgs.length);
            assertEquals("First remaining argument is correct", "arg1", remainingArgs[0]);
            assertEquals("Second remaining argument is correct", "arg2", remainingArgs[1]);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testParseWithMissingArguments() {
        // Test parsing with missing arguments for required options
        String[] args = {"-a"};
        Options options = new Options();
        Option optionA = Option.builder("a").required().hasArgs().build();
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            fail("MissingArgumentException not thrown");
        } catch (MissingArgumentException e) {
            assertTrue(true);
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        // Test parsing with unrecognized option
        String[] args = {"-c"};
        Options options = new Options();
        Option optionA = new Option("a", true, "Option A");
        options.addOption(optionA);

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            fail("UnrecognizedOptionException not thrown");
        } catch (UnrecognizedOptionException e) {
            assertTrue(true);
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }
}