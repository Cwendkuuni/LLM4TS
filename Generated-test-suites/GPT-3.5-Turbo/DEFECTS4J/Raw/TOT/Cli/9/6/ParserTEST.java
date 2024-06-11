import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParse_WithNoOptions_ShouldReturnEmptyCommandLine() throws ParseException {
        String[] args = new String[]{};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getOptions().length == 0);
    }

    @Test
    public void testParse_WithOptions_ShouldReturnExpectedCommandLine() throws ParseException {
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);

        String[] args = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParse_WithArguments_ShouldAddArgumentsToCommandLine() throws ParseException {
        String[] args = new String[]{"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getArgs().length == 2);
    }

    @Test(expected = ParseException.class)
    public void testParse_WithInvalidOptions_ShouldThrowParseException() throws ParseException {
        String[] args = new String[]{"-b"};
        parser.parse(options, args);
    }

    @Test
    public void testParse_WithProperties_ShouldProcessPropertiesCorrectly() throws ParseException {
        options.addOption(Option.builder("p").hasArg().build());

        Properties properties = new Properties();
        properties.setProperty("p", "value");

        String[] args = new String[]{};
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("p"));
        assertEquals(cmd.getOptionValue("p"), "value");
    }

    @Test
    public void testParse_WithStopAtNonOption_ShouldStopParsingAtNonOption() throws ParseException {
        options.addOption(Option.builder("o").build());

        String[] args = new String[]{"-o", "arg1", "-a", "arg2"};
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.getArgList().size() == 2);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_WithMissingRequiredOption_ShouldThrowMissingOptionException() throws ParseException {
        Option option = Option.builder("r").required().build();
        options.addOption(option);

        String[] args = new String[]{};
        parser.parse(options, args);
    }

    @Test
    public void testCheckRequiredOptions_WithAllRequiredOptions_ShouldNotThrowException() throws ParseException {
        Option option1 = Option.builder("r1").required().build();
        Option option2 = Option.builder("r2").required().build();
        options.addOption(option1);
        options.addOption(option2);

        String[] args = new String[]{"-r1", "-r2"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("r1"));
        assertTrue(cmd.hasOption("r2"));
    }

    @Test
    public void testProcessArgs_WithMultipleArguments_ShouldAddAllValuesToOption() throws ParseException {
        Option option = Option.builder("o").hasArgs().build();
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();

        parser.processArgs(option, iterator);

        assertTrue(option.getValues().length == 2);
        assertEquals(option.getValue(0), "value1");
        assertEquals(option.getValue(1), "value2");
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_WithMissingRequiredArgument_ShouldThrowMissingArgumentException() throws ParseException {
        Option option = Option.builder("o").hasArg().build();
        ListIterator<String> iterator = Collections.singletonList("-o").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption_WithValidOption_ShouldAddOptionToCommandLine() throws ParseException {
        Option option = Option.builder("o").longOpt("option").hasArg().build();
        ListIterator<String> iterator = Collections.singletonList("-o").listIterator();

        parser.processOption("o", iterator);

        CommandLine cmd = parser.parse(options, new String[]{});
        assertTrue(cmd.hasOption("o"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_WithInvalidOption_ShouldThrowUnrecognizedOptionException() throws ParseException {
        ListIterator<String> iterator = Collections.singletonList("-a").listIterator();
        parser.processOption("a", iterator);
    }

}