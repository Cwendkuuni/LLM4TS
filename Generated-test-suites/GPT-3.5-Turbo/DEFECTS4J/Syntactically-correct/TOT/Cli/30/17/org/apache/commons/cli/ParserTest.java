import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_WithNullOptionsAndArguments_ShouldReturnEmptyCommandLine() {
        // Arrange
        Options options = null;
        String[] arguments = null;
        Parser parser = new ParserStub();

        // Act
        CommandLine result = parser.parse(options, arguments);

        // Assert
        assertNotNull(result);
        assertTrue(result.getOptions().isEmpty());
        assertTrue(result.getArgs().isEmpty());
    }

    @Test
    public void testParse_WithOptionsAndArguments_ShouldReturnParsedException() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"arg1"};
        Parser parser = new ParserStub();

        // Act & Assert
        try {
            parser.parse(options, arguments);
            fail("ParseException should be thrown");
        } catch (ParseException ex) {
            assertEquals("Unrecognized option: arg1", ex.getMessage());
        }
    }

    @Test
    public void testParse_WithOptionsArgumentsAndStopAtNonOptionTrue_ShouldReturnOnlyOptions() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        Parser parser = new ParserStub();

        // Act
        CommandLine result = parser.parse(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertFalse(result.hasArg("arg1"));
        assertFalse(result.hasArg("arg2"));
    }

    @Test
    public void testParse_WithOptionsAndRequiredOptionMissing_ShouldReturnMissingOptionException() {
        // Arrange
        Options options = new Options();
        Option requiredOption = new Option("r", true, "Required Option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a", "value"};
        Parser parser = new ParserStub();

        // Act & Assert
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException ex) {
            assertEquals("[r]", ex.getMissingOptions().toString());
        }
    }
    
    @Test
    public void testParse_WithOptionsAndProperties_ShouldReturnParsedCommandLineWithOptionsAndProperties() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-b", "value"};
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("c", "true");
        Parser parser = new ParserStub();

        // Act
        CommandLine result = parser.parse(options, arguments, properties);

        // Assert
        assertNotNull(result);
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertTrue(result.hasOption("c"));
    }

    @Test
    public void testProcessArgs_WithOptionAndArguments_ShouldAddValuesToOption() {
        // Arrange
        Option option = new Option("a", true, "Option A");
        ListIterator<String> iter = Arrays.asList("value1", "value2").listIterator();
        Parser parser = new ParserStub();
        
        // Act
        parser.processArgs(option, iter);
        
        // Assert
        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testProcessArgs_WithOptionAndMissingArgument_ShouldThrowMissingArgumentException() {
        // Arrange
        Option option = new Option("a", true, "Option A");
        ListIterator<String> iter = Arrays.asList().listIterator();
        Parser parser = new ParserStub();

        // Act & Assert
        try {
            parser.processArgs(option, iter);
            fail("MissingArgumentException should be thrown");
        } catch (MissingArgumentException ex) {
            assertEquals("Missing argument for option: a", ex.getMessage());
        }
    }

    @Test
    public void testProcessOption_WithValidOptionAndArguments_ShouldAddOptionToCommandLine() {
        // Arrange
        String arg = "-a";
        ListIterator<String> iter = Arrays.asList("value").listIterator();
        Parser parser = new ParserStub();
        CommandLine commandLine = new CommandLine();
        
        // Act
        parser.processOption(arg, iter);
        
        // Assert
        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testProcessOption_WithInvalidOption_ShouldThrowUnrecognizedOptionException() {
        // Arrange
        String arg = "-x";
        ListIterator<String> iter = Arrays.asList().listIterator();
        Parser parser = new ParserStub();
        
        // Act & Assert
        try {
            parser.processOption(arg, iter);
            fail("UnrecognizedOptionException should be thrown");
        } catch (UnrecognizedOptionException ex) {
            assertEquals("Unrecognized option: -x", ex.getMessage());
        }
    }

    private static class ParserStub extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
        
        @Override
        protected void processArgs(Option opt, ListIterator iter) throws ParseException {
            super.processArgs(opt, iter);
        }
        
        @Override
        protected void processOption(String arg, ListIterator iter) throws ParseException {
            super.processOption(arg, iter);
        }
    }
}