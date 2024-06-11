import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }

    @Test
    public void testParse_OptionsAndArguments_ReturnsCommandLine() throws ParseException {
        // Arrange
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("u", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"--option1", "--option2", "value"};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertNotNull(result);
        assertEquals(false, result.hasOption("option1"));
        assertEquals(true, result.hasOption("option2"));
        assertEquals("value", result.getOptionValue("option2"));
    }

    @Test
    public void testParse_OptionsAndNoArguments_ReturnsCommandLine() throws ParseException {
        // Arrange
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option option2 = new Option("u", "option2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {};

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertNotNull(result);
        assertEquals(false, result.hasOption("option1"));
        assertEquals(false, result.hasOption("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionMissing_ThrowsMissingOptionException() throws ParseException {
        // Arrange
        Option option1 = new Option("o", "option1", true, "Required Option 1");
        Option option2 = new Option("u", "option2", true, "Option 2");

        option1.setRequired(true);

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"--option2", "value"};

        // Act
        parser.parse(options, arguments);

        // Assert
        // Expects MissingOptionException to be thrown
    }

    @Test
    public void testProcessArgs_ArgumentValueExists_AddsValueToOption() throws ParseException {
        // Arrange
        Option option = new Option("u", "option", true, "Option");
        option.setArgName("value");

        CmdLineParserTestUtil.setValue(option, "value");

        ListIterator iterator = CmdLineParserTestUtil.createIterator("value");

        // Act
        parser.processArgs(option, iterator);

        // Assert
        assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_ArgumentValueRequiredAndNotPresent_ThrowsMissingArgumentException() throws ParseException {
        // Arrange
        Option option = new Option("u", "option", true, "Option");
        option.setArgName("value");

        ListIterator iterator = CmdLineParserTestUtil.createIterator();

        // Act
        parser.processArgs(option, iterator);

        // Assert
        // Expects MissingArgumentException to be thrown
    }

    @Test
    public void testProcessOption_OptionPresent_ParsesOptionCorrectly() throws ParseException {
        // Arrange
        Option option = new Option("u", "option", true, "Option");
        Option option2 = new Option("v", "option2", false, "Option 2");

        options.addOption(option);
        options.addOption(option2);

        ListIterator iterator = CmdLineParserTestUtil.createIterator("-u", "value");

        // Act
        parser.processOption("-u", iterator);

        // Assert
        CommandLine cmdLine = parser.getCmd();
        assertNotNull(cmdLine);
        assertEquals(false, cmdLine.hasOption("option2"));
        assertEquals(true, cmdLine.hasOption("option"));
        assertEquals("value", cmdLine.getOptionValue("option"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_OptionNotPresent_ThrowsUnrecognizedOptionException() throws ParseException {
        // Arrange
        Option option = new Option("u", "option", true, "Option");
        option.setArgName("value");

        ListIterator iterator = CmdLineParserTestUtil.createIterator();

        // Act
        parser.processOption("-u", iterator);

        // Assert
        // Expects UnrecognizedOptionException to be thrown
    }  

}