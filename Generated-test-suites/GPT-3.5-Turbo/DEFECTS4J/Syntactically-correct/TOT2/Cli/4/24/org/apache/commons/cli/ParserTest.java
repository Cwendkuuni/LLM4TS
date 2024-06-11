import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testFlatten_basic() {
        // Create a Parser instance
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        Options options = new Options();
        String[] arguments = {"-l", "-r", "--option", "value"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testParse_basic() throws ParseException {
        // Create a Parser instance
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        Options options = new Options();
        String[] arguments = {"-l", "-r", "--option", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingArgument() throws ParseException {
        // Create a Parser instance
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        Option option = new Option("o", "option", true, "option description");
        ListIterator<String> iterator = Arrays.asList("-o").listIterator();
        parser.processArgs(option, iterator);
    }

    // Add more test cases here for each method in the Parser class

}