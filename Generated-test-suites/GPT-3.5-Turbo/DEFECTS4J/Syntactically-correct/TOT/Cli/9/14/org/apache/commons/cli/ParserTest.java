import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
    }

    @Test
    public void testParse_WithNullOptions_ShouldThrowParseException() {
        String[] args = {"-arg1", "-arg2"};
        assertThrows(ParseException.class, () -> {
            parser.parse(null, args);
        });
    }

    @Test
    public void testParse_WithNullArguments_ShouldReturnEmptyCommandLine() throws ParseException {
        Options options = new Options();
        CommandLine result = parser.parse(options, null);
        assertNotNull(result);
        assertTrue(result.getArgList().isEmpty());
        assertTrue(result.getOptions().length == 0);
    }

    @Test
    public void testParse_WithEmptyArguments_ShouldReturnEmptyCommandLine() throws ParseException {
        Options options = new Options();
        String[] args = {};
        CommandLine result = parser.parse(options, args);
        assertNotNull(result);
        assertTrue(result.getArgList().isEmpty());
        assertTrue(result.getOptions().length == 0);
    }
    
    @Test
    public void testParse_WithOneOption_ShouldReturnCommandLineWithOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] args = {"-a"};
        CommandLine result = parser.parse(options, args);
        assertNotNull(result);
        assertFalse(result.getArgList().isEmpty());
        assertTrue(result.hasOption("a"));
    }
    
    @Test
    public void testParse_WithMultipleOptionsAndArguments_ShouldReturnCommandLineWithAllOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
        String[] args = {"-a", "-b", "value", "-c", "argument"};
        CommandLine result = parser.parse(options, args);
        assertNotNull(result);
        assertFalse(result.getArgList().isEmpty());
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertTrue(result.getOptionValue("b").equals("value"));
        assertTrue(result.hasOption("c"));
        assertTrue(result.getArgList().contains("argument"));
    }

    @Test
    public void testProcessArgs_WithValidArgument_ShouldAddValueToOption() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator<String> iterator = Arrays.asList("value").listIterator();
        parser.processArgs(option, iterator);
        assertTrue(option.getValues().length == 1);
        assertTrue(option.getValue().equals("value"));
    }

    @Test
    public void testProcessArgs_WithMissingArgument_ShouldThrowMissingArgumentException() {
        Option option = new Option("a", true, "Option A");
        ListIterator<String> iterator = Arrays.asList().listIterator();
        assertThrows(MissingArgumentException.class, () -> {
            parser.processArgs(option, iterator);
        });
    }

    @Test
    public void testProcessOption_WithUnrecognizedOption_ShouldThrowUnrecognizedOptionException() {
        Option option = new Option("a", false, "Option A");
        ListIterator<String> iterator = Arrays.asList("-b").listIterator();
        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.processOption("b", iterator);
        });
    }

    @Test
    public void testProcessOption_WithRequiredOption_ShouldRemoveFromRequiredOptionsList() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", false, "Option A");
        option.setRequired(true);
        options.addOption(option);
        ListIterator<String> iterator = Arrays.asList("-a").listIterator();
        parser.processOption("a", iterator);
        assertTrue(parser.getRequiredOptions().isEmpty());
    }

    @Test
    public void testProcessOption_WithOptionGroup_ShouldSetSelectedOptionOfGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", false, "Option B");
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOptionGroup(group);
        ListIterator<String> iterator = Arrays.asList("-a").listIterator();
        parser.processOption("a", iterator);
        assertNotNull(group.getSelected());
        assertTrue(group.getSelected().equals(optionA));
    }

    @Test
    public void testProcessProperties_WithNullProperties_ShouldDoNothing() {
        Properties properties = null;
        parser.processProperties(properties);
        // No exception should be thrown
    }

    @Test
    public void testCheckRequiredOptions_WithMissingRequiredOptions_ShouldThrowMissingOptionException() {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", false, "Option B");
        optionA.setRequired(true);
        optionB.setRequired(true);
        options.addOption(optionA);
        options.addOption(optionB);
        parser.setOptions(options);
        assertThrows(MissingOptionException.class, () -> {
            parser.checkRequiredOptions();
        });
    }

    @Test
    public void testCheckRequiredOptions_WithAllRequiredOptionsPresent_ShouldNotThrowException() throws MissingOptionException {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", false, "Option B");
        optionA.setRequired(true);
        optionB.setRequired(true);
        options.addOption(optionA);
        options.addOption(optionB);
        parser.setOptions(options);
        parser.getRequiredOptions().clear();
        parser.checkRequiredOptions();
        // No exception should be thrown
    }

    @Test
    public void testSetOptions_ShouldSetOptionsAndInitializeRequiredOptionsList() {
        Options options = new Options();
        Option optionA = new Option("a", false, "Option A");
        Option optionB = new Option("b", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertFalse(parser.getRequiredOptions().isEmpty());
    }

}