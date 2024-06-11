
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ParserTest");
    }
    
    @Test
    public void testParser() throws ParseException {
        // create the Parser object
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
        
        // create example Options
        Option option1 = Option.builder("a")
                            .longOpt("option1")
                            .desc("Option 1")
                            .required()
                            .build();
        Option option2 = Option.builder("b")
                            .longOpt("option2")
                            .desc("Option 2")
                            .hasArg()
                            .optionalArg(true)
                            .build();
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        // create example arguments
        String[] arguments = new String[] {"-a", "--option2", "value1"};
        
        // parse the arguments
        CommandLine cmd = parser.parse(options, arguments);
        
        // assert that the options are correctly parsed
        assert(cmd.hasOption("a"));
        assert(cmd.hasOption("option1"));
        assert(cmd.hasOption("b"));
        assert(cmd.getOptionValue("b").equals("value1"));
    }
}
