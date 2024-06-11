import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserStub();
        options = new Options();
    }

    @Test
    public void testParse_NoOptions() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));
        assertEquals("arg3", cmd.getArg(2));
    }

    @Test
    public void testParse_SingleOption() {
        options.addOption(new Option("a", false, "Option A"));

        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParse_MultipleOptions() {
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", false, "Option B"));
        options.addOption(new Option("c", false, "Option C"));

        String[] arguments = {"-a", "-b", "-c"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testParse_OptionWithValue() {
        options.addOption(new Option("a", true, "Option A"));

        String[] arguments = {"-a", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParse_UnknownOption() {
        String[] arguments = {"-x"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_OptionWithValue_MissingValue() {
        options.addOption(new Option("a", true, "Option A"));

        String[] arguments = {"-a"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_StopAtNonOption() {
        options.addOption(new Option("a", false, "Option A"));

        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, true);

        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("-b", cmd.getArg(1));
        assertEquals("arg2", cmd.getArg(2));
    }

    private class ParserStub extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}