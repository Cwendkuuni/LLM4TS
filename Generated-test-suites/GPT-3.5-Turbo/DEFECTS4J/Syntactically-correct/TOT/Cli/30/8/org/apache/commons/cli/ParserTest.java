import org.apache.commons.cli.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class ParserTest {

    private Options options;
    private Parser parser;

    @BeforeEach
    public void setup() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
    }

    @Test
    public void testParse_WithNullOptionsAndArguments_ShouldThrowNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.parse(null, null);
        });
    }

    @Test
    public void testParse_WithNoOptionsAndNoArguments_ShouldReturnEmptyCommandLine() throws ParseException {
        CommandLine commandLine = parser.parse(options, new String[0]);
        Assertions.assertEquals(0, commandLine.getOptions().length);
        Assertions.assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testParse_WithSingleOptionAndNoArguments_ShouldReturnCommandLineWithOption() throws ParseException {
        Option option = new Option("a", "option", false, "Option A");
        options.addOption(option);

        CommandLine commandLine = parser.parse(options, new String[0]);
        Assertions.assertTrue(commandLine.hasOption("a"));
        Assertions.assertFalse(commandLine.hasOption("b"));
        Assertions.assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testParse_WithOptionAndArguments_ShouldReturnCommandLineWithOptionAndArguments() throws ParseException {
        Option option = new Option("a", "option", true, "Option A");
        options.addOption(option);

        String[] arguments = {"argument1", "argument2"};

        CommandLine commandLine = parser.parse(options, arguments);
        Assertions.assertTrue(commandLine.hasOption("a"));
        Assertions.assertFalse(commandLine.hasOption("b"));
        Assertions.assertEquals(2, commandLine.getArgs().length);
        Assertions.assertEquals("argument1", commandLine.getArgs()[0]);
        Assertions.assertEquals("argument2", commandLine.getArgs()[1]);
    }

    @Test
    public void testParse_WithOptionAndProperties_ShouldReturnCommandLineWithOption() throws ParseException {
        Option option = new Option("a", "option", true, "Option A");
        options.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine commandLine = parser.parse(options, new String[0], properties);
        Assertions.assertTrue(commandLine.hasOption("a"));
        Assertions.assertFalse(commandLine.hasOption("b"));
        Assertions.assertEquals("value", commandLine.getOptionValue("a"));
    }

    @Test
    public void testParse_WithRequiredOption_ShouldThrowMissingOptionException() {
        Option option = new Option("a", "option", true, "Option A");
        option.setRequired(true);
        options.addOption(option);

        Assertions.assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, new String[0]);
        });
    }

    @Test
    public void testParse_WithOptionGroupAndOptionPresent_ShouldSelectOptionInGroup() throws ParseException {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", "optionA", false, "Option A"));
        optionGroup.addOption(new Option("b", "optionB", false, "Option B"));
        options.addOptionGroup(optionGroup);

        String[] arguments = {"-a"};

        CommandLine commandLine = parser.parse(options, arguments);
        Assertions.assertTrue(commandLine.hasOption("a"));
        Assertions.assertFalse(commandLine.hasOption("b"));
        Assertions.assertNull(commandLine.getOptionValue("a"));
    }

    @Test
    public void testParse_WithOptionGroupAndNoOptionPresent_ShouldThrowMissingOptionException() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("a", "optionA", false, "Option A"));
        optionGroup.addOption(new Option("b", "optionB", false, "Option B"));
        options.addOptionGroup(optionGroup);

        Assertions.assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, new String[0]);
        });
    }

    @Test
    public void testParse_WithStopAtNonOptionTrueAndNonOptionArguments_ShouldStopParsing() throws ParseException {
        Option option = new Option("a", "option", false, "Option A");
        options.addOption(option);

        String[] arguments = {"nonOption"};

        CommandLine commandLine = parser.parse(options, arguments, true);
        Assertions.assertFalse(commandLine.hasOption("a"));
        Assertions.assertEquals(1, commandLine.getArgs().length);
        Assertions.assertEquals("nonOption", commandLine.getArgs()[0]);
    }

}