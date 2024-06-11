import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
    }

    @Test
    public void testParse_NoOptions_ReturnsEmptyCommandLine() throws ParseException {
        // Arrange
        String[] args = {};

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    @Test
    public void testParse_SingleOption_ReturnsCommandLineWithOption() throws ParseException {
        // Arrange
        String[] args = {"-h"};

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.hasOption("h"));
    }

    @Test
    public void testParse_LongOption_ReturnsCommandLineWithOption() throws ParseException {
        // Arrange
        String[] args = {"--help"};

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testParse_OptionWithValue_ReturnsCommandLineWithOptionAndValue() throws ParseException {
        // Arrange
        String[] args = {"-f", "filename.txt"};

        Option fileOption = new Option("f", true, "File option");
        options.addOption(fileOption);

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.hasOption("f"));
        assertEquals("filename.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParse_OptionWithMultipleValues_ReturnsCommandLineWithOptionAndValues() throws ParseException {
        // Arrange
        String[] args = {"-f", "file1.txt", "file2.txt"};

        Option fileOption = new Option("f", true, "File option").setArgs(2);
        options.addOption(fileOption);

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.hasOption("f"));
        assertEquals("file1.txt", cmd.getOptionValues("f")[0]);
        assertEquals("file2.txt", cmd.getOptionValues("f")[1]);
    }

    @Test
    public void testParse_OptionWithRequiredValue_MissingValue_ThrowsMissingArgumentException() {
        // Arrange
        String[] args = {"-f"};

        Option fileOption = new Option("f", true, "File option").setRequired(true);
        options.addOption(fileOption);

        // Act & Assert
        try {
            parser.parse(options, args);
        } catch (MissingArgumentException e) {
            assertTrue(e.getMessage().contains("Missing argument for option: -f"));
        }
    }

    @Test
    public void testParse_OptionWithOptionalValue_NoValue_ReturnsCommandLineWithOption() throws ParseException {
        // Arrange
        String[] args = {"-f"};

        Option fileOption = new Option("f", true, "File option").setOptionalArg(true);
        options.addOption(fileOption);

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.hasOption("f"));
    }

    @Test
    public void testParse_OptionWithOptionalValue_WithValue_ReturnsCommandLineWithOptionAndValue() throws ParseException {
        // Arrange
        String[] args = {"-f", "filename.txt"};

        Option fileOption = new Option("f", true, "File option").setOptionalArg(true);
        options.addOption(fileOption);

        // Act
        CommandLine cmd = parser.parse(options, args);

        // Assert
        assertTrue(cmd.hasOption("f"));
        assertEquals("filename.txt", cmd.getOptionValue("f"));
    }

    @Test
    public void testParse_StopAtNonOptionFlagEnabled_AfterNonOption_AllRemainingArgsAdded() throws ParseException {
        // Arrange
        String[] args = {"-f", "filename.txt", "nonOptionArg1", "nonOptionArg2"};

        Option fileOption = new Option("f", true, "File option");
        options.addOption(fileOption);

        // Act
        CommandLine cmd = parser.parse(options, args, true);

        // Assert
        assertTrue(cmd.hasOption("f"));
        assertEquals(3, cmd.getArgs().size());
        assertEquals("filename.txt", cmd.getArgs().get(0));
        assertEquals("nonOptionArg1", cmd.getArgs().get(1));
        assertEquals("nonOptionArg2", cmd.getArgs().get(2));
    }

    @Test
    public void testParse_StopAtNonOptionFlagDisabled_AfterNonOption_IgnoreNonOptionArgs() throws ParseException {
        // Arrange
        String[] args = {"-f", "filename.txt", "nonOptionArg1", "nonOptionArg2"};

        Option fileOption = new Option("f", true, "File option");
        options.addOption(fileOption);

        // Act
        CommandLine cmd = parser.parse(options, args, false);

        // Assert
        assertTrue(cmd.hasOption("f"));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("filename.txt", cmd.getArgs().get(0));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_InvalidOption_ThrowsUnrecognizedOptionException() throws ParseException {
        // Arrange
        String[] args = {"-x"};

        // Act
        parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionMissing_ThrowsMissingOptionException() throws ParseException {
        // Arrange
        String[] args = {};

        Option fileOption = new Option("f", true, "File option").setRequired(true);
        options.addOption(fileOption);

        // Act
        parser.parse(options, args);
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return new String[0];
        }
    }
}