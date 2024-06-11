import org.apache.commons.cli.*; // import the necessary packages
import org.junit.Test;
import static org.junit.Assert.*; // for assertions

public class ParserTest {

    @Test
    public void testParseWithValidArguments() throws ParseException {
        // Given
        String[] arguments = new String[]{"--option1", "value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1")
                .desc("Option 1 description");
        options.addOption("option2", true, "o2")
                .desc("Option 2 description");
        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, arguments);

        // Then
        assertTrue(cmd.hasOption("o1"));
        assertTrue(cmd.hasOption("o2"));
        assertEquals("value1", cmd.getOptionValue("o1"));
        assertEquals("value2", cmd.getOptionValue("o2"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        // Given
        String[] arguments = new String[]{"--option1", "value1"};
        Options options = new Options();
        options.addOption("option1", true, "o1")
                .desc("Option 1 description")
                .required(true);
        options.addOption("option2", true, "o2")
                .desc("Option 2 description");
        Parser parser = new DefaultParser();

        // When & Then
        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        // Given
        String[] arguments = new String[]{"--invalidOption", "value1"};
        Options options = new Options();
        options.addOption("option1", true, "o1")
                .desc("Option 1 description");
        options.addOption("option2", true, "o2")
                .desc("Option 2 description");
        Parser parser = new DefaultParser();

        // When & Then
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }

    // Add edge cases and exception handling scenarios here
}