import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_OptionsArray_ReturnsCommandLine() throws ParseException {
        Options options = createMockOptions();
        String[] arguments = {"-a", "arg1", "-b", "arg2"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        CommandLine cmd = parser.parse(options, arguments);
        
        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_OptionsArray_ThrowsMissingOptionException() throws ParseException {
        Options options = createMockOptions();
        String[] arguments = {"-a"};
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        parser.parse(options, arguments);
    }
    
    private Options createMockOptions() {
        Option option1 = Option.builder("a")
                .argName("arg1")
                .required()
                .hasArg()
                .build();
        
        Option option2 = Option.builder("b")
                .argName("arg2")
                .required()
                .hasArg()
                .build();
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        return options;
    }
}