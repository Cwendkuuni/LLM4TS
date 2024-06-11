// Import necessary libraries
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithNoOptions() {
        // Create a Parser instance
        Parser parser = new Parser();

        // Define the command line arguments
        String[] args = new String[]{"arg1", "arg2"};

        // Attempt to parse the arguments
        try {
            CommandLine cmd = parser.parse(new Options(), args);
            // Check if the command line object is not null
            assertNotNull(cmd);
            // Check if the command line arguments are correctly added
            assertArrayEquals(args, cmd.getArgs());
        } catch (ParseException e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testParseWithRequiredOption() {
        // Create a Parser instance
        Parser parser = new Parser();

        // Define the command line arguments with required option
        String[] args = new String[]{"-o", "option"};

        // Create the required option
        Option option = new Option("o", "option", false, "option description");
        option.setRequired(true);
        Options options = new Options();
        options.addOption(option);

        // Attempt to parse the arguments
        try {
            CommandLine cmd = parser.parse(options, args);
            // Check if the required option is correctly added
            assertTrue(cmd.hasOption("o"));
            assertEquals("option", cmd.getOptionValue("o"));
        } catch (ParseException e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        // Create a Parser instance
        Parser parser = new Parser();

        // Define the command line arguments without required option
        String[] args = new String[]{};

        // Create the required option
        Option option = new Option("o", "option", false, "option description");
        option.setRequired(true);
        Options options = new Options();
        options.addOption(option);

        // Attempt to parse the arguments (should throw exception)
        parser.parse(options, args);
    }

    @Test
    public void testProcessProperties() {
        // Create a Parser instance
        Parser parser = new Parser();

        // Create a properties object with an option
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        // Create the option
        Option option = new Option("a", "description");
        Options options = new Options();
        options.addOption(option);

        // Process the properties
        parser.processProperties(properties);

        // Check if the option is correctly added
        assertTrue(parser.cmd.hasOption("a"));
    }

    @Test
    public void testCheckRequiredOptions() {
        // Create a Parser instance
        Parser parser = new Parser();

        // Create the required options
        Option option1 = new Option("option1", "description");
        Options options = new Options();
        options.addOption(option1);
        parser.setOptions(options);

        // Attempt to check for required options
        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            fail("No options should be missing");
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckMissingRequiredOptions() throws ParseException {
        // Create a Parser instance
        Parser parser = new Parser();

        // Create the required options
        Option option1 = new Option("option1", "description");
        option1.setRequired(true);
        Options options = new Options();
        options.addOption(option1);
        parser.setOptions(options);

        // Attempt to check for required options (should throw exception)
        parser.checkRequiredOptions();
    }
}