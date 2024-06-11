import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();

        // create option objects
        Option optionA = new Option("a", "option A");
        Option optionB = new Option("b", "option B");
        
        // add options to the options object
        options.addOption(optionA);
        options.addOption(optionB);
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        
        String[] arguments = null;
        
        CommandLine result = parser.parse(options, arguments);
        
        assertFalse("Option A not present", result.hasOption("a"));
        assertFalse("Option B not present", result.hasOption("b"));
        
        arguments = new String[] {"-a"};
        
        result = parser.parse(options, arguments);
        
        assertTrue("Option A present", result.hasOption("a"));
        assertFalse("Option B not present", result.hasOption("b"));
        
        arguments = new String[] {"-a", "-b"};
        
        result = parser.parse(options, arguments);
        
        assertTrue("Option A present", result.hasOption("a"));
        assertTrue("Option B present", result.hasOption("b"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();

        // create option objects
        Option optionA = new Option("a", "option A");
        optionA.setArgs(2);

        // add options to the options object
        options.addOption(optionA);

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        CommandLine cmd = new CommandLine();
        ListIterator<String> iterator = Arrays.asList(new String[]{"arg1", "arg2"}).listIterator();

        parser.processArgs(optionA, iterator);

        String[] values = optionA.getValues();
        assertEquals("Invalid number of values", 2, values.length);
        assertEquals("Invalid value", "arg1", values[0]);
        assertEquals("Invalid value", "arg2", values[1]);

        iterator = Arrays.asList(new String[]{"arg1"}).listIterator();

        parser.processArgs(optionA, iterator);

        values = optionA.getValues();
        assertEquals("Invalid number of values", 1, values.length);
        assertEquals("Invalid value", "arg1", values[0]);

        iterator = Arrays.asList(new String[]{}).listIterator();

        Exception exception = assertThrows(MissingArgumentException.class, () -> {
            parser.processArgs(optionA, iterator);
        });

        String expectedMessage = "Missing argument for option:a";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();

        // create option objects
        Option optionA = new Option("a", "option A");
        Option optionB = new Option("b", "option B");

        // add options to the options object
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        CommandLine cmd = new CommandLine();
        cmd.addOption(optionB);

        ListIterator<String> iterator = Arrays.asList(new String[]{"-a"}).listIterator();

        parser.processOption("-a", iterator);

        assertTrue("Option A present", cmd.hasOption("a"));

        iterator = Arrays.asList(new String[]{"-c"}).listIterator();

        Exception exception = assertThrows(UnrecognizedOptionException.class, () -> {
            parser.processOption("-c", iterator);
        });

        String expectedMessage = "Unrecognized option: -c";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}